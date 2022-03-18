package com.innda.otcdemo.dao.mapper;

import com.innda.otcdemo.dao.model.Advertising;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdvertisingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Advertising record);

    Advertising selectByPrimaryKey(Integer id);

    List<Advertising> selectAll();

    List<Advertising> selectByType(Byte type);

    int updateByPrimaryKey(Advertising record);

    //悲观锁查询
    Advertising selectOneByLock(Integer id);

    Advertising selectOneByTypeAndLock(@Param("uid") Integer uid, @Param("type") Byte type);
}