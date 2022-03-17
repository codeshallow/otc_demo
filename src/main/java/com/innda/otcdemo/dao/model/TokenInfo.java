package com.innda.otcdemo.dao.model;

import lombok.Data;

import java.util.Date;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.dao.model
 * @date 2022/2/23 01:18
 */
@Data
public class TokenInfo {
    private String tokenid;

    private String userid;

    private String type;

    private Date createtime;

    private Date endtime;
}
