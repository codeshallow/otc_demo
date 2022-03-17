package com.innda.otcdemo.outdto;

import lombok.Data;

/**
 * @author qianyu
 * @title   支付方式dto
 * @Package com.innda.otcdemo.outdto
 * @date 2022/3/13 00:22
 */
@Data
public class PaymentTypeOutDto {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 账号
     */
    private String account;

    /**
     * 二维码
     */
    private String qrCodeUrl;

    /**
     * 类型 1 微信 2 支付宝
     */
    private Byte type;
}
