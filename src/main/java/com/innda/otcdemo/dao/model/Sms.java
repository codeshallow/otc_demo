package com.innda.otcdemo.dao.model;

import lombok.Data;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.dao.model
 * @date 2022/2/23 01:17
 */
@Data
public class Sms {
    private Integer id;

    private String templateCode;

    private String templateType;

    private String templateParam;

    private String platform;

    private Integer num;
}
