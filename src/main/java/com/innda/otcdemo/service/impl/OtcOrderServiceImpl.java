package com.innda.otcdemo.service.impl;

import com.github.pagehelper.PageInfo;
import com.innda.otcdemo.config.Common;
import com.innda.otcdemo.dao.mapper.AdvertisingMapper;
import com.innda.otcdemo.dao.mapper.OtcOrderMapper;
import com.innda.otcdemo.dao.mapper.PaymentTypeMapper;
import com.innda.otcdemo.dao.mapper.UserGsonMapper;
import com.innda.otcdemo.dao.model.Advertising;
import com.innda.otcdemo.dao.model.OtcOrder;
import com.innda.otcdemo.dao.model.UserGson;
import com.innda.otcdemo.indto.*;
import com.innda.otcdemo.outdto.OtcOrderDetailOutDto;
import com.innda.otcdemo.service.OtcOrderService;
import com.innda.otcdemo.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.service.impl
 * @date 2022/3/15 23:03
 */
@Service
@Slf4j
public class OtcOrderServiceImpl implements OtcOrderService {

    @Resource
    private OtcOrderMapper otcOrderMapper;

    @Resource
    private UserGsonMapper userGsonMapper;

    @Resource
    private AdvertisingMapper advertisingMapper;

    @Autowired
    private PaymentTypeMapper paymentTypeMapper;

    @Autowired
    private SmsService smsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 获取唯一id
     * @return
     */
    @Override
    public Long getOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String newDate = sdf.format(new Date());
        RedisAtomicLong entityIdCounter = new RedisAtomicLong("orderNo", stringRedisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();

        //初始设置过期时间
        if ((null == increment || increment.longValue() == 0)) {
            entityIdCounter.expire(60, TimeUnit.SECONDS);
        }

        int max = 9999;
        if (increment > max){
            throw new RuntimeException("生产订单号失败");
        }

        String str = increment.toString();
        str = addZeroLeft(str, 4);
        return Long.valueOf(newDate + str);
    }

    /**
     * 左补零
     * @param str
     * @param strlength
     * @return
     */
    private static String addZeroLeft(String str, int strlength) {
        int strLen = str.length();
        if (strLen < strlength){
            while (strLen < strlength) {
                StringBuffer sb = new StringBuffer();
                //左补零
                sb.append("0").append(str);
                str = sb.toString();
                strLen = str.length();
            }

        }
        return str;
    }

    /**
     * 下单
     * @param otcOrderInDto-订单入参
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long placeOrder(OtcOrderInDto otcOrderInDto) {

        //获取买家信息 //获取卖家信息
        Integer userId = Common.getUserId();
        UserGson gson = Common.getUserGson();
        //for update
        UserGson userGson = userGsonMapper.findOneOrderByLock(userId);
        //获取广告信息，需要用悲观锁 for updata
        Advertising advertising = advertisingMapper.selectOneByLock(otcOrderInDto.getAdvertisingId());
        UserGson oneUserGson = userGsonMapper.findOneOrderByLock(advertising.getUid());
        if (userId.equals(advertising.getUid())){
            throw new RuntimeException("不能对自己的广告进行下单操作");
        }

        //判断下单金额是否合理
        BigDecimal tradeAmount = otcOrderInDto.getTradeAmount();
        if (tradeAmount.compareTo(advertising.getMinAmount()) < 0){
            throw new RuntimeException("未达到下单最低限额，请调整下单金额后再试");
        }
        if (tradeAmount.compareTo(advertising.getMaxAmount()) > 0) {
            throw new RuntimeException("超过下单最高限额，请调整下单金额后再试");
        }

        //判断商品是否充足
        BigDecimal remainingAmount = advertising.getRemainingAmount();
        BigDecimal price = advertising.getPrice();
        BigDecimal tokenAmount = tradeAmount.divide(price,6,BigDecimal.ROUND_HALF_UP);
        OtcOrder otcOrder = new OtcOrder();

        //广告类型为1 即承兑商买 用户卖
        if (advertising.getType() == 1) {
            //广告商的需求
            if (tokenAmount.compareTo(tradeAmount) > 0){
                throw new RuntimeException("订单过大，超过所需GCNY");
            }

            String payPwd = otcOrderInDto.getPayPwd();
            if (!payPwd.equals(gson.getPassword())){
                throw new RuntimeException("支付密码错误");
            }
            //下单人账户锁定币增加 币总量减少
            if (tokenAmount.compareTo(userGson.getZdtnum()) > 0) {
                throw new RuntimeException("您的GCNY不足");
            }
            userGson.setZdtlocknum(userGson.getZdtlocknum().add(tokenAmount));
            userGson.setZdtlocknum(userGson.getZdtnum().subtract(tokenAmount));
            userGson.setUpdatetime(new Date());
            userGsonMapper.updateByPrimaryKey(userGson);

            //订单类型为卖出
            otcOrder.setType((byte) 2);
            sendSellSms(advertising.getPhone());
        }



        return null;
    }

    @Override
    public void releaseOrder(ReleaseOrderInDto releaseOrderInDto) {

    }

    @Override
    public void cancelOrder(CancelOrderInDto cancelOrderInDto) {

    }

    @Override
    public void complaintOrder(ComplaintOrderInDto complaintOrderInDto) {

    }

    @Override
    public PageInfo<OtcOrder> getOtcOrderList(OrderSearchInDto orderSearchInDto) {
        return null;
    }

    @Override
    public OtcOrderDetailOutDto getOtcOrderDetail(Long orderId) {
        return null;
    }

    @Override
    public List<OtcOrder> getOrderByStatus(Byte status) {
        return null;
    }

    @Override
    public void confirmTransferces(Integer orderId) {

    }

    @Override
    public void sendSellSms(String phone) {


    }

    ///**
    // * 下单
    // *
    // * @param otcOrderInDto-订单入参
    // * @return
    // */
    //@Override
    //public Long placeOrder(OtcOrderInDto otcOrderInDto) {
    //
    //    //参数校验 用户id 发布者广告id 广告id
    //    if (otcOrderInDto.getUid() == null || otcOrderInDto.getAdvertisingUid() == null || otcOrderInDto.getAdvertisingId() == null || otcOrderInDto.getPrice() == null || otcOrderInDto.getTokenAmount() == null || otcOrderInDto.getTradeAmount() == null || otcOrderInDto.getType() == null || StringUtils.isEmpty(otcOrderInDto.getUserName())) {
    //        throw new BusinessException("必传参数不能为空");
    //    }
    //    //获取买家信息 获取卖家信息
    //    //UserGson userGson = userGsonMapper.selectByPrimaryKey(otcOrderInDto.getUid());
    //
    //    //获取广告消息，需要用悲观锁 for update
    //    Advertising advertising = advertisingMapper.selectOneByLock(otcOrderInDto.getAdvertisingId());
    //
    //    //判断商品是否充足
    //    BigDecimal remainingAmount = advertising.getRemainingAmount();
    //    if (remainingAmount.compareTo(otcOrderInDto.getTokenAmount()) >= 0) {
    //        Advertising record = new Advertising();
    //        record.setId(otcOrderInDto.getAdvertisingId());
    //        record.setRemainingAmount(remainingAmount.multiply(otcOrderInDto.getTokenAmount()));
    //        advertisingMapper.updateByPrimaryKey(record);
    //    }else {
    //        throw new RuntimeException("库存不足");
    //    }
    //
    //    //组装订单
    //    OtcOrder otcOrder = new OtcOrder();
    //    long orderId = System.nanoTime();
    //    BeanUtils.copyProperties(otcOrderInDto,otcOrder);
    //    otcOrder.setOrderNo(orderId);
    //    otcOrder.setState((byte) OrderStatus.UN_PAY.getStatus().intValue());
    //    otcOrder.setOrderAt(new Date());
    //    otcOrderMapper.insert(otcOrder);
    //
    //    //将订单放入延迟队列，30分钟过期 TODO
    //
    //    return orderId;
    //}
    //
    ///**
    // * 放行订单
    // * @param releaseOrderInDto-放行入参
    // */
    //@Override
    //public void releaseOrder(ReleaseOrderInDto releaseOrderInDto) {
    //    //参数校验
    //    if (releaseOrderInDto.getOrderId() == null || StringUtils.isEmpty(releaseOrderInDto.getPayPwd())) {
    //        throw new RuntimeException("必传参数不能为空");
    //    }
    //
    //    //查询订单信息
    //    OtcOrder queryOrder = otcOrderMapper.selectByPrimaryKey(releaseOrderInDto.getOrderId());
    //    UserGson userGson = userGsonMapper.selectByPrimaryKey(queryOrder.getUid());
    //    String pwd = Md5Util.generate(releaseOrderInDto.getPayPwd());
    //    if (!userGson.getPaypassword().equals(pwd)) {
    //        throw new RuntimeException("支付密码错误");
    //    }
    //
    //    //判断订单是否过期
    //    if (OrderStatus.CANCEL.getStatus() == (int)queryOrder.getState()) {
    //        throw new RuntimeException("订单已过期");
    //    }
    //
    //    OtcOrder otcOrder = new OtcOrder();
    //    otcOrder.setOrderNo((long)releaseOrderInDto.getOrderId());
    //    otcOrder.setState((byte) OrderStatus.RELEASE.getStatus().intValue());
    //    otcOrder.setReleaseAt(new Date());
    //    otcOrderMapper.updateByPrimaryKey(otcOrder);
    //
    //}
    //
    ///**
    // * 取消订单
    // * @param cancelOrderInDto
    // */
    //@Override
    //public void cancelOrder(CancelOrderInDto cancelOrderInDto) {
    //    Integer orderId = cancelOrderInDto.getOrderId();
    //    if (orderId == null || orderId == 0) {
    //        throw new RuntimeException("必传参数不能为空");
    //    }
    //
    //    OtcOrder otcOrder = new OtcOrder();
    //    otcOrder.setOrderNo((long)orderId);
    //    otcOrder.setState((byte) OrderStatus.CANCEL.getStatus().intValue());
    //    otcOrder.setReleaseAt(new Date());
    //    otcOrderMapper.updateByPrimaryKey(otcOrder);
    //}
    //
    ///**
    // * 申诉订单
    // * @param complaintOrderInDto
    // */
    //@Override
    //public void complaintOrder(ComplaintOrderInDto complaintOrderInDto) {
    //    if (complaintOrderInDto.getOrderId() == null || complaintOrderInDto.getOrderId() == 0) {
    //        throw new RuntimeException("必传参数不能为空");
    //    }
    //
    //    OtcOrder queryOrder = otcOrderMapper.selectByPrimaryKey(complaintOrderInDto.getOrderId());
    //    if (OrderStatus.CANCEL.getStatus() == (int)queryOrder.getState()){
    //        throw new RuntimeException("订单已过期");
    //    }
    //
    //    OtcOrder otcOrder = new OtcOrder();
    //    otcOrder.setOrderNo((long)complaintOrderInDto.getOrderId());
    //    otcOrder.setState((byte) OrderStatus.COMPLAIN.getStatus().intValue());
    //    otcOrder.setOrderAt(new Date());
    //    otcOrderMapper.updateByPrimaryKey(otcOrder);
    //
    //}
    //
    ///**
    // * 获取订单列表
    // * @param queryOrderInDto
    // * @return
    // */
    //@Override
    //public PageInfo<OtcOrder> getOtcOrderList(OrderSearchInDto queryOrderInDto) {
    //    if (queryOrderInDto.getUid() == null){
    //        throw new RuntimeException("必传参数不能为空");
    //    }
    //
    //    PageHelper.startPage(queryOrderInDto.getPageNum(), queryOrderInDto.getPageSize());
    //    List<OtcOrder> otcOrderList = otcOrderMapper.queryOrdersByCondition(queryOrderInDto);
    //    PageInfo<OtcOrder> pageInfo = new PageInfo<OtcOrder>(otcOrderList);
    //    List<OtcOrderOutDto> otcOrderOutDtoList = null;
    //    if (!CollectionUtils.isEmpty(otcOrderList)){
    //        otcOrderOutDtoList = new ArrayList<>(otcOrderOutDtoList.size());
    //        OtcOrderOutDto otcOrderOutDto = null;
    //        for (OtcOrder otcOrder : otcOrderList) {
    //            otcOrderOutDto = new OtcOrderOutDto();
    //            BeanUtils.copyProperties(otcOrder, otcOrderOutDto);
    //            otcOrderOutDtoList.add(otcOrderOutDto);
    //        }
    //    }
    //
    //    return pageInfo;
    //}
    //
    ///**
    // * 获取订单详情
    // * @param orderId
    // * @return
    // */
    //@Override
    //public OtcOrderDetailOutDto getOtcOrderDetail(Integer orderId) {
    //    if (orderId == null || orderId == 0) {
    //        return null;
    //    }
    //    OtcOrder otcOrder = otcOrderMapper.selectByPrimaryKey(orderId);
    //    OtcOrderDetailOutDto otcOrderDetailOutDto = null;
    //    if (orderId != null){
    //        otcOrderDetailOutDto = new OtcOrderDetailOutDto();
    //        BeanUtils.copyProperties(otcOrder, otcOrderDetailOutDto);
    //    }
    //    return otcOrderDetailOutDto;
    //}
    //
    //@Override
    //public List<OtcOrder> getOrderByStatus(Byte status) {
    //    return otcOrderMapper.getOrderByStatus(status);
    //}
    //
    //@Override
    //public int updateBatchOrders(List<OtcOrder> otcOrderList) {
    //    return otcOrderMapper.updateBatchOrders(otcOrderList);
    //}
}
