package com.innda.otcdemo.dao.mapper;

import com.innda.otcdemo.dao.model.OtcOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OtcOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OtcOrder record);

    OtcOrder selectByPrimaryKey(Integer id);

    OtcOrder findOneOrderByLock(Integer id);

    OtcOrder selectByOrderNo(Long orderNo);

    List<OtcOrder> selectAll();

    List<OtcOrder> selectByUid(Integer uid);

    int updateByPrimaryKey(OtcOrder record);

    List<OtcOrder> selectByUidAndState(@Param("uid") Integer uid, @Param("states") List<Byte> states);

    //List<OtcOrder> queryOrdersByCondition(OrderSearchInDto queryOrderInDto);

    List<OtcOrder> getOrderByStatus(Byte state);

    //public int updateBatchOrders(List<OtcOrder> otcOrderList);
}