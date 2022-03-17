package com.innda.otcdemo.dao.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.dao.model
 * @date 2022/2/23 01:15
 */
public class OtcOrder {
    private Integer id;

    private Integer uid;

    private Integer advertisingUid;

    private String userName;

    private Integer advertisingId;

    private Integer payTypeId;

    private Long orderNo;

    private BigDecimal price;

    private BigDecimal tradeAmount;

    private BigDecimal tokenAmount;

    private Date orderAt;

    private Byte type;

    private Byte state;

    private Date payAt;

    private Date releaseAt;

    private Date cancelAt;

    private Date complaintAt;

    private Date handleAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getAdvertisingUid() {
        return advertisingUid;
    }

    public void setAdvertisingUid(Integer advertisingUid) {
        this.advertisingUid = advertisingUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(Integer advertisingId) {
        this.advertisingId = advertisingId;
    }

    public Integer getPayTypeId() {
        return payTypeId;
    }

    public void setPayTypeId(Integer payTypeId) {
        this.payTypeId = payTypeId;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public BigDecimal getTokenAmount() {
        return tokenAmount;
    }

    public void setTokenAmount(BigDecimal tokenAmount) {
        this.tokenAmount = tokenAmount;
    }

    public Date getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(Date orderAt) {
        this.orderAt = orderAt;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getPayAt() {
        return payAt;
    }

    public void setPayAt(Date payAt) {
        this.payAt = payAt;
    }

    public Date getReleaseAt() {
        return releaseAt;
    }

    public void setReleaseAt(Date releaseAt) {
        this.releaseAt = releaseAt;
    }

    public Date getCancelAt() {
        return cancelAt;
    }

    public void setCancelAt(Date cancelAt) {
        this.cancelAt = cancelAt;
    }

    public Date getComplaintAt() {
        return complaintAt;
    }

    public void setComplaintAt(Date complaintAt) {
        this.complaintAt = complaintAt;
    }

    public Date getHandleAt() {
        return handleAt;
    }

    public void setHandleAt(Date handleAt) {
        this.handleAt = handleAt;
    }
}
