package com.innda.otcdemo.indto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author qianyu
 * @title   放行订单
 * @Package com.innda.otcdemo.indto
 * @date 2022/3/15 22:28
 */
@Data
public class ReleaseOrderInDto {

    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Integer orderId;

    /**
     * 支付密码
     */
    @NotBlank(message = "支付密码不能为空")
    private String payPwd;
}
