package com.diamon.ganar;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.diamon.ganar.model.FileProcessingResult;
import com.diamon.ganar.model.WalletData;
import com.diamon.ganar.utils.CryptoUtils;
import com.diamon.ganar.utils.FileUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel para la generación de carteras Bitcoin.
 * Maneja el estado de la UI y ejecuta operaciones criptográficas en segundo
 * plano.
 * 
 * @author Bitcoin Wallet Generator
 * @version 2.0
 */
public class MainViewModel extends AndroidViewModel {

    // LiveData para resultados de cartera
    private final MutableLiveData<WalletData> walletData = new MutableLiveData<>();

    // LiveData para estado de carga
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    // LiveData para mensajes de error
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // LiveData para información de archivo procesado
    private final MutableLiveData<FileProcessingResult> fileProcessingInfo = new MutableLiveData<>();

    // Executor para operaciones en segundo plano
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    // Getters para LiveData
    public LiveData<WalletData> getWalletData() {
        return walletData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<FileProcessingResult> getFileProcessingInfo() {
        return fileProcessingInfo;
    }

    /**
     * Genera cartera desde texto.
     * 
     * @param seedText Texto semilla
     */
    public void generateFromText(String seedText) {
        if (seedText == null || seedText.isEmpty()) {
            errorMessage.setValue("Por favor ingrese un texto semilla.");
            return;
        }

        // Limpiar información de archivo anterior
        fileProcessingInfo.setValue(null);

        // Procesar semilla de texto
        processSeed(seedText.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera cartera desde archivo.
     * Aplica límites de tamaño y compresión automática.
     * 
     * @param fileUri URI del archivo seleccionado
     */
    public void generateFromFile(Uri fileUri) {
        isLoading.setValue(true);
        executor.execute(() -> {
            try {
                // Procesar archivo con límites y compresión
                FileProcessingResult result = FileUtils.processFile(
                        getApplication().getApplicationContext(),
                        fileUri);

                // Publicar información del archivo
                fileProcessingInfo.postValue(result);

                // Procesar bytes del archivo como semilla
                processSeedInternal(result.processedBytes);

            } catch (Exception e) {
                errorMessage.postValue("Error procesando archivo: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    /**
     * Procesa semilla en segundo plano.
     * 
     * @param seedBytes Bytes de la semilla
     */
    private void processSeed(byte[] seedBytes) {
        isLoading.setValue(true);
        executor.execute(() -> processSeedInternal(seedBytes));
    }

    /**
     * Método interno que ejecuta la generación criptográfica.
     * Ya corre en hilo secundario.
     * 
     * @param seedBytes Bytes de la semilla
     */
    private void processSeedInternal(byte[] seedBytes) {
        try {
            // Generar clave privada: SHA256(SHA256(seed))
            byte[] privateKeyBytes = CryptoUtils.generatePrivateKey(seedBytes);
            String privateKeyHex = CryptoUtils.bytesToHex(privateKeyBytes);

            // Generar WIF
            String wif = CryptoUtils.generateWIF(privateKeyBytes);

            // Derivar clave pública
            byte[] publicKeyBytes = CryptoUtils.derivePublicKey(privateKeyBytes);
            String publicKeyHex = CryptoUtils.bytesToHex(publicKeyBytes);

            // Generar dirección Bitcoin
            String address = CryptoUtils.generateAddress(publicKeyBytes);

            // Crear modelo de datos inmutable
            WalletData wallet = new WalletData(
                    privateKeyHex,
                    wif,
                    publicKeyHex,
                    address);

            // Publicar resultado
            walletData.postValue(wallet);

        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.postValue("Error criptográfico: " + e.getMessage());
        } finally {
            isLoading.postValue(false);
        }
    }

    /**
     * Limpia los datos de la cartera.
     * Útil para seguridad al pausar la app.
     */
    public void clearWalletData() {
        walletData.setValue(null);
        fileProcessingInfo.setValue(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Cerrar executor al destruir ViewModel
        executor.shutdown();
    }
}