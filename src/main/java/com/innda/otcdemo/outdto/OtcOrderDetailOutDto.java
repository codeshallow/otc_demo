package com.innda.otcdemo.outdto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innda.otcdemo.common.serializer.DateToLongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单详情
 */
@Data
public class OtcOrderDetailOutDto {

    /**
     * 订单主键
     */
    private Integer id;

    /**
     * 订单号码
     */
    private Long orderNo;


    /**
     * 倒计时 30分钟
     */
    private Long countdown;
    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 交易总额
     */
    private BigDecimal tradeAmount;

    /**
     * token总额
     */
    private BigDecimal tokenAmount;
    /**
     * 订单时间
     */
    @JsonSerialize(using = DateToLongSerializer.class)
    private Date orderAt;

    /**
     * 类型 1买入 2卖出
     */
    private Byte type;

    /**
     * 状态 1:待支付 2:已支付 3:已放行 4:已取消 5:申述 6:已处理
     */
    private Byte state;

    /**
     * 支付使用付款方式
     */
    private PaymentTypeOutDto paymentType;

    /**
     * 支付列表
     */
    private List<PaymentTypeOutDto> paymentTypeList;

    /**
     * 电话号码
     */
    private String phoneNum;
}
