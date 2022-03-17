package com.innda.otcdemo.service;

import com.github.pagehelper.PageInfo;
import com.innda.otcdemo.dao.model.OtcOrder;
import com.innda.otcdemo.indto.*;
import com.innda.otcdemo.outdto.OtcOrderDetailOutDto;

import java.util.List;

/**
 * @author qianyu
 * @title   otc订单服务
 * @Package com.innda.otcdemo.service
 * @date 2022/3/14 01:02
 */
public interface OtcOrderService {

    /**
     * 获取订单编号
     * @return
     */
    Long getOrderNo();

    /**
     * 下单
     * @param ortoOrderInDto-订单入参
     * @return  订单号
     */
    Long placeOrder(OtcOrderInDto ortoOrderInDto);

    /**
     * 放行订单
     * @param releaseOrderInDto-放行入参
     */
    void releaseOrder(ReleaseOrderInDto releaseOrderInDto);

    /**
     * 取消订单
     * @param cancelOrderInDto
     */
    void cancelOrder(CancelOrderInDto cancelOrderInDto);

    /**
     * 申诉订单
     * @param complaintOrderInDto
     */
    void complaintOrder(ComplaintOrderInDto complaintOrderInDto);


    /**
     * 获取订单列表
     *
     * @return 订单明细
     */
    PageInfo<OtcOrder> getOtcOrderList(OrderSearchInDto orderSearchInDto);

    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    OtcOrderDetailOutDto getOtcOrderDetail(Long orderId);

    /**
     * 根据订单状态查询订单
     *
     * @param status
     * @return List<OtcOrder>
     */
    List<OtcOrder> getOrderByStatus(Byte status);


    /**
     * 转账后确认已支付
     * @param orderId
     */
    void confirmTransferces(Integer orderId);

    /**
     * 向承兑商发送用户出售短信
     * @param phone
     */
    void sendSellSms(String phone);

    ///**
    // * 订单批量更新
    // *
    // * @param otcOrderList
    // * @return
    // */
    //int updateBatchOrders(List<OtcOrder> otcOrderList);

}
