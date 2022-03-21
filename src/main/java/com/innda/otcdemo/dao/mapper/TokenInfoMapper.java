package com.innda.otcdemo.dao.mapper;


import com.innda.otcdemo.dao.model.TokenInfo;

import java.util.List;

public interface TokenInfoMapper {
    int deleteByPrimaryKey(String tokenid);

    int insert(TokenInfo record);

    TokenInfo selectByPrimaryKey(String tokenid);

    List<TokenInfo> selectAll();

    int updateByPrimaryKey(TokenInfo record);

    TokenInfo findTokenInfoByTokenAndType(String token, int type);
}