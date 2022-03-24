package com.innda.otcdemo.common.util;

import java.util.Base64;

/**
 * base64加密算法
 *
 * @author: alibeibei
 */
public class Base64Util {

    /**
     * 加密
     *
     * @param str 原数据
     * @return
     */
    public static String encode(String str) {
        byte[] encode = Base64.getEncoder().encode(str.getBytes());
        return new String(encode);
    }

    /**
     * 解密
     *
     * @param str 密文
     * @return
     */
    public static String decode(String str) {
        byte[] decode = Base64.getDecoder().decode(str.getBytes());
        return new String(decode);
    }

    public static void main(String[] args) {
        String str = "hello";
        String encode = encode(str);
        System.out.println("encode=" + encode);
        String decode = decode(encode);
        System.out.println("decode=" + decode);

    }
}
