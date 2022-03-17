package com.innda.otcdemo.service;

import com.innda.otcdemo.indto.SmsSendInDto;

/**
 * @author qianyu
 * @title   发送短信
 * @Package com.innda.otcdemo.service
 * @date 2022/3/11 14:18
 */
public interface SmsService {

    /**
     * 发送短信
     * @param smsSendInDto
     */
    void sendSms(SmsSendInDto smsSendInDto);
}
