package com.diamon.ganar;

import com.diamon.ganar.utils.Base58Utils;
import com.diamon.ganar.utils.CryptoUtils;

/**
 * Fachada principal para operaciones criptográficas de Bitcoin.
 * Delega a clases de utilidad especializadas.
 * 
 * @author Bitcoin Wallet Generator
 * @version 2.0
 * @deprecated Usar directamente CryptoUtils y Base58Utils para nuevo código
 */
public class BitcoinUtils {

    /**
     * Genera clave privada desde semilla.
     * 
     * @param seedBytes Bytes de la semilla
     * @return Clave privada de 32 bytes
     * @throws Exception Si hay error criptográfico
     */
    public static byte[] generatePrivateKeyFromSeed(byte[] seedBytes) throws Exception {
        return CryptoUtils.generatePrivateKey(seedBytes);
    }

    /**
     * Deriva clave pública desde clave privada.
     * 
     * @param privateKeyBytes Clave privada
     * @return Clave pública no comprimida
     */
    public static byte[] getPublicKey(byte[] privateKeyBytes) {
        return CryptoUtils.derivePublicKey(privateKeyBytes);
    }

    /**
     * Genera dirección Bitcoin desde clave pública.
     * 
     * @param publicKeyBytes Clave pública
     * @return Dirección Bitcoin en Base58Check
     * @throws Exception Si hay error criptográfico
     */
    public static String getAddress(byte[] publicKeyBytes) throws Exception {
        return CryptoUtils.generateAddress(publicKeyBytes);
    }

    /**
     * Genera WIF desde clave privada.
     * 
     * @param privateKeyBytes Clave privada
     * @return WIF en Base58Check
     * @throws Exception Si hay error criptográfico
     */
    public static String getWIF(byte[] privateKeyBytes) throws Exception {
        return CryptoUtils.generateWIF(privateKeyBytes);
    }

    /**
     * Convierte bytes a hexadecimal.
     * 
     * @param bytes Bytes a convertir
     * @return String hexadecimal
     */
    public static String bytesToHex(byte[] bytes) {
        return CryptoUtils.bytesToHex(bytes);
    }

    /**
     * Convierte hexadecimal a bytes.
     * 
     * @param hex String hexadecimal
     * @return Array de bytes
     */
    public static byte[] hexToBytes(String hex) {
        return CryptoUtils.hexToBytes(hex);
    }

    /**
     * Verifica si una cadena Base58Check es válida.
     * 
     * @param base58 Cadena a verificar
     * @return true si es válida
     */
    public static boolean isValidBase58Check(String base58) {
        return Base58Utils.isValidBase58Check(base58);
    }
}