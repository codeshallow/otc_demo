package com.innda.otcdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author qianyu
 * @title   短信配置
 * @Package com.innda.otcdemo.config
 * @date 2022/3/28 22:50
 */
@Component
@ConfigurationProperties(value = "sms.bao")
@Data
public class SmsConfig {
    private String username;
    private String password;
}
