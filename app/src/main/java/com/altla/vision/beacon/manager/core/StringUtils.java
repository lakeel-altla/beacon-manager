package com.altla.vision.beacon.manager.core;

import android.util.Base64;

public final class StringUtils {

    public static final String EMPTY = "";

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    private StringUtils() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String toHex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int c = bytes[i] & 0xFF;
            chars[i * 2] = HEX[c >>> 4];
            chars[i * 2 + 1] = HEX[c & 0x0F];
        }
        return new String(chars).toLowerCase();
    }

    public static String toBase64Encoded(byte[] b) {
        return Base64.encodeToString(b, Base64.DEFAULT).trim();
    }
}
