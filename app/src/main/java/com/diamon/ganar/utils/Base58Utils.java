package com.diamon.ganar.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Utilidad para codificación y decodificación Base58Check.
 * Implementación compatible con Bitcoin para WIF y direcciones.
 * 
 * @author Bitcoin Wallet Generator
 * @version 1.0
 */
public class Base58Utils {

    /**
     * Alfabeto Base58 usado por Bitcoin (sin 0, O, I, l para evitar confusión)
     */
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final BigInteger BASE58 = BigInteger.valueOf(58);

    /**
     * Codifica bytes a Base58Check con checksum.
     * 
     * Proceso:
     * 1. Calcula checksum: primeros 4 bytes de SHA256(SHA256(payload))
     * 2. Concatena payload + checksum
     * 3. Codifica en Base58
     * 4. Preserva ceros iniciales como '1'
     * 
     * @param payload Bytes a codificar (incluye version byte)
     * @return String en formato Base58Check
     * @throws NoSuchAlgorithmException Si SHA-256 no está disponible
     */
    public static String encodeBase58Check(byte[] payload) throws NoSuchAlgorithmException {
        // Calcular checksum: SHA256(SHA256(payload))
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash1 = sha256.digest(payload);
        byte[] hash2 = sha256.digest(hash1);
        byte[] checksum = Arrays.copyOfRange(hash2, 0, 4);

        // Concatenar payload + checksum
        byte[] resultBytes = new byte[payload.length + 4];
        System.arraycopy(payload, 0, resultBytes, 0, payload.length);
        System.arraycopy(checksum, 0, resultBytes, payload.length, 4);

        // Contar ceros iniciales (se convertirán en '1' en Base58)
        int leadingZeros = 0;
        for (byte b : resultBytes) {
            if (b == 0) {
                leadingZeros++;
            } else {
                break;
            }
        }

        // Convertir a BigInteger y codificar en Base58
        BigInteger num = new BigInteger(1, resultBytes);
        StringBuilder sb = new StringBuilder();

        while (num.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divRem = num.divideAndRemainder(BASE58);
            sb.insert(0, ALPHABET.charAt(divRem[1].intValue()));
            num = divRem[0];
        }

        // Agregar '1' por cada cero inicial
        for (int i = 0; i < leadingZeros; i++) {
            sb.insert(0, '1');
        }

        return sb.toString();
    }

    /**
     * Verifica si una cadena Base58Check es válida.
     * 
     * @param base58 Cadena a verificar
     * @return true si el checksum es válido
     */
    public static boolean isValidBase58Check(String base58) {
        try {
            // Decodificar Base58
            BigInteger num = BigInteger.ZERO;
            for (char c : base58.toCharArray()) {
                int digit = ALPHABET.indexOf(c);
                if (digit == -1) {
                    return false; // Carácter inválido
                }
                num = num.multiply(BASE58).add(BigInteger.valueOf(digit));
            }

            byte[] decoded = num.toByteArray();

            // Mínimo: 1 byte version + 4 bytes checksum
            if (decoded.length < 5) {
                return false;
            }

            // Separar payload y checksum
            byte[] payload = Arrays.copyOfRange(decoded, 0, decoded.length - 4);
            byte[] checksum = Arrays.copyOfRange(decoded, decoded.length - 4, decoded.length);

            // Recalcular checksum
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] hash1 = sha256.digest(payload);
            byte[] hash2 = sha256.digest(hash1);
            byte[] expectedChecksum = Arrays.copyOfRange(hash2, 0, 4);

            // Comparar checksums
            return Arrays.equals(checksum, expectedChecksum);

        } catch (Exception e) {
            return false;
        }
    }
}
