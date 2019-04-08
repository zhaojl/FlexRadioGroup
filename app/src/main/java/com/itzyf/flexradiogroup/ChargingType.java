package com.itzyf.flexradiogroup;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChargingType {
    public static final String ALL = "全部显示";
    public static final String FAST = "快充";
    public static final String SLOW = "慢充";

    private static final String[] AllTypes = {
            ALL,
            FAST,
            SLOW,
    };

    public static String randomType() {
        Random random = new Random();
        return AllTypes[random.nextInt(AllTypes.length)];
    }

    public static List<String> getAllChargeType() {
        return Arrays.asList(AllTypes);
    }
}
