package com.innda.otcdemo.service;

import com.innda.otcdemo.outdto.AdvertistingOutDto;

import java.util.List;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.service
 * @date 2022/3/7 16:41
 */
public interface AdvertistingService {

    List<AdvertistingOutDto> getAdvertistingList(Byte type);
}
