package com.danielk.generadorcarterabitcoin.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.danielk.generadorcarterabitcoin.crypto.KeyGenerator;
import com.danielk.generadorcarterabitcoin.model.WalletData;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WalletViewModel extends ViewModel {
    private final MutableLiveData<WalletData> walletLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final Executor executor = Executors.newSingleThreadExecutor();
    
    public LiveData<WalletData> getWalletLiveData() {
        return walletLiveData;
    }
    
    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }
    
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
    
    public void generateWalletFromText(String seedText) {
        loadingLiveData.postValue(true);
        
        executor.execute(() -> {
            try {
                WalletData wallet = KeyGenerator.generateWalletFromText(seedText);
                walletLiveData.postValue(wallet);
                errorLiveData.postValue(null);
            } catch (Exception e) {
                errorLiveData.postValue("Error: " + e.getMessage());
            } finally {
                loadingLiveData.postValue(false);
            }
        });
    }
    
    public void generateWalletFromFile(byte[] fileBytes) {
        loadingLiveData.postValue(true);
        
        executor.execute(() -> {
            try {
                WalletData wallet = KeyGenerator.generateWalletFromFile(fileBytes);
                walletLiveData.postValue(wallet);
                errorLiveData.postValue(null);
            } catch (Exception e) {
                errorLiveData.postValue("Error: " + e.getMessage());
            } finally {
                loadingLiveData.postValue(false);
            }
        });
    }
}