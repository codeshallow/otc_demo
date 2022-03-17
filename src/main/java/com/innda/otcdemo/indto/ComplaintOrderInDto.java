package com.innda.otcdemo.indto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.indto
 * @date 2022/3/15 22:33
 */
@Data
public class ComplaintOrderInDto {

    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Integer orderId;
}
