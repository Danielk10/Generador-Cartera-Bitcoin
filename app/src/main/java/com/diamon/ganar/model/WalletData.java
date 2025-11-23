package com.diamon.ganar.model;

/**
 * Modelo inmutable que representa los datos de una cartera Bitcoin.
 * Contiene clave privada, WIF, clave pública y dirección.
 * 
 * @author Bitcoin Wallet Generator
 * @version 1.0
 */
public class WalletData {

    /** Clave privada en formato hexadecimal (64 caracteres) */
    public final String privateKeyHex;

    /** Wallet Import Format - formato estándar para importar claves */
    public final String wif;

    /** Clave pública en formato hexadecimal (130 caracteres, no comprimida) */
    public final String publicKeyHex;

    /** Dirección Bitcoin en formato Base58Check (comienza con '1') */
    public final String address;

    /**
     * Constructor para crear datos de cartera inmutables.
     * 
     * @param privateKeyHex Clave privada en hex
     * @param wif           Wallet Import Format
     * @param publicKeyHex  Clave pública en hex
     * @param address       Dirección Bitcoin
     */
    public WalletData(String privateKeyHex, String wif, String publicKeyHex, String address) {
        this.privateKeyHex = privateKeyHex;
        this.wif = wif;
        this.publicKeyHex = publicKeyHex;
        this.address = address;
    }

    /**
     * Verifica si todos los campos están presentes.
     * 
     * @return true si la cartera es válida
     */
    public boolean isValid() {
        return privateKeyHex != null && !privateKeyHex.isEmpty()
                && wif != null && !wif.isEmpty()
                && publicKeyHex != null && !publicKeyHex.isEmpty()
                && address != null && !address.isEmpty();
    }

    @Override
    public String toString() {
        return "WalletData{" +
                "address='" + address + '\'' +
                ", wif='" + wif.substring(0, 10) + "...'" +
                ", privateKeyHex='" + privateKeyHex.substring(0, 16) + "...'" +
                '}';
    }
}
