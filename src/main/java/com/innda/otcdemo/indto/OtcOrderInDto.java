package com.innda.otcdemo.indto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author qianyu
 * @title   订单入参
 * @Package com.innda.otcdemo.indto
 * @date 2022/3/14 01:04
 */
@Data
public class OtcOrderInDto {

    /**
     * 广告id
     */
    @NotNull(message = "advertisingId不能为空")
    private Integer advertisingId;

    /**
     * 交易总额
     */
    @DecimalGreaterThan(value = 0,message = "交易总额必须大于0")
    private BigDecimal tradeAmount;

    /**
     * 支付密码
     */
    @NotBlank(message = "支付密码不能为空")
    private String payPwd;

    private Integer uid;
    private Integer advertisingUid;
    private String userName;
    private Integer payTypeId;
    private BigDecimal tokenAmount;
    private BigDecimal price;
    private Integer type;

}
