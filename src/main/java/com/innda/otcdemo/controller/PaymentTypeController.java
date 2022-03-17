package com.innda.otcdemo.controller;

import com.innda.otcdemo.common.model.Result;
import com.innda.otcdemo.config.Common;
import com.innda.otcdemo.indto.PaymentTypeInDto;
import com.innda.otcdemo.outdto.PaymentTypeOutDto;
import com.innda.otcdemo.service.PaymentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author qianyu
 * @title   支付方式服务
 * @Package com.innda.otcdemo.controller
 * @date 2022/3/12 02:21
 */

@RestController
@RequestMapping(Common.API + "/payment_type")
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService paymentTypeService;

    /**
     * 增加支付方式
     * @param paymentTypeInDto
     * @return
     */
    @PostMapping("/add")
    @UserLoginToken
    public Result add(@Valid @RequestBody PaymentTypeInDto paymentTypeInDto){
        paymentTypeService.add(paymentTypeInDto);
        return Result.ok();
    }

    /**
     * 更新支付方式
     * @param paymentTypeInDto
     * @return
     */
    @PostMapping("/update")
    @UserLoginToken
    public Result update(@Valid @RequestBody PaymentTypeInDto paymentTypeInDto) {
        paymentTypeService.update(paymentTypeInDto);
        return Result.ok();
    }

    /**
     * 取消支付方式
     * @param paymentTypeInDto
     * @return
     */
    @PostMapping("/down")
    @UserLoginToken
    public Result down(@RequestBody paymentTypeInDto paymentTypeInDto){
        paymentTypeService.down(paymentTypeInDto);
        return Result.ok();
    }

    /**
     * 获取支付方式
     * @param id
     * @return
     */
    @GetMapping("/info")
    @UserLoginToken
    public Result getPaymentType(@RequestParam(value = "id",defaultValue = "1") Integer id){
        PaymentTypeOutDto paymentType = paymentTypeService.getPaymentType(id);
        return Result.ok(paymentType);

    }

    /**
     * 获取支付列表
     * @return
     */
    @GetMapping("/list")
    @UserLoginToken
    public Result getPaymentTypeList(){
        List<PaymentTypeOutDto> paymentTypeList = paymentTypeService.getPaymentTypeList();
        return Result.ok(paymentTypeList);
    }





}
