package com.innda.otcdemo.outdto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.outdto
 * @date 2022/3/7 16:45
 */
@Data
public class AdvertistingOutDto {

    /**
     * 广告id
     */
    private Integer id;

    /**
     * 类型 1 购买 2 卖出
     */
    private Byte type;

    /**
     * 状态 1  正常 2下架
     */
    private Byte state;

    /**
     * token剩余数量
     */
    private BigDecimal remainingAmount;

    /**
     * 发布人昵称
     */
    private String userName;

    /**
     * 发布人头像
     */
    private String headUrl;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 最小交易金额
     */
    private BigDecimal minAmount;

    /**
     * 最大交易金额
     */
    private BigDecimal maxAmount;

    /**
     * 支持微信  1支持  2 不支持
     */
    private Byte enableWeixin;

    /**
     * 支持支付宝 1 支持 2 不支持
     */
    private Byte enableAlipay;
}
