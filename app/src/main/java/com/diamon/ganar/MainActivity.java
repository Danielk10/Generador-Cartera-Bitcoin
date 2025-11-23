package com.diamon.ganar; // Unificado

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

// Esta clase se genera automáticamente tras hacer "Sync Project with Gradle Files"
// si tienes viewBinding true y el xml correcto.
import com.diamon.ganar.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private boolean isPrivKeyVisible = false;

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        // Reseteamos visualmente
                        binding.inputSeed.setText("Archivo seleccionado: " + uri.getLastPathSegment());
                        binding.inputSeed.setEnabled(false);
                        // Llamamos al ViewModel
                        viewModel.generateFromFile(uri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.toolbar); // Si tienes toolbar, si no, borrar esta línea o agregarla al XML

        setupListeners();
        setupObservers();
    }

    private void setupListeners() {
        binding.btnGenerate.setOnClickListener(v -> {
            binding.inputSeed.setEnabled(true);
            String seed = binding.inputSeed.getText().toString();
            viewModel.generateFromText(seed);
        });

        binding.btnLoadFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            filePickerLauncher.launch(intent);
        });

        binding.btnClear.setOnClickListener(v -> {
            binding.inputSeed.setText("");
            binding.inputSeed.setEnabled(true);
            clearResults();
        });

        binding.btnToggleVisibility.setOnClickListener(v -> {
            isPrivKeyVisible = !isPrivKeyVisible;
            updateVisibility();

            int iconRes = isPrivKeyVisible ? R.drawable.ic_visibility_off : R.drawable.ic_visibility;
            binding.btnToggleVisibility.setIconResource(iconRes);
        });
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnGenerate.setEnabled(!isLoading);
            binding.btnLoadFile.setEnabled(!isLoading);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                // Limpiar error para no mostrarlo de nuevo al rotar pantalla
                // (opcionalmente resetear en ViewModel)
            }
        });

        viewModel.getWalletResult().observe(this, result -> {
            if (result != null) {
                binding.tvPrivateKey.setText(result.privateKeyHex);
                binding.tvWif.setText(result.wif);
                binding.tvPublicKey.setText(result.publicKeyHex);
                binding.tvAddress.setText(result.address);
                binding.resultsLayout.setVisibility(View.VISIBLE);
                updateVisibility();
            }
        });
    }

    private void updateVisibility() {
        if (isPrivKeyVisible) {
            binding.tvPrivateKey.setTransformationMethod(null);
            binding.tvWif.setTransformationMethod(null);
        } else {
            binding.tvPrivateKey.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
            binding.tvWif.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
        }
    }

    private void clearResults() {
        binding.resultsLayout.setVisibility(View.GONE);
        binding.tvPrivateKey.setText("");
        binding.tvWif.setText("");
        binding.tvPublicKey.setText("");
        binding.tvAddress.setText("");
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
package com.diamon.ganar; // Unificado

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

// Esta clase se genera automáticamente tras hacer "Sync Project with Gradle
// Files"
// si tienes viewBinding true y el xml correcto.
import com.diamon.ganar.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private boolean isPrivKeyVisible = false;

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        // Reseteamos visualmente
                        binding.inputSeed.setText("Archivo seleccionado: " + uri.getLastPathSegment());
                        binding.inputSeed.setEnabled(false);
                        // Llamamos al ViewModel
                        viewModel.generateFromFile(uri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.toolbar); // Si tienes toolbar, si no, borrar esta línea o agregarla al XML

        setupListeners();
        setupObservers();
    }

    private void setupListeners() {
        binding.btnGenerate.setOnClickListener(v -> {
            binding.inputSeed.setEnabled(true);
            String seed = binding.inputSeed.getText().toString();
            viewModel.generateFromText(seed);
        });

        binding.btnLoadFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            filePickerLauncher.launch(intent);
        });

        binding.btnClear.setOnClickListener(v -> {
            binding.inputSeed.setText("");
            binding.inputSeed.setEnabled(true);
            clearResults();
        });

        binding.btnToggleVisibility.setOnClickListener(v -> {
            isPrivKeyVisible = !isPrivKeyVisible;
            updateVisibility();

            int iconRes = isPrivKeyVisible ? R.drawable.ic_visibility_off : R.drawable.ic_visibility;
            binding.btnToggleVisibility.setIconResource(iconRes);
        });
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.btnGenerate.setEnabled(!isLoading);
            binding.btnLoadFile.setEnabled(!isLoading);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                // Limpiar error para no mostrarlo de nuevo al rotar pantalla
                // (opcionalmente resetear en ViewModel)
            }
        });

        viewModel.getWalletResult().observe(this, result -> {
            if (result != null) {
                binding.tvPrivateKey.setText(result.privateKeyHex);
                binding.tvWif.setText(result.wif);
                binding.tvPublicKey.setText(result.publicKeyHex);
                binding.tvAddress.setText(result.address);
                binding.resultsLayout.setVisibility(View.VISIBLE);
                updateVisibility();
            }
        });
    }

    private void updateVisibility() {
        if (isPrivKeyVisible) {
            binding.tvPrivateKey.setTransformationMethod(null);
            binding.tvWif.setTransformationMethod(null);
        } else {
            binding.tvPrivateKey.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
            binding.tvWif.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
        }
    }

    private void clearResults() {
        binding.resultsLayout.setVisibility(View.GONE);
        binding.tvPrivateKey.setText("");
        binding.tvWif.setText("");
        binding.tvPublicKey.setText("");
        binding.tvAddress.setText("");
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

    private void showAboutDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Acerca de Bitcoin Wallet")
                .setIcon(R.drawable.ic_btc_shield)
                .setMessage("Generador de carteras Bitcoin seguro y offline.\n\n" +
                        "Esta aplicación replica exactamente la lógica criptográfica estándar de Bitcoin:\n" +
                        "• SHA-256 para entropía\n" +
                        "• ECDSA (secp256k1) para claves\n" +
                        "• RIPEMD-160 para direcciones\n\n" +
                        "Código abierto y verificable. No confíes, verifica.")
                .setPositiveButton("Entendido", null)
                .show();
    }

    private void showPrivacyDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Política de Privacidad")
                .setIcon(R.drawable.ic_btc_shield)
                .setMessage("Tu privacidad es absoluta.\n\n" +
                        "1. 100% Offline: Esta app no tiene permiso de internet.\n" +
                        "2. Sin Rastreo: No recolectamos datos, analíticas ni información personal.\n" +
                        "3. Efímero: Las claves se generan en la memoria RAM y se borran al cerrar la app.\n\n" +
                        "Cumple con las Políticas de Google Play 2025.")
                .setPositiveButton("Aceptar", null)
                .show();
    }
}