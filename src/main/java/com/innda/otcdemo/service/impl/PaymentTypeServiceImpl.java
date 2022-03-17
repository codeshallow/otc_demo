package com.innda.otcdemo.service.impl;

import com.innda.otcdemo.config.Common;
import com.innda.otcdemo.dao.mapper.PaymentTypeMapper;
import com.innda.otcdemo.dao.model.PaymentType;
import com.innda.otcdemo.dao.model.UserGson;
import com.innda.otcdemo.indto.PaymentTypeInDto;
import com.innda.otcdemo.outdto.PaymentTypeOutDto;
import com.innda.otcdemo.service.PaymentTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author qianyu
 * @title   支付方式
 * @Package com.innda.otcdemo.service
 * @date 2022/3/13 00:27
 */
@Service
@Slf4j
public class PaymentTypeServiceImpl implements PaymentTypeService {

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    private final static Byte PAYMENT_NORMAL_STATE = 1;

    private final static Byte PAYMENT_EXPIRE_STATE = 2;

    @Override
    public void add(PaymentTypeInDto paymentTypeInDto) {
        PaymentType paymentType = inDto2PaymentType(paymentTypeInDto);
        paymentTypeMapper.insert(paymentType);
    }

    private PaymentType inDto2PaymentType(PaymentTypeInDto inDto) {
        PaymentType paymentType = new PaymentType();
        UserGson userGson = Common.getUserGson();
        Integer uid = userGson.getId();

        paymentType.setUid(uid);
        paymentType.setName(inDto.getName());
        paymentType.setAccount(inDto.getAccount());
        paymentType.setQrCodeUrl(inDto.getQrCodeUrl());
        paymentType.setType(inDto.getType());
        paymentType.setState(PAYMENT_NORMAL_STATE);
        paymentType.setCreateAt(new Date());

        return paymentType;
    }

    @Override
    public void update(PaymentTypeInDto paymentTypeInDto) {
        PaymentType paymentType = inDto2PaymentType(paymentTypeInDto);
        paymentTypeMapper.updateByPrimaryKey(paymentType);

    }

    @Override
    public void down(Integer id) {
        paymentTypeMapper.deleteByPrimaryKey(id);

    }

    @Override
    public PaymentTypeOutDto getPaymentType(Integer id) {
        PaymentType paymentType = paymentTypeMapper.selectByPrimaryKey(id);
        PaymentTypeOutDto outDto = paymentType2outDto(paymentType);
        return outDto;
    }

    private PaymentTypeOutDto paymentType2outDto(PaymentType paymentType) {
        PaymentTypeOutDto outDto = new PaymentTypeOutDto();
        outDto.setId(paymentType.getId());
        outDto.setType(paymentType.getType());
        outDto.setName(paymentType.getName());
        outDto.setAccount(paymentType.getAccount());
        outDto.setQrCodeUrl(paymentType.getQrCodeUrl());
        return outDto;
    }

    @Override
    public List<PaymentTypeOutDto> getPaymentTypeList() {
        UserGson userGson = Common.getUserGson();
        Integer uid = userGson.getId();
        List<PaymentType> paymentTypes = paymentTypeMapper.selectByUid(uid);
        List<PaymentTypeOutDto> outDtoList = new ArrayList<>();
        if (paymentTypes != null){
            for (PaymentType paymentType : paymentTypes) {
                PaymentTypeOutDto outDto = paymentType2outDto(paymentType);
                outDtoList.add(outDto);
            }
        }
        return outDtoList;
    }
}
