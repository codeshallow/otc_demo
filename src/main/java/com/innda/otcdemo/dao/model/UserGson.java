package com.innda.otcdemo.dao.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.dao.model
 * @date 2022/2/23 01:07
 */

public class UserGson {
    private Integer id;
    private String userphone;
    private long voucher;
    private Date createtime;
    private Date updatetime;
    private long tmpvoucher;
    private String password;

    private String type;
    private BigDecimal zdtnum;

    private BigDecimal zdtlocknum;

    private String paypassword;

    private String nickname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public long getVoucher() {
        return voucher;
    }

    public void setVoucher(long voucher) {
        this.voucher = voucher;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public long getTmpvoucher() {
        return tmpvoucher;
    }

    public void setTmpvoucher(long tmpvoucher) {
        this.tmpvoucher = tmpvoucher;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getZdtnum() {
        return zdtnum;
    }

    public void setZdtnum(BigDecimal zdtnum) {
        this.zdtnum = zdtnum;
    }

    public BigDecimal getZdtlocknum() {
        return zdtlocknum;
    }

    public void setZdtlocknum(BigDecimal zdtlocknum) {
        this.zdtlocknum = zdtlocknum;
    }

    public String getPaypassword() {
        return paypassword;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
