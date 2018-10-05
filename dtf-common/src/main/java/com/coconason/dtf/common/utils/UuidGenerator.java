package com.coconason.dtf.common.utils;

import java.util.UUID;

/**
 * @Author: Jason
 * @date: 2018/8/10-10:58
 */
public final class UuidGenerator {

    public static String generateUuid() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid;
    }

    public static long generateLongId(){
        return System.currentTimeMillis();
    }

}
