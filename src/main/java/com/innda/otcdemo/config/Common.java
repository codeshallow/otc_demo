package com.innda.otcdemo.config;


import com.innda.otcdemo.dao.model.UserGson;

/**
 * @author fugq
 * @version V1.0
 * @date 18-4-10 上午9:55
 */
public class Common {
    public final static String API = "/otc/v1";
    public static ThreadLocal<UserGson> userGson = new ThreadLocal();

    /**
     * 获取userId
     *
     * @return
     */
    public static Integer getUserId() {
        Object o = userGson.get().getId();
        if (o != null) {
            return (Integer) o;
        }
        return null;
    }


    /**
     * 获取userId
     *
     * @return
     */
    public static UserGson getUserGson() {
        Object o = userGson.get();
        if (o == null) {
            return null;
        }
        return (UserGson) o;
    }

    /**
     * 设置userId
     */
    public static void removeUserGson() {
        Common.userGson.remove();
    }
}
