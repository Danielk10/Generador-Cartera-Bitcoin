package com.diamon.ganar.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Security;

/**
 * Utilidades criptográficas para generación de carteras Bitcoin.
 * Implementa SHA-256, RIPEMD-160 y ECDSA con curva secp256k1.
 * 
 * @author Bitcoin Wallet Generator
 * @version 1.0
 */
public class CryptoUtils {

    // Inicializar Bouncy Castle provider para RIPEMD-160
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Genera una clave privada mediante doble hash SHA-256 de la semilla.
     * 
     * Proceso: PrivateKey = SHA256(SHA256(seed))
     * 
     * @param seed Bytes de la semilla (texto o archivo)
     * @return Clave privada de 32 bytes
     * @throws Exception Si SHA-256 no está disponible
     */
    public static byte[] generatePrivateKey(byte[] seed) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        // Primera pasada SHA-256
        byte[] firstHash = sha256.digest(seed);

        // Segunda pasada SHA-256
        byte[] privateKey = sha256.digest(firstHash);

        return privateKey;
    }

    /**
     * Deriva la clave pública desde la clave privada usando ECDSA secp256k1.
     * 
     * Proceso:
     * 1. Interpreta privateKey como número BigInteger
     * 2. Multiplica punto generador G por privateKey
     * 3. Retorna punto público en formato no comprimido (65 bytes)
     * 
     * @param privateKey Clave privada de 32 bytes
     * @return Clave pública no comprimida (65 bytes: 0x04 + X + Y)
     */
    public static byte[] derivePublicKey(byte[] privateKey) {
        // Obtener parámetros de la curva secp256k1
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256k1");

        // Convertir clave privada a BigInteger
        BigInteger d = new BigInteger(1, privateKey);

        // Multiplicar punto generador G por d para obtener punto público Q
        ECPoint q = spec.getG().multiply(d).normalize();

        // Retornar en formato no comprimido (0x04 + X + Y)
        return q.getEncoded(false);
    }

    /**
     * Genera dirección Bitcoin desde la clave pública.
     * 
     * Proceso:
     * 1. SHA256(publicKey)
     * 2. RIPEMD160(hash anterior)
     * 3. Agregar version byte 0x00 (mainnet)
     * 4. Base58Check encode
     * 
     * @param publicKey Clave pública (65 bytes no comprimida)
     * @return Dirección Bitcoin en formato Base58Check
     * @throws Exception Si algún algoritmo no está disponible
     */
    public static String generateAddress(byte[] publicKey) throws Exception {
        // SHA-256 de la clave pública
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] sha256Hash = sha256.digest(publicKey);

        // RIPEMD-160 del hash SHA-256 (usando Bouncy Castle)
        MessageDigest ripemd160 = MessageDigest.getInstance("RIPEMD160", "BC");
        byte[] hash160 = ripemd160.digest(sha256Hash);

        // Agregar version byte 0x00 para mainnet
        byte[] payload = new byte[1 + hash160.length];
        payload[0] = (byte) 0x00;
        System.arraycopy(hash160, 0, payload, 1, hash160.length);

        // Codificar en Base58Check
        return Base58Utils.encodeBase58Check(payload);
    }

    /**
     * Genera WIF (Wallet Import Format) desde la clave privada.
     * 
     * Proceso:
     * 1. Agregar version byte 0x80 (mainnet)
     * 2. Base58Check encode
     * 
     * @param privateKey Clave privada de 32 bytes
     * @return WIF en formato Base58Check
     * @throws Exception Si SHA-256 no está disponible
     */
    public static String generateWIF(byte[] privateKey) throws Exception {
        // Agregar version byte 0x80 para mainnet WIF
        byte[] payload = new byte[1 + privateKey.length];
        payload[0] = (byte) 0x80;
        System.arraycopy(privateKey, 0, payload, 1, privateKey.length);

        // Codificar en Base58Check
        return Base58Utils.encodeBase58Check(payload);
    }

    /**
     * Convierte bytes a representación hexadecimal.
     * 
     * @param bytes Bytes a convertir
     * @return String hexadecimal (minúsculas)
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Convierte string hexadecimal a bytes.
     * 
     * @param hex String hexadecimal
     * @return Array de bytes
     */
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
