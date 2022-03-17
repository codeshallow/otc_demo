package com.innda.otcdemo.service;

import com.innda.otcdemo.indto.ResetPwdInDto;

/**
 * @author qianyu
 * @title   用户服务
 * @Package com.innda.otcdemo.service
 * @date 2022/3/11 13:47
 */
public interface UserService {

    /**
     * 重置密码发送短信
     */
    void resetPwdSendSms();

    /**
     * 重置密码
     * @param resetPwdInDto
     */
    void resetPwd(ResetPwdInDto resetPwdInDto);

    /**
     * 验证密码
     * @param pwd
     * @param uid
     */
    void checkPayPwd(String pwd,Integer uid);
}
