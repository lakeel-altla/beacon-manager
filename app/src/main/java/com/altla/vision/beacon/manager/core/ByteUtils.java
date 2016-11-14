package com.altla.vision.beacon.manager.core;

import android.util.Base64;

public final class ByteUtils {

    private ByteUtils() {
    }

    public static byte[] toBase64Decoded(String s) {
        return Base64.decode(s, Base64.DEFAULT);
    }

    public static byte[] concat(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        byte[] result = new byte[length];
        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }
}
