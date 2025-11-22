package com.diamon.ganar; // Unificado

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;

public class BitcoinUtils {

    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final BigInteger BASE58 = BigInteger.valueOf(58);

    static {
        // Importante: Eliminar primero para evitar conflictos y añadir la versión incluida
        Security.removeProvider("BC");
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] generatePrivateKeyFromSeed(byte[] seedBytes) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] firstHash = sha256.digest(seedBytes);
        return sha256.digest(firstHash);
    }

    public static byte[] getPublicKey(byte[] privateKeyBytes) {
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256k1");
        BigInteger d = new BigInteger(1, privateKeyBytes);
        ECPoint q = spec.getG().multiply(d).normalize();
        return q.getEncoded(false); // false = uncompressed
    }

    public static String getWIF(byte[] privateKeyBytes) throws NoSuchAlgorithmException {
        byte[] payload = new byte[1 + privateKeyBytes.length];
        payload[0] = (byte) 0x80;
        System.arraycopy(privateKeyBytes, 0, payload, 1, privateKeyBytes.length);
        return base58CheckEncode(payload);
    }

    public static String getAddress(byte[] publicKeyBytes) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] sha256Hash = sha256.digest(publicKeyBytes);

        // Especificar el proveedor "BC" explícitamente para RIPEMD160
        MessageDigest ripemd160 = MessageDigest.getInstance("RIPEMD160", "BC");
        byte[] hash160 = ripemd160.digest(sha256Hash);

        byte[] payload = new byte[1 + hash160.length];
        payload[0] = (byte) 0x00;
        System.arraycopy(hash160, 0, payload, 1, hash160.length);

        return base58CheckEncode(payload);
    }

    private static String base58CheckEncode(byte[] payload) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash1 = sha256.digest(payload);
        byte[] hash2 = sha256.digest(hash1);
        byte[] checksum = Arrays.copyOfRange(hash2, 0, 4);

        byte[] resultBytes = new byte[payload.length + 4];
        System.arraycopy(payload, 0, resultBytes, 0, payload.length);
        System.arraycopy(checksum, 0, resultBytes, payload.length, 4);

        int leadingZeros = 0;
        for (byte b : resultBytes) {
            if (b == 0) leadingZeros++;
            else break;
        }

        BigInteger num = new BigInteger(1, resultBytes);
        StringBuilder sb = new StringBuilder();

        while (num.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divRem = num.divideAndRemainder(BASE58);
            sb.insert(0, ALPHABET.charAt(divRem[1].intValue()));
            num = divRem[0];
        }

        for (int i = 0; i < leadingZeros; i++) {
            sb.insert(0, '1');
        }

        return sb.toString();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}