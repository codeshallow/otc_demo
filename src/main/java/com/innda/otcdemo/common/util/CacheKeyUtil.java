package com.innda.otcdemo.common.util;

/**
 * 缓存key约定协议
 *
 * @author: alibeibei
 */
public class CacheKeyUtil {

    /**
     * test key
     *
     * @param code
     * @return
     */
    public static String getTestKeyByCode(String code) {
        return "test_" + code;
    }
}
