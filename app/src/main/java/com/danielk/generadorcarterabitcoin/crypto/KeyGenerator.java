package com.danielk.generadorcarterabitcoin.crypto;

import com.danielk.generadorcarterabitcoin.model.WalletData;
import java.nio.charset.StandardCharsets;

public class KeyGenerator {
    
    public static WalletData generateWalletFromText(String seedText) throws Exception {
        if (seedText == null || seedText.trim().isEmpty()) {
            throw new IllegalArgumentException("La semilla no puede estar vacía");
        }
        byte[] seedBytes = seedText.getBytes(StandardCharsets.UTF_8);
        return generateWallet(seedBytes);
    }
    
    public static WalletData generateWalletFromFile(byte[] fileBytes) throws Exception {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        return generateWallet(fileBytes);
    }
    
    private static WalletData generateWallet(byte[] seedBytes) throws Exception {
        String privateKey = BitcoinUtils.generatePrivateKey(seedBytes);
        String wif = BitcoinUtils.privateKeyToWIF(privateKey);
        String publicKey = BitcoinUtils.privateKeyToPublicKey(privateKey);
        String address = BitcoinUtils.publicKeyToAddress(publicKey);
        
        return new WalletData(privateKey, wif, publicKey, address);
    }
}