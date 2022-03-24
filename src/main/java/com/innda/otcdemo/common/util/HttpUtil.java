package com.innda.otcdemo.common.util;


import cn.iinda.xhttputils.HttpClient;

import java.util.Map;

/**
 * http工具类
 * 只封装了简单得get post请求
 * 可自动支持https请求
 * 如果有特殊要求，可自定义添加
 *
 * @author: alibeibei
 */
public class HttpUtil {
    /**
     * 普通get请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        String res = HttpClient.get(url).execute();
        return res;
    }

    /**
     * get 表单提交
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static String getForm(String url, Map<String, String> paramMap) {
        String res = HttpClient.get(url).param(paramMap).execute();
        return res;
    }

    /**
     * json post请求
     *
     * @param url
     * @param json
     * @return
     */
    public static String post(String url, String json) {
        String res = HttpClient.post(url).json(json).execute();
        return res;
    }

    /**
     * post 表单提交
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static String postForm(String url, Map<String, String> paramMap) {
        String res = HttpClient.post(url).param(paramMap).execute();
        return res;
    }
}
