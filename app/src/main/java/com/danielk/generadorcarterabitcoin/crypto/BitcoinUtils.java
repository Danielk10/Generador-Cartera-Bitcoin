package com.danielk.generadorcarterabitcoin.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Arrays;

public class BitcoinUtils {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    public static String generatePrivateKey(byte[] seed) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] firstHash = digest.digest(seed);
        byte[] secondHash = digest.digest(firstHash);
        return bytesToHex(secondHash);
    }
    
    public static String privateKeyToWIF(String privateKeyHex) throws Exception {
        byte[] privateKeyBytes = hexToBytes(privateKeyHex);
        byte[] extended = new byte[privateKeyBytes.length + 1];
        extended[0] = (byte) 0x80;
        System.arraycopy(privateKeyBytes, 0, extended, 1, privateKeyBytes.length);
        
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash1 = digest.digest(extended);
        byte[] hash2 = digest.digest(hash1);
        byte[] checksum = Arrays.copyOfRange(hash2, 0, 4);
        
        byte[] fullPayload = new byte[extended.length + checksum.length];
        System.arraycopy(extended, 0, fullPayload, 0, extended.length);
        System.arraycopy(checksum, 0, fullPayload, extended.length, checksum.length);
        
        return Base58.encode(fullPayload);
    }
    
    public static String privateKeyToPublicKey(String privateKeyHex) throws Exception {
        byte[] privateKeyBytes = hexToBytes(privateKeyHex);
        BigInteger privateKeyInt = new BigInteger(1, privateKeyBytes);
        
        ECNamedCurveParameterSpec spec = ECNamedCurveTable.getParameterSpec("secp256k1");
        ECPoint point = spec.getG().multiply(privateKeyInt);
        
        byte[] x = point.getXCoord().getEncoded();
        byte[] y = point.getYCoord().getEncoded();
        
        byte[] publicKey = new byte[65];
        publicKey[0] = 0x04;
        System.arraycopy(x, 0, publicKey, 1, 32);
        System.arraycopy(y, 0, publicKey, 33, 32);
        
        return bytesToHex(publicKey);
    }
    
    public static String publicKeyToAddress(String publicKeyHex) throws Exception {
        byte[] publicKeyBytes = hexToBytes(publicKeyHex);
        
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] sha256Hash = sha256.digest(publicKeyBytes);
        
        MessageDigest ripemd160 = MessageDigest.getInstance("RIPEMD160", "BC");
        byte[] hash160 = ripemd160.digest(sha256Hash);
        
        byte[] extended = new byte[hash160.length + 1];
        extended[0] = 0x00;
        System.arraycopy(hash160, 0, extended, 1, hash160.length);
        
        byte[] hash1 = sha256.digest(extended);
        byte[] hash2 = sha256.digest(hash1);
        byte[] checksum = Arrays.copyOfRange(hash2, 0, 4);
        
        byte[] fullPayload = new byte[extended.length + checksum.length];
        System.arraycopy(extended, 0, fullPayload, 0, extended.length);
        System.arraycopy(checksum, 0, fullPayload, extended.length, checksum.length);
        
        return Base58.encode(fullPayload);
    }
    
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hex.append(String.format("%02x", b & 0xFF));
        }
        return hex.toString();
    }
    
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