package com.innda.otcdemo.indto;

import lombok.Data;

import java.util.Map;

/**
 * @author qianyu
 * @title   发送短信模板
 * @Package com.innda.otcdemo.indto
 * @date 2022/3/11 14:19
 */
@Data
public class SmsSendInDto {

    String phone;

    String type;
    Map<String,String> data;
}
