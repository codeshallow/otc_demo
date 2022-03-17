package com.innda.otcdemo.dao.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.dao.model
 * @date 2022/2/23 01:15
 */
@Data
public class Advertising {
    private Integer id;

    private Integer uid;

    private Byte type;

    private Byte state;

    private BigDecimal totalAmount;

    private BigDecimal remainingAmount;

    private String userName;

    private String headUrl;

    private String phone;

    private BigDecimal price;

    private Date pubAt;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private Date endAt;

    private Byte enableWeixin;

    private Byte enableAlipay;
}
