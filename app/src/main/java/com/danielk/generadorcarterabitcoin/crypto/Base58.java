package com.danielk.generadorcarterabitcoin.crypto;

import java.math.BigInteger;
import java.util.Arrays;

public class Base58 {
    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    private static final BigInteger BASE = BigInteger.valueOf(58);
    
    public static String encode(byte[] input) {
        if (input.length == 0) {
            return "";
        }
        
        int leadingZeros = 0;
        while (leadingZeros < input.length && input[leadingZeros] == 0) {
            leadingZeros++;
        }
        
        byte[] temp = Arrays.copyOf(input, input.length);
        BigInteger num = new BigInteger(1, temp);
        
        StringBuilder encoded = new StringBuilder();
        while (num.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = num.divideAndRemainder(BASE);
            num = divmod[0];
            int remainder = divmod[1].intValue();
            encoded.insert(0, ALPHABET.charAt(remainder));
        }
        
        for (int i = 0; i < leadingZeros; i++) {
            encoded.insert(0, ALPHABET.charAt(0));
        }
        
        return encoded.toString();
    }
    
    public static byte[] decode(String input) {
        if (input.isEmpty()) {
            return new byte[0];
        }
        
        int leadingOnes = 0;
        while (leadingOnes < input.length() && input.charAt(leadingOnes) == ALPHABET.charAt(0)) {
            leadingOnes++;
        }
        
        BigInteger num = BigInteger.ZERO;
        for (int i = leadingOnes; i < input.length(); i++) {
            char c = input.charAt(i);
            int digit = ALPHABET.indexOf(c);
            if (digit == -1) {
                throw new IllegalArgumentException("Invalid Base58 character: " + c);
            }
            num = num.multiply(BASE).add(BigInteger.valueOf(digit));
        }
        
        byte[] decoded = num.toByteArray();
        boolean stripSignByte = decoded.length > 1 && decoded[0] == 0 && decoded[1] < 0;
        int leadingZeros = leadingOnes;
        int totalLength = decoded.length - (stripSignByte ? 1 : 0) + leadingZeros;
        
        byte[] result = new byte[totalLength];
        int srcPos = stripSignByte ? 1 : 0;
        System.arraycopy(decoded, srcPos, result, leadingZeros, decoded.length - srcPos);
        
        return result;
    }
}