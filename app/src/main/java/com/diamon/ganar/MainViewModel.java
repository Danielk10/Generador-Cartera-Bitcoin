package com.diamon.ganar; // Unificado

import android.app.Application;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {

    public static class WalletResult {
        public final String privateKeyHex;
        public final String wif;
        public final String publicKeyHex;
        public final String address;

        public WalletResult(String pk, String wif, String pub, String addr) {
            this.privateKeyHex = pk;
            this.wif = wif;
            this.publicKeyHex = pub;
            this.address = addr;
        }
    }

    private final MutableLiveData<WalletResult> walletResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<WalletResult> getWalletResult() { return walletResult; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public void generateFromText(String seedText) {
        if (seedText == null || seedText.isEmpty()) {
            errorMessage.setValue("Por favor ingrese un texto semilla.");
            return;
        }
        processSeed(seedText.getBytes(StandardCharsets.UTF_8));
    }

    public void generateFromFile(Uri fileUri) {
        isLoading.setValue(true);
        executor.execute(() -> {
            try (InputStream inputStream = getApplication().getContentResolver().openInputStream(fileUri);
                 ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

                if (inputStream == null) throw new Exception("No se pudo abrir el archivo");

                // Lectura segura del archivo completo en RAM
                int nRead;
                byte[] data = new byte[4096]; // Buffer de 4KB
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                byte[] completeBytes = buffer.toByteArray();

                // Procesar (volvemos a llamar a processSeed pero ya dentro del hilo secundario)
                processSeedInternal(completeBytes);

            } catch (Exception e) {
                errorMessage.postValue("Error leyendo archivo: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    private void processSeed(byte[] seedBytes) {
        isLoading.setValue(true);
        executor.execute(() -> processSeedInternal(seedBytes));
    }

    // Método interno que ya corre en el hilo secundario
    private void processSeedInternal(byte[] seedBytes) {
        try {
            byte[] privKeyBytes = BitcoinUtils.generatePrivateKeyFromSeed(seedBytes);
            String privKeyHex = BitcoinUtils.bytesToHex(privKeyBytes);
            String wif = BitcoinUtils.getWIF(privKeyBytes);
            byte[] pubKeyBytes = BitcoinUtils.getPublicKey(privKeyBytes);
            String pubKeyHex = BitcoinUtils.bytesToHex(pubKeyBytes);
            String address = BitcoinUtils.getAddress(pubKeyBytes);

            walletResult.postValue(new WalletResult(privKeyHex, wif, pubKeyHex, address));

        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.postValue("Error criptográfico: " + e.getMessage());
        } finally {
            isLoading.postValue(false);
        }
    }
}