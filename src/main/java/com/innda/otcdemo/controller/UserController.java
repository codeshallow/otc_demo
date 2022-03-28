package com.innda.otcdemo.controller;

import cn.iinda.validator.constraints.UserLoginToken;
import com.innda.otcdemo.common.model.Result;
import com.innda.otcdemo.config.Common;
import com.innda.otcdemo.indto.ResetPwdInDto;
import com.innda.otcdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.controller
 * @date 2022/3/11 13:45
 */
@RestController
@RequestMapping(Common.API + "/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 重置密码发送短信
     * @return
     */
    @PostMapping("/reset_pwd_send_sms")
    @UserLoginToken
    public Result resetPwdSendSms(){
        userService.resetPwdSendSms();
        return Result.ok();
    }


    /**
     * 重置密码
     * @param resetPwdInDto
     * @return
     */
    @PostMapping("/reset_pwd")
    public Result resetPwd(@Valid @RequestBody ResetPwdInDto resetPwdInDto){
        userService.resetPwd(resetPwdInDto);
        return Result.ok();
    }

}
