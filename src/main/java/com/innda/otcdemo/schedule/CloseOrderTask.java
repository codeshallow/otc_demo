package com.innda.otcdemo.schedule;

import com.innda.otcdemo.common.enums.OrderStatus;
import com.innda.otcdemo.dao.model.OtcOrder;
import com.innda.otcdemo.indto.CancelOrderInDto;
import com.innda.otcdemo.service.OtcOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author qianyu
 * @title   订单30分钟后关闭
 * @Package com.innda.otcdemo.schedule
 * @date 2022/3/21 15:42
 */
@Component
@Slf4j
public class CloseOrderTask {

    @Resource
    private OtcOrderService otcOrderService;

    //或直接指定时间间隔，例如：5秒
    //设置为一分钟定时任务
    @Scheduled(fixedRate = 60000)
    @Async
    public void closeOrder() {

        log.info("zdtoc|configureTasks -- 订单30分钟自动关闭定时任务启动");
        //查询所有待付款的订单 --待优化，如果订单量比较大，需要分批处理
        List<OtcOrder> orderList = otcOrderService.getOrderByStatus(OrderStatus.UN_PAY.getStatus());
        LocalDateTime start = LocalDateTime.now();
        if (!CollectionUtils.isEmpty(orderList)){
            List<OtcOrder> closeOrderList = new LinkedList<>();
            for (OtcOrder otcOrder : orderList) {
                //判断时间是否超过30分钟，后期可抽取一个工具类
                Date orderAtDate = otcOrder.getOrderAt();
                long orderAtDateTime = orderAtDate.getTime();
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - orderAtDateTime > 30 * 60 * 1000){
                    CancelOrderInDto cancelOrderInDto = new CancelOrderInDto();
                    otcOrder.setType((byte) OrderStatus.CANCEL.getStatus().intValue());
                    cancelOrderInDto.setOrderId(otcOrder.getId());
                    otcOrderService.cancelOrder(cancelOrderInDto);

                }
            }
        }


    }
}
