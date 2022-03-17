package com.innda.otcdemo.dao.model;

import lombok.Data;

import java.util.Date;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.dao.model
 * @date 2022/2/23 01:16
 */
@Data
public class PaymentType {
    private Integer id;

    private Integer uid;

    private String name;

    private String account;

    private String qrCodeUrl;

    private Byte type;

    private Byte state;

    private Date createAt;
}
