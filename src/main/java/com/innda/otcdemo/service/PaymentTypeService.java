package com.innda.otcdemo.service;

import com.innda.otcdemo.indto.PaymentTypeInDto;
import com.innda.otcdemo.outdto.PaymentTypeOutDto;

import java.util.List;

/**
 * @author qianyu
 * @title   添加支付方式服务
 * @Package com.innda.otcdemo.service
 * @date 2022/3/12 02:23
 */
public interface PaymentTypeService {

    /**
     * 增加支付方式
     * @param paymentTypeInDto
     */
    void add(PaymentTypeInDto paymentTypeInDto);

    /**
     * 更新支付方式
     * @param paymentTypeInDto
     */
    void update(PaymentTypeInDto paymentTypeInDto);

    /**
     * 取消支付方式
     * @param id
     */
    void down(Integer id);

    /**
     * 获取支付方式
     * @param id
     * @return
     */
    PaymentTypeOutDto getPaymentType(Integer id);

    /**
     * 返回支付列表
     * @return
     */
    List<PaymentTypeOutDto> getPaymentTypeList();
}
