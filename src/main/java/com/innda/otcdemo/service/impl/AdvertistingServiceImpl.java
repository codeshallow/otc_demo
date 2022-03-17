package com.innda.otcdemo.service.impl;

import com.innda.otcdemo.dao.mapper.AdvertisingMapper;
import com.innda.otcdemo.dao.model.Advertising;
import com.innda.otcdemo.outdto.AdvertistingOutDto;
import com.innda.otcdemo.service.AdvertistingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.service
 * @date 2022/3/7 21:18
 */
@Service
@Slf4j
public class AdvertistingServiceImpl implements AdvertistingService {

    @Autowired
    private AdvertisingMapper advertisingMapper;

    @Override
    public List<AdvertistingOutDto> getAdvertistingList(Byte type) {
        List<Advertising> advertisings = advertisingMapper.selectByType(type);
        List<AdvertistingOutDto> outDtoList = new ArrayList<>();
        if (advertisings != null) {
            for (Advertising advertising : advertisings) {
                AdvertistingOutDto outDto = new AdvertistingOutDto();
                outDto.setId(advertising.getId());
                outDto.setType(advertising.getType());
                outDto.setState(advertising.getState());
                outDto.setRemainingAmount(advertising.getRemainingAmount());
                outDto.setUserName(advertising.getUserName());
                outDto.setHeadUrl(advertising.getHeadUrl());
                outDto.setPrice(advertising.getPrice());
                outDto.setMinAmount(advertising.getMinAmount());
                outDto.setMaxAmount(advertising.getMaxAmount());
                outDto.setEnableWeixin(advertising.getEnableWeixin());
                outDto.setEnableAlipay(advertising.getEnableAlipay());
                outDtoList.add(outDto);
            }
        }
        return outDtoList;
    }
}
