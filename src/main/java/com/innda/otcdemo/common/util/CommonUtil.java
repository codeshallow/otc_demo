package com.innda.otcdemo.common.util;

import com.innda.otcdemo.common.enums.SmsTemplateEnum;

/**
 * 通用常量
 */
public class CommonUtil {

    /**
     * 获取短信发送key
     *
     * @param userId
     * @param smsTemplateEnum
     * @return
     */
    public static String getSmsCacheByUserId(Integer userId, SmsTemplateEnum smsTemplateEnum) {
        return "SmsCache_" + smsTemplateEnum.getType() + "_" + userId;
    }

    /**
     * sms过期缓存
     *
     * @param userId
     * @param smsTemplateEnum
     * @return
     */
    public static String getSmsExpireByUserId(Integer userId, SmsTemplateEnum smsTemplateEnum) {
        return "SmsExpireCache" + smsTemplateEnum.getType() + "_" + userId;
    }
}