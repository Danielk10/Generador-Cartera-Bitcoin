package com.danielk.generadorcarterabitcoin.model;

public class WalletData {
    private final String privateKey;
    private final String wif;
    private final String publicKey;
    private final String address;
    
    public WalletData(String privateKey, String wif, String publicKey, String address) {
        this.privateKey = privateKey;
        this.wif = wif;
        this.publicKey = publicKey;
        this.address = address;
    }
    
    public String getPrivateKey() {
        return privateKey;
    }
    
    public String getWif() {
        return wif;
    }
    
    public String getPublicKey() {
        return publicKey;
    }
    
    public String getAddress() {
        return address;
    }
}