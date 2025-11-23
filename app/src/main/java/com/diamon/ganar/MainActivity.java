package com.diamon.ganar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.diamon.ganar.databinding.ActivityMainBinding;
import com.diamon.ganar.model.FileProcessingResult;
import com.diamon.ganar.model.WalletData;
import com.diamon.ganar.utils.ClipboardUtils;
import com.diamon.ganar.utils.SecurityUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

/**
 * Activity principal para generación de carteras Bitcoin.
 * Implementa Material Design 3 con funcionalidad de copiar y seguridad
 * mejorada.
 * 
 * @author Bitcoin Wallet Generator
 * @version 2.0
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private boolean isPrivKeyVisible = false;

    /**
     * Launcher para selección de archivos.
     */
    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        viewModel.generateFromFile(uri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.toolbar);

        setupListeners();
        setupObservers();
    }

    /**
     * Configura listeners de botones y eventos de UI.
     */
    private void setupListeners() {
        // Botón generar desde texto
        binding.btnGenerate.setOnClickListener(v -> {
            binding.inputSeed.setEnabled(true);
            String seed = binding.inputSeed.getText().toString();

            if (seed.isEmpty()) {
                Snackbar.make(binding.getRoot(), getString(R.string.msg_enter_seed), Snackbar.LENGTH_SHORT).show();
                return;
            }

            viewModel.generateFromText(seed);
        });

        // Botón cargar archivo
        binding.btnLoadFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            filePickerLauncher.launch(intent);
        });

        // Botón limpiar
        binding.btnClear.setOnClickListener(v -> {
            binding.inputSeed.setText("");
            binding.inputSeed.setEnabled(true);
            clearResults();
            viewModel.clearWalletData();
        });

        // Botón toggle visibilidad
        binding.btnToggleVisibility.setOnClickListener(v -> {
            isPrivKeyVisible = !isPrivKeyVisible;
            updateVisibility();

            // Actualizar icono
            int iconRes = isPrivKeyVisible ? R.drawable.ic_visibility_off : R.drawable.ic_visibility;
            binding.btnToggleVisibility.setIconResource(iconRes);

            // Actualizar texto
            binding.btnToggleVisibility
                    .setText(isPrivKeyVisible ? getString(R.string.btn_hide) : getString(R.string.btn_show));

            // Aplicar/remover FLAG_SECURE
            if (isPrivKeyVisible) {
                SecurityUtils.enableScreenshotProtection(this);
            } else {
                SecurityUtils.disableScreenshotProtection(this);
            }
        });

        // Botones de copiar (se configurarán cuando haya datos)
    }

    /**
     * Configura observadores de LiveData del ViewModel.
     */
    private void setupObservers() {
        // Observar estado de carga
        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnGenerate.setEnabled(!isLoading);
            binding.btnLoadFile.setEnabled(!isLoading);
        });

        // Observar mensajes de error
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
            }
        });

        // Observar información de archivo procesado
        viewModel.getFileProcessingInfo().observe(this, fileInfo -> {
            if (fileInfo != null) {
                updateFileInfo(fileInfo);
            }
        });

        // Observar datos de cartera
        viewModel.getWalletData().observe(this, walletData -> {
            if (walletData != null && walletData.isValid()) {
                displayWalletData(walletData);
            }
        });
    }

    /**
     * Muestra información del archivo procesado.
     */
    private void updateFileInfo(FileProcessingResult fileInfo) {
        String message = getString(R.string.file_info_template, fileInfo.fileName,
                FileProcessingResult.formatSize(fileInfo.originalSize));

        if (fileInfo.wasCompressed) {
            message += getString(R.string.file_compressed_template, fileInfo.getCompressionRatio(),
                    FileProcessingResult.formatSize(fileInfo.processedSize));
        }

        binding.inputSeed.setText(message);
        binding.inputSeed.setEnabled(false);
    }

    /**
     * Muestra los datos de la cartera generada.
     */
    private void displayWalletData(WalletData wallet) {
        // Mostrar datos
        binding.tvPrivateKey.setText(wallet.privateKeyHex);
        binding.tvWif.setText(wallet.wif);
        binding.tvPublicKey.setText(wallet.publicKeyHex);
        binding.tvAddress.setText(wallet.address);

        // Mostrar layout de resultados
        binding.resultsLayout.setVisibility(View.VISIBLE);

        // Aplicar visibilidad inicial (oculto)
        updateVisibility();

        // Configurar botones de copiar
        setupCopyButtons(wallet);
    }

    /**
     * Configura botones de copiar para cada campo.
     */
    private void setupCopyButtons(WalletData wallet) {
        // Copiar dirección (seguro)
        binding.btnCopyAddress.setOnClickListener(v -> ClipboardUtils.copyToClipboard(this, binding.getRoot(),
                getString(R.string.copy_address), wallet.address));

        // Copiar clave pública (seguro)
        binding.btnCopyPublicKey.setOnClickListener(v -> ClipboardUtils.copyToClipboard(this, binding.getRoot(),
                getString(R.string.copy_public_key), wallet.publicKeyHex));

        // Copiar clave privada (sensible - con confirmación)
        binding.btnCopyPrivateKey
                .setOnClickListener(
                        v -> showCopyConfirmationDialog(getString(R.string.copy_private_key), wallet.privateKeyHex));

        // Copiar WIF (sensible - con confirmación)
        binding.btnCopyWif
                .setOnClickListener(v -> showCopyConfirmationDialog(getString(R.string.copy_wif), wallet.wif));
    }

    /**
     * Muestra diálogo de confirmación para copiar datos sensibles.
     */
    private void showCopyConfirmationDialog(String label, String data) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.dialog_warning_title))
                .setMessage(getString(R.string.dialog_warning_msg, label))
                .setPositiveButton(getString(R.string.btn_yes_copy),
                        (dialog, which) -> ClipboardUtils.copySensitiveToClipboard(this, binding.getRoot(), label,
                                data))
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show();
    }

    /**
     * Actualiza la visibilidad de claves privadas.
     */
    private void updateVisibility() {
        if (isPrivKeyVisible) {
            binding.tvPrivateKey.setTransformationMethod(null);
            binding.tvWif.setTransformationMethod(null);
        } else {
            binding.tvPrivateKey.setTransformationMethod(
                    new android.text.method.PasswordTransformationMethod());
            binding.tvWif.setTransformationMethod(
                    new android.text.method.PasswordTransformationMethod());
        }
    }

    /**
     * Limpia los resultados mostrados.
     */
    private void clearResults() {
        binding.resultsLayout.setVisibility(View.GONE);
        binding.tvPrivateKey.setText("");
        binding.tvWif.setText("");
        binding.tvPublicKey.setText("");
        binding.tvAddress.setText("");

        // Resetear visibilidad
        isPrivKeyVisible = false;
        binding.btnToggleVisibility.setText(getString(R.string.btn_show));
        binding.btnToggleVisibility.setIconResource(R.drawable.ic_visibility);

        // Remover FLAG_SECURE
        SecurityUtils.disableScreenshotProtection(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            showAboutDialog();
            return true;
        } else if (item.getItemId() == R.id.action_privacy) {
            showPrivacyDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Muestra diálogo "Acerca de" con información de la app.
     */
    private void showAboutDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.dialog_about_title))
                .setIcon(R.drawable.ic_btc_shield)
                .setMessage(
                        getString(R.string.dialog_about_msg, "https://github.com/Danielk10/Generador-Cartera-Bitcoin"))
                .setPositiveButton("Entendido", null)
                .show();
    }

    /**
     * Muestra diálogo de política de privacidad.
     */
    private void showPrivacyDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.dialog_privacy_title))
                .setIcon(R.drawable.ic_btc_shield)
                .setMessage(getString(R.string.dialog_privacy_msg, "https://generadorcarterasbitcoin.blogspot.com/"))
                .setPositiveButton("Aceptar", null)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Por seguridad, ocultar claves al pausar
        if (isPrivKeyVisible) {
            isPrivKeyVisible = false;
            updateVisibility();
            binding.btnToggleVisibility.setText(getString(R.string.btn_show));
            binding.btnToggleVisibility.setIconResource(R.drawable.ic_visibility);
        }

        // Remover FLAG_SECURE
        SecurityUtils.disableScreenshotProtection(this);

        // Opcional: limpiar portapapeles
        // ClipboardUtils.clearClipboard(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpiar binding
        binding = null;
    }
}