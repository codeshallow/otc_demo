package com.innda.otcdemo.dao.mapper;

import com.innda.otcdemo.dao.model.PaymentType;

import java.util.List;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.dao.mapper
 * @date 2022/3/13 01:53
 */
public interface PaymentTypeMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(PaymentType record);

    PaymentType selectByPrimaryKey(Integer id);

    List<PaymentType> selectAll();

    int updateByPrimaryKey(PaymentType record);

    List<PaymentType> selectByUid(Integer uid);
}
