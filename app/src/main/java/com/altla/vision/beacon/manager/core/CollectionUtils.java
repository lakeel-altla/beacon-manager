package com.altla.vision.beacon.manager.core;

import java.util.List;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }
}
