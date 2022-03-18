package com.innda.otcdemo.dao.mapper;

import com.innda.otcdemo.dao.model.UserGson;

import java.util.List;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.dao.mapper
 * @date 2022/2/23 01:23
 */
public interface UserGsonMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserGson record);

    UserGson selectByPrimaryKey(Integer id);

    List<UserGson> selectAll();

    int updateByPrimaryKey(UserGson record);

    int updatePayPassword(Integer id, String pwd);

    UserGson findOneOrderByLock(Integer id);
}
