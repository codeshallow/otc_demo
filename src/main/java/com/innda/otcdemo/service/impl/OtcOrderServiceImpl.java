package com.innda.otcdemo.service.impl;

import com.github.pagehelper.PageHelper;
import com.innda.otcdemo.common.enums.OrderStatus;
import com.innda.otcdemo.common.exception.BusinessException;
import com.innda.otcdemo.config.Common;
import com.innda.otcdemo.dao.mapper.AdvertisingMapper;
import com.innda.otcdemo.dao.mapper.OtcOrderMapper;
import com.innda.otcdemo.dao.mapper.PaymentTypeMapper;
import com.innda.otcdemo.dao.mapper.UserGsonMapper;
import com.innda.otcdemo.dao.model.Advertising;
import com.innda.otcdemo.dao.model.OtcOrder;
import com.innda.otcdemo.dao.model.PaymentType;
import com.innda.otcdemo.dao.model.UserGson;
import com.innda.otcdemo.indto.*;
import com.innda.otcdemo.outdto.OtcOrderDetailOutDto;
import com.innda.otcdemo.outdto.OtcOrderOutDto;
import com.innda.otcdemo.outdto.PaymentTypeOutDto;
import com.innda.otcdemo.service.OtcOrderService;
import com.innda.otcdemo.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
     * ????????????id
     *
     * @return
     */
    @Override
    public Long getOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String newDate = sdf.format(new Date());
        RedisAtomicLong entityIdCounter = new RedisAtomicLong("orderNo", stringRedisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();

        //????????????????????????
        if ((null == increment || increment.longValue() == 0)) {
            entityIdCounter.expire(60, TimeUnit.SECONDS);
        }

        int max = 9999;
        if (increment > max) {
            throw new BusinessException("?????????????????????");
        }

        String str = increment.toString();
        str = addZeroLeft(str, 4);
        return Long.valueOf(newDate + str);
    }

    /**
     * ?????????
     *
     * @param str
     * @param strlength
     * @return
     */
    private static String addZeroLeft(String str, int strlength) {
        int strLen = str.length();
        if (strLen < strlength) {
            while (strLen < strlength) {
                StringBuffer sb = new StringBuffer();
                //?????????
                sb.append("0").append(str);
                str = sb.toString();
                strLen = str.length();
            }

        }
        return str;
    }

    /**
     * ??????
     *
     * @param otcOrderInDto-????????????
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long placeOrder(OtcOrderInDto otcOrderInDto) {

        //?????????????????? //??????????????????
        Integer userId = Common.getUserId();
        UserGson gson = Common.getUserGson();
        //for update
        UserGson userGson = userGsonMapper.findOneUserGsonByLock(userId);
        //??????????????????????????????????????? for updata
        Advertising advertising = advertisingMapper.selectOneByLock(otcOrderInDto.getAdvertisingId());
        UserGson oneUserGson = userGsonMapper.findOneUserGsonByLock(advertising.getUid());
        if (userId.equals(advertising.getUid())) {
            throw new BusinessException("??????????????????????????????????????????");
        }

        //??????????????????????????????
        BigDecimal tradeAmount = otcOrderInDto.getTradeAmount();
        if (tradeAmount.compareTo(advertising.getMinAmount()) < 0) {
            throw new BusinessException("????????????????????????????????????????????????????????????");
        }
        if (tradeAmount.compareTo(advertising.getMaxAmount()) > 0) {
            throw new BusinessException("?????????????????????????????????????????????????????????");
        }

        //????????????????????????
        BigDecimal remainingAmount = advertising.getRemainingAmount();
        BigDecimal price = advertising.getPrice();
        BigDecimal tokenAmount = tradeAmount.divide(price, 6, BigDecimal.ROUND_HALF_UP);
        OtcOrder otcOrder = new OtcOrder();

        //???????????????1 ??????????????? ?????????
        if (advertising.getType() == 1) {
            //??????????????????
            if (tokenAmount.compareTo(tradeAmount) > 0) {
                throw new BusinessException("???????????????????????????GCNY");
            }

            String payPwd = otcOrderInDto.getPayPwd();
            if (!payPwd.equals(gson.getPassword())) {
                throw new BusinessException("??????????????????");
            }
            //?????????????????????????????? ???????????????
            if (tokenAmount.compareTo(userGson.getZdtnum()) > 0) {
                throw new BusinessException("??????GCNY??????");
            }
            userGson.setZdtlocknum(userGson.getZdtlocknum().add(tokenAmount));
            userGson.setZdtlocknum(userGson.getZdtnum().subtract(tokenAmount));
            userGson.setUpdatetime(new Date());
            userGsonMapper.updateByPrimaryKey(userGson);

            //?????????????????????
            otcOrder.setType((byte) 2);
            sendSellSms(advertising.getPhone());
        }

        //???????????????2 ??????????????? ?????????
        if (advertising.getType() == 2) {
            //??????????????????????????????
            if (tokenAmount.compareTo(advertising.getRemainingAmount()) > 0) {
                throw new BusinessException("GCNY????????????");
            }
            advertising.setRemainingAmount(advertising.getRemainingAmount().subtract(tokenAmount));
            advertisingMapper.updateByPrimaryKey(advertising);

            //?????????????????????????????????
            oneUserGson.setZdtlocknum(oneUserGson.getZdtlocknum().add(tokenAmount));
            oneUserGson.setUpdatetime(new Date());
            userGsonMapper.updateByPrimaryKey(oneUserGson);
            //?????????????????????
            otcOrder.setType((byte) 1);
        }

        //????????????
        Long orderNo = getOrderNo();
        otcOrder.setUid(userGson.getId());
        otcOrder.setAdvertisingUid(advertising.getUid());
        otcOrder.setUserName(userGson.getNickname());
        otcOrder.setAdvertisingId(otcOrderInDto.getAdvertisingId());
        otcOrder.setOrderNo(orderNo);
        otcOrder.setPrice(price);
        otcOrder.setTradeAmount(tradeAmount);
        otcOrder.setOrderAt(new Date());
        otcOrder.setState(OrderStatus.UN_PAY.getStatus());
        otcOrderMapper.insert(otcOrder);

        return orderNo;

    }

    /**
     * ????????????
     *
     * @param releaseOrderInDto-????????????
     */
    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void releaseOrder(ReleaseOrderInDto releaseOrderInDto) {
        Integer userId = Common.getUserId();

        //??????????????????
        OtcOrder queryOrder = otcOrderMapper.findOneOrderByLock(releaseOrderInDto.getOrderId());
        if (queryOrder.getState().equals(OrderStatus.RELEASE.getStatus())) {
            throw new BusinessException("???????????????,??????????????????");
        }

        BigDecimal tokenAmount = queryOrder.getTokenAmount();
        Byte type = queryOrder.getType();
        if (type == 1) {
            if (!userId.equals(queryOrder.getAdvertisingUid())) {
                throw new BusinessException("??????????????????");
            } else {
                if (queryOrder.getState().equals(OrderStatus.UN_PAY.getStatus())) {
                    throw new BusinessException("??????????????????");

                }

                //for update
                UserGson oneUserGson = userGsonMapper.findOneUserGsonByLock(userId);
                if (!releaseOrderInDto.getPayPwd().equals(oneUserGson.getPassword())) {
                    throw new BusinessException("??????????????????");
                }

                //????????????????????????
                if (OrderStatus.CANCEL.getStatus().equals(queryOrder.getState())) {
                    throw new BusinessException("???????????????");
                }

                queryOrder.setState(OrderStatus.RELEASE.getStatus());
                queryOrder.setOrderAt(new Date());
                otcOrderMapper.updateByPrimaryKey(queryOrder);

                //type = 1 ?????????????????????????????????????????????????????????????????????
                oneUserGson.setZdtlocknum(oneUserGson.getZdtlocknum().subtract(tokenAmount));
                oneUserGson.setUpdatetime(new Date());
                userGsonMapper.updateByPrimaryKey(oneUserGson);
                UserGson oneUserGsonByLock = userGsonMapper.findOneUserGsonByLock(queryOrder.getUid());
                oneUserGsonByLock.setZdtnum(oneUserGsonByLock.getZdtnum().add(tokenAmount));
                oneUserGsonByLock.setUpdatetime(new Date());
                userGsonMapper.updateByPrimaryKey(oneUserGsonByLock);
                SmsSendInDto smsSendInDto = new SmsSendInDto();
                smsSendInDto.setPhone(oneUserGsonByLock.getUserphone());
                smsSendInDto.setType("business");

                HashMap<String, String> dataMap = new HashMap<>(4);
                dataMap.put("orderNo", queryOrder.getOrderNo().toString());
                smsSendInDto.setData(dataMap);
                smsService.sendSms(smsSendInDto);
            }
        } else {
            //for update
            UserGson oneUserGson = userGsonMapper.findOneUserGsonByLock(userId);
            if (!releaseOrderInDto.getPayPwd().equals(oneUserGson.getPassword())) {
                throw new BusinessException("??????????????????");
            }
            if (queryOrder.getState().equals(OrderStatus.UN_PAY.getStatus())) {
                throw new BusinessException("??????????????????");
            }

            //????????????????????????
            if (OrderStatus.CANCEL.getStatus().equals(queryOrder.getState())) {
                throw new BusinessException("???????????????");
            }

            queryOrder.setReleaseAt(new Date());
            queryOrder.setState(OrderStatus.RELEASE.getStatus());

            //type = 2 ????????????????????????????????????????????????????????????????????????
            Advertising advertising = advertisingMapper.selectOneByTypeAndLock(queryOrder.getAdvertisingUid(), (byte) 2);
            advertising.setRemainingAmount(advertising.getRemainingAmount().add(tokenAmount));
            advertisingMapper.updateByPrimaryKey(advertising);
            oneUserGson.setZdtlocknum(oneUserGson.getZdtlocknum().subtract(tokenAmount));
            oneUserGson.setUpdatetime(new Date());
            userGsonMapper.updateByPrimaryKey(oneUserGson);
            SmsSendInDto smsSendInDto = new SmsSendInDto();
            HashMap<String, String> dataMap = new HashMap<>(4);
            smsSendInDto.setPhone(advertising.getPhone());
            smsSendInDto.setType("business");
            dataMap.put("orderNO", queryOrder.getOrderNo().toString());
            smsSendInDto.setData(dataMap);
            smsService.sendSms(smsSendInDto);

            //??????dev??????????????????
        }

    }

    /**
     * ????????????
     *
     * @param cancelOrderInDto
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void cancelOrder(CancelOrderInDto cancelOrderInDto) {
        Integer orderId = cancelOrderInDto.getOrderId();
        OtcOrder otcOrder = otcOrderMapper.findOneOrderByLock(orderId);

        if (otcOrder == null) {
            throw new BusinessException("???????????????");
        }

        if (otcOrder.getState().equals(OrderStatus.CANCEL.getStatus())) {
            throw new BusinessException("????????????????????????????????????");
        }

        UserGson userGson = Common.getUserGson();
        Integer userId;

        if (userGson != null) {
            userId = userGson.getId();
        } else {
            userId = otcOrder.getUid();
        }

        BigDecimal tokenAmount = otcOrder.getTokenAmount();
        if (!userId.equals(otcOrder.getUid())) {
            throw new BusinessException("????????????");
        }

        if (otcOrder.getState().equals(OrderStatus.COMPLAIN.getStatus())) {
            throw new BusinessException("??????????????????????????????");
        }

        //????????????ZDT
        if (otcOrder.getType() == 1) {
            UserGson adUserGson = userGsonMapper.findOneUserGsonByLock(otcOrder.getAdvertisingUid());
            adUserGson.setZdtlocknum(adUserGson.getZdtlocknum().subtract(tokenAmount));
            adUserGson.setUpdatetime(new Date());
            userGsonMapper.updateByPrimaryKey(adUserGson);

            otcOrder.setState(OrderStatus.CANCEL.getStatus());
            otcOrder.setCancelAt(new Date());
            otcOrderMapper.updateByPrimaryKey(otcOrder);

            Advertising advertising = advertisingMapper.selectOneByLock(otcOrder.getAdvertisingId());
            advertising.setRemainingAmount(advertising.getRemainingAmount().add(tokenAmount));
            advertisingMapper.updateByPrimaryKey(advertising);
        } else {
            UserGson oneUserGson = userGsonMapper.findOneUserGsonByLock(userId);
            oneUserGson.setZdtlocknum(oneUserGson.getZdtlocknum().add(tokenAmount));
            oneUserGson.setUpdatetime(new Date());
            userGsonMapper.updateByPrimaryKey(oneUserGson);
            //Advertising advertising = advertisingMapper.selectOneByLock(otcOrder.getAdvertisingId());
            //advertising.setRemainingAmount(advertising.getRemainingAmount().add(tokenAmount));
            //advertisingMapper.updateByPrimaryKey(advertising);
            otcOrder.setState(OrderStatus.CANCEL.getStatus());
            otcOrder.setCancelAt(new Date());
            otcOrderMapper.updateByPrimaryKey(otcOrder);
        }

    }

    /**
     * ????????????
     *
     * @param complaintOrderInDto
     */
    @Override
    public void complaintOrder(ComplaintOrderInDto complaintOrderInDto) {
        Integer userId = Common.getUserId();

        //????????????????????????
        OtcOrder queryOrder = otcOrderMapper.findOneOrderByLock(complaintOrderInDto.getOrderId());
        if (queryOrder.getState().equals(OrderStatus.COMPLAIN.getStatus())) {
            throw new BusinessException("???????????????");
        }

        if (userId.equals(queryOrder.getAdvertisingUid()) || userId.equals(queryOrder.getUid())) {
            if (OrderStatus.CANCEL.getStatus().equals(queryOrder.getState())) {
                throw new BusinessException("???????????????");
            }

            queryOrder.setState(OrderStatus.COMPLAIN.getStatus());
            queryOrder.setComplaintAt(new Date());
            otcOrderMapper.updateByPrimaryKey(queryOrder);

        } else {
            throw new BusinessException("??????????????????");
        }
    }

    /**
     * h??????????????????
     *
     * @param queryOrderInDto - 1 ????????? 2 ?????????
     * @return
     */
    @Override
    public List<OtcOrderOutDto> getOtcOrderList(OrderSearchInDto queryOrderInDto) {
        UserGson userGson = Common.getUserGson();
        Integer userGsonId = userGson.getId();
        List<OtcOrderOutDto> outDtoList = new ArrayList<>();
        List<Byte> states = new ArrayList<>(3);
        if (queryOrderInDto.getType() == 1) {
            states.add(OrderStatus.UN_PAY.getStatus());
            states.add(OrderStatus.PAY.getStatus());
            states.add(OrderStatus.COMPLAIN.getStatus());
        } else {
            states.add(OrderStatus.RELEASE.getStatus());
            states.add(OrderStatus.CANCEL.getStatus());
            states.add(OrderStatus.HANDLE.getStatus());
        }
        PageHelper.startPage(queryOrderInDto.getPageNum(), queryOrderInDto.getPageSize());
        List<OtcOrder> otcOrders = otcOrderMapper.selectByUidAndState(userGsonId, states);
        outDtoList = orders2OutDtos(otcOrders, outDtoList, userGsonId);
        return outDtoList;


    }

    private List<OtcOrderOutDto> orders2OutDtos(List<OtcOrder> orders, List<OtcOrderOutDto> outDtoList, Integer uid) {
        if (orders != null && orders.size() > 0) {
            for (OtcOrder otcorder : orders) {
                OtcOrderOutDto outDto = new OtcOrderOutDto();
                if (uid.equals(otcorder.getAdvertisingUid())) {
                    outDto.setOwnPubAdvertising((byte) 1);
                } else {
                    outDto.setOwnPubAdvertising((byte) 2);
                }
                outDto.setId(otcorder.getId());
                outDto.setState(otcorder.getState());
                outDto.setType(otcorder.getType());
                outDto.setUserName(otcorder.getUserName());
                outDto.setOrderNo(otcorder.getOrderNo());
                outDtoList.add(outDto);
            }
        }
        return outDtoList;
    }

    /**
     * ??????????????????
     *
     * @param orderNo
     * @return
     */
    @Override
    public OtcOrderDetailOutDto getOtcOrderDetail(Long orderNo) {
        if (orderNo == null || orderNo == 0) {
            return null;
        }
        Integer userId = Common.getUserId();

        OtcOrder otcOrder = otcOrderMapper.selectByOrderNo(orderNo);
        OtcOrderDetailOutDto otcOrderDetailOutDto = null;

        if (orderNo != null) {
            otcOrderDetailOutDto = new OtcOrderDetailOutDto();
            Date orderAt = otcOrder.getOrderAt();
            long countDown = (orderAt.getTime() + 30 * 60 * 1000) - System.currentTimeMillis();
            BeanUtils.copyProperties(otcOrder, otcOrderDetailOutDto);
            otcOrderDetailOutDto.setCountdown(countDown);

            PaymentType paymentType = paymentTypeMapper.selectByPrimaryKey(otcOrder.getPayTypeId());
            PaymentTypeOutDto paymentTypeOutDto = new PaymentTypeOutDto();
            if (paymentType != null) {
                BeanUtils.copyProperties(paymentType, paymentTypeOutDto);
            }

            UserGson userGson;
            List<PaymentTypeOutDto> paymentTypeList;

            if (userId.equals(otcOrder.getUid())) {
                List<PaymentType> paymentTypes = paymentTypeMapper.selectByUid(otcOrder.getAdvertisingUid());
                userGson = userGsonMapper.selectByPrimaryKey(otcOrder.getAdvertisingUid());
                paymentTypeList = getPaymentTypeList(paymentTypes, paymentType, paymentTypeOutDto);

            } else {
                List<PaymentType> paymentTypes = paymentTypeMapper.selectByUid(otcOrder.getUid());
                userGson = userGsonMapper.selectByPrimaryKey(otcOrder.getUid());
                paymentTypeList = getPaymentTypeList(paymentTypes, paymentType, paymentTypeOutDto);
            }

            otcOrderDetailOutDto.setPaymentType(paymentTypeOutDto);
            otcOrderDetailOutDto.setPaymentTypeList(paymentTypeList);
            otcOrderDetailOutDto.setPhoneNum(userGson.getUserphone());

        }


        return otcOrderDetailOutDto;
    }

    private List<PaymentTypeOutDto> getPaymentTypeList(List<PaymentType> paymentTypes, PaymentType paymentType, PaymentTypeOutDto paymentTypeOutDto) {
        List<PaymentTypeOutDto> paymentTypeList = new ArrayList<>();
        if (paymentTypes != null && paymentTypes.size() > 0) {
            for (PaymentType type : paymentTypes) {
                if (paymentType == null) {
                    BeanUtils.copyProperties(type, paymentTypeOutDto);
                }
                PaymentTypeOutDto outDto = new PaymentTypeOutDto();
                BeanUtils.copyProperties(type, outDto);
                paymentTypeList.add(outDto);
            }

        }
        return paymentTypeList;

    }

    /**
     * ??????????????????????????????
     *
     * @param status
     * @return
     */
    @Override
    public List<OtcOrder> getOrderByStatus(Byte status) {
        List<OtcOrder> orderByStatus = otcOrderMapper.getOrderByStatus(status);

        return orderByStatus;
    }

    /**
     * ??????????????????
     *
     * @param orderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmTransferces(Integer orderId) {

        Integer userId = Common.getUserId();
        OtcOrder otcOrder = otcOrderMapper.findOneOrderByLock(orderId);
        Advertising advertising = advertisingMapper.selectByPrimaryKey(otcOrder.getAdvertisingId());

        if (otcOrder == null) {
            throw new BusinessException("???????????????");
        }
        if (otcOrder.getState().equals(OrderStatus.PAY.getStatus())) {
            throw new BusinessException("???????????????");
        }
        Byte type = otcOrder.getType();
        if (type == 1) {
            if (!userId.equals(otcOrder.getUid())) {
                throw new BusinessException("??????????????????");
            }
        } else {
            if (userId.equals(otcOrder.getAdvertisingUid())) {
                throw new BusinessException("??????????????????");
            }
        }

        otcOrder.setState(OrderStatus.PAY.getStatus());
        otcOrder.setOrderAt(new Date());
        otcOrderMapper.updateByPrimaryKey(otcOrder);
        HashMap<String, String> dataMap = new HashMap<>(4);

        if (userId.equals(otcOrder.getUid())) {
            dataMap.put("orderType","??????");
            sendConfirmSms("placeOrder",advertising.getPhone(),dataMap);
        }

        if (userId.equals(otcOrder.getAdvertisingUid())){
            dataMap.put("orderNo",otcOrder.getOrderNo().toString());
            UserGson userGson = userGsonMapper.selectByPrimaryKey(otcOrder.getUid());
            sendConfirmSms("releaseOrder",userGson.getUserphone(),dataMap);
        }

    }

    private void sendConfirmSms(String type,String phone,HashMap<String,String> map) {
        SmsSendInDto smsSendInDto = new SmsSendInDto();
        smsSendInDto.setType(type);
        smsSendInDto.setPhone(phone);
        smsSendInDto.setData(map);
        smsService.sendSms(smsSendInDto);
    }


    /**
     * ????????????????????????????????????
     *
     * @param phone
     */
    @Override
    public void sendSellSms(String phone) {
        SmsSendInDto smsSendInDto = new SmsSendInDto();
        smsSendInDto.setPhone(phone);
        smsSendInDto.setType("placeOrder");
        HashMap<String, String> dataMap = new HashMap<>(4);
        dataMap.put("placeOrder", "??????");
        smsSendInDto.setData(dataMap);
        smsService.sendSms(smsSendInDto);
    }

    ///**
    // * ??????
    // *
    // * @param otcOrderInDto-????????????
    // * @return
    // */
    //@Override
    //public Long placeOrder(OtcOrderInDto otcOrderInDto) {
    //
    //    //???????????? ??????id ???????????????id ??????id
    //    if (otcOrderInDto.getUid() == null || otcOrderInDto.getAdvertisingUid() == null || otcOrderInDto.getAdvertisingId() == null || otcOrderInDto.getPrice() == null || otcOrderInDto.getTokenAmount() == null || otcOrderInDto.getTradeAmount() == null || otcOrderInDto.getType() == null || StringUtils.isEmpty(otcOrderInDto.getUserName())) {
    //        throw new BusinessException("????????????????????????");
    //    }
    //    //?????????????????? ??????????????????
    //    //UserGson userGson = userGsonMapper.selectByPrimaryKey(otcOrderInDto.getUid());
    //
    //    //??????????????????????????????????????? for update
    //    Advertising advertising = advertisingMapper.selectOneByLock(otcOrderInDto.getAdvertisingId());
    //
    //    //????????????????????????
    //    BigDecimal remainingAmount = advertising.getRemainingAmount();
    //    if (remainingAmount.compareTo(otcOrderInDto.getTokenAmount()) >= 0) {
    //        Advertising record = new Advertising();
    //        record.setId(otcOrderInDto.getAdvertisingId());
    //        record.setRemainingAmount(remainingAmount.multiply(otcOrderInDto.getTokenAmount()));
    //        advertisingMapper.updateByPrimaryKey(record);
    //    }else {
    //        throw new BusinessException("????????????");
    //    }
    //
    //    //????????????
    //    OtcOrder otcOrder = new OtcOrder();
    //    long orderId = System.nanoTime();
    //    BeanUtils.copyProperties(otcOrderInDto,otcOrder);
    //    otcOrder.setOrderNo(orderId);
    //    otcOrder.setState((byte) OrderStatus.UN_PAY.getStatus().intValue());
    //    otcOrder.setOrderAt(new Date());
    //    otcOrderMapper.insert(otcOrder);
    //
    //    //??????????????????????????????30???????????? TODO
    //
    //    return orderId;
    //}
    //
    ///**
    // * ????????????
    // * @param releaseOrderInDto-????????????
    // */
    //@Override
    //public void releaseOrder(ReleaseOrderInDto releaseOrderInDto) {
    //    //????????????
    //    if (releaseOrderInDto.getOrderId() == null || StringUtils.isEmpty(releaseOrderInDto.getPayPwd())) {
    //        throw new BusinessException("????????????????????????");
    //    }
    //
    //    //??????????????????
    //    OtcOrder queryOrder = otcOrderMapper.selectByPrimaryKey(releaseOrderInDto.getOrderId());
    //    UserGson userGson = userGsonMapper.selectByPrimaryKey(queryOrder.getUid());
    //    String pwd = Md5Util.generate(releaseOrderInDto.getPayPwd());
    //    if (!userGson.getPaypassword().equals(pwd)) {
    //        throw new BusinessException("??????????????????");
    //    }
    //
    //    //????????????????????????
    //    if (OrderStatus.CANCEL.getStatus() == (int)queryOrder.getState()) {
    //        throw new BusinessException("???????????????");
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
    // * ????????????
    // * @param cancelOrderInDto
    // */
    //@Override
    //public void cancelOrder(CancelOrderInDto cancelOrderInDto) {
    //    Integer orderId = cancelOrderInDto.getOrderId();
    //    if (orderId == null || orderId == 0) {
    //        throw new BusinessException("????????????????????????");
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
    // * ????????????
    // * @param complaintOrderInDto
    // */
    //@Override
    //public void complaintOrder(ComplaintOrderInDto complaintOrderInDto) {
    //    if (complaintOrderInDto.getOrderId() == null || complaintOrderInDto.getOrderId() == 0) {
    //        throw new BusinessException("????????????????????????");
    //    }
    //
    //    OtcOrder queryOrder = otcOrderMapper.selectByPrimaryKey(complaintOrderInDto.getOrderId());
    //    if (OrderStatus.CANCEL.getStatus() == (int)queryOrder.getState()){
    //        throw new BusinessException("???????????????");
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
    // * ??????????????????
    // * @param queryOrderInDto
    // * @return
    // */
    //@Override
    //public PageInfo<OtcOrder> getOtcOrderList(OrderSearchInDto queryOrderInDto) {
    //    if (queryOrderInDto.getUid() == null){
    //        throw new BusinessException("????????????????????????");
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
    // * ??????????????????
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
