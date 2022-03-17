package com.innda.otcdemo.controller;

import com.innda.otcdemo.common.model.Result;
import com.innda.otcdemo.config.Common;
import com.innda.otcdemo.indto.AdvertisingTypeInDto;
import com.innda.otcdemo.outdto.AdvertistingOutDto;
import com.innda.otcdemo.service.AdvertistingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.controller
 * @date 2022/3/7 16:33
 */
@RestController
@RequestMapping(Common.API + "/advertising")
public class AdvertistingController {

    @Autowired
    private AdvertistingService advertistingService;

    @GetMapping("list")
    @UserloginToken
    public Result getAdvertisingList(@Valid AdvertisingTypeInDto advertisingTypeInDto) {
        List<AdvertistingOutDto> advertistingList = advertistingService.getAdvertistingList(advertisingTypeInDto.getType());
        return Result.ok(advertistingList);
    }
}
