package com.innda.otcdemo.indto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.indto
 * @date 2022/3/12 02:24
 */
@Data
public class PaymentTypeInDto {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 姓名
     */
    @NotBlank(message="项目不能为空")
    private String name;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 二维码
     */
    @NotBlank(message = "二维码不能为空")
    private String qrCodeUrl;

    /**
     * 类型 1 微信 2 支付宝
     */
    @In(value = "1,2",message = "type错误")
    private Byte type;

}
