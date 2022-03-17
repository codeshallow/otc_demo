package com.innda.otcdemo.indto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author qianyu
 * @title   重置密码
 * @Package com.innda.otcdemo.indto
 * @date 2022/3/11 13:51
 */
@Data
public class ResetPwdInDto {

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String code;

}
