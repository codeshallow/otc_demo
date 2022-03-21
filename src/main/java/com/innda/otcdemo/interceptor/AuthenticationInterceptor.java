package com.innda.otcdemo.interceptor;


import cn.iinda.validator.constraints.UserLoginToken;
import com.innda.otcdemo.common.enums.Headers;
import com.innda.otcdemo.common.enums.ResCodeEnum;
import com.innda.otcdemo.common.exception.BusinessException;
import com.innda.otcdemo.config.Common;
import com.innda.otcdemo.dao.mapper.TokenInfoMapper;
import com.innda.otcdemo.dao.mapper.UserGsonMapper;
import com.innda.otcdemo.dao.model.TokenInfo;
import com.innda.otcdemo.dao.model.UserGson;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 登陆状态验证拦截器
 *
 * @author alibeibei
 */
@Data
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenInfoMapper tokenInfoMapper;
    @Autowired
    private UserGsonMapper userGsonMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //web通道验证
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                String token = request.getHeader(Headers.Token.getType());
                if (StringUtils.isEmpty(token)) {
                    throw new BusinessException(ResCodeEnum.TOKEN_ERROR.getCode(), "token不能为空");
                }
                TokenInfo tokenInfo = tokenInfoMapper.findTokenInfoByTokenAndType(token, 3);
                if (tokenInfo == null) {
                    throw new BusinessException(ResCodeEnum.TOKEN_ERROR.getCode(), "token不存在");
                }
                if (System.currentTimeMillis() > tokenInfo.getEndtime().getTime()) {
                    throw new BusinessException(ResCodeEnum.TOKEN_ERROR.getCode(), "token已经过期，请重新登陆");
                }
                UserGson userGson = userGsonMapper.selectByPrimaryKey(Integer.valueOf(tokenInfo.getUserid()));
                Common.userGson.set(userGson);
            }
            return true;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            Common.removeUserGson();
        }
    }
}
