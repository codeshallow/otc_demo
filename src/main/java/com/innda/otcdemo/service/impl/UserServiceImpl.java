package com.innda.otcdemo.service.impl;

import com.innda.otcdemo.common.enums.SmsTemplateEnum;
import com.innda.otcdemo.common.exception.BusinessException;
import com.innda.otcdemo.common.util.CommonUtil;
import com.innda.otcdemo.common.util.Md5Util;
import com.innda.otcdemo.common.util.RandomUtil;
import com.innda.otcdemo.config.Common;
import com.innda.otcdemo.dao.mapper.UserGsonMapper;
import com.innda.otcdemo.dao.model.UserGson;
import com.innda.otcdemo.indto.ResetPwdInDto;
import com.innda.otcdemo.indto.SmsSendInDto;
import com.innda.otcdemo.service.SmsService;
import com.innda.otcdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.service.impl
 * @date 2022/3/11 14:17
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SmsService smsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserGsonMapper userGsonMapper;

    @Override
    public void resetPwdSendSms() {

        Map<String, String> dataMap = new HashMap<>(1);
        String data = RandomUtil.createData(6);
        dataMap.put("code",data);
        SmsSendInDto smsSendInDto = new SmsSendInDto();
        UserGson userGson = Common.getUserGson();
        String phone = userGson.getUserphone();
        smsSendInDto.setPhone(phone);
        smsSendInDto.setType(SmsTemplateEnum.RESET_PWD.getType());
        smsSendInDto.setData(dataMap);
        String expireKey = CommonUtil.getSmsExpireByUserId(userGson.getId(), SmsTemplateEnum.RESET_PWD);
        if (stringRedisTemplate.hasKey(expireKey)) {
            throw new BusinessException("请在60秒后再试");
        }

        stringRedisTemplate.opsForValue().set(expireKey,data,1, TimeUnit.MINUTES);
        String key = CommonUtil.getSmsCacheByUserId(userGson.getId(), SmsTemplateEnum.RESET_PWD);
        stringRedisTemplate.opsForValue().set(key,data,10,TimeUnit.MINUTES);
        smsService.sendSms(smsSendInDto);
    }

    @Override
    public void resetPwd(ResetPwdInDto resetPwdInDto) {
        UserGson userGson = Common.getUserGson();
        String key = CommonUtil.getSmsCacheByUserId(userGson.getId(), SmsTemplateEnum.RESET_PWD);
        String vCode = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(vCode)) {
            throw new RuntimeException("验证码错误");
        }
        if (!resetPwdInDto.equals(vCode)){
            throw new RuntimeException("验证码错误");
        }

        //MD5加盐
        String pwd = Md5Util.generate(resetPwdInDto.getPassword());
        userGsonMapper.updatePayPassword(userGson.getId(),pwd);


    }

    @Override
    public void checkPayPwd(String pwd, Integer uid) {

    }
}
