package com.innda.otcdemo.outdto;

import lombok.Data;

/**
 * 订单列表明细
 */
@Data
public class OtcOrderOutDto {
    /**
     * 订单主键
     */
    private Integer id;

    /**
     * 类型 1买入 2卖出
     */
    private Byte type;

    /**
     * 用户
     */
    private String userName;

    /**
     * 状态 1:待支付 2:已支付 3:已放行 4:已取消 5:申述 6:已处理
     */
    private Byte state;

    /**
     * 类型  1 是自己发布得广告 2 不是
     */
    private Byte ownPubAdvertising;

    /**
     * 订单号
     */
    private Long orderNo;
}
