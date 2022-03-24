package com.innda.otcdemo.config;


import com.innda.otcdemo.common.enums.ResCodeEnum;
import com.innda.otcdemo.common.exception.BusinessException;
import com.innda.otcdemo.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

/**
 * 全局异常处理器
 *
 * @author fugq
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception ex) {
        ex.printStackTrace();
        Result result = new Result();
        result.setStatus(ResCodeEnum.ERROR.getCode());
        result.setMsg("系统繁忙");
        if (ex instanceof MethodArgumentNotValidException || ex instanceof BindException) {
            BindingResult bindingResult;
            if (ex instanceof MethodArgumentNotValidException) {
                bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            } else {
                bindingResult = ((BindException) ex).getBindingResult();
            }
            StringBuilder sb = new StringBuilder();
            String errorMsg = "";
            if (bindingResult.hasErrors()) {
                List<ObjectError> errorList = bindingResult.getAllErrors();
                for (int i = 0, size = errorList.size(); i < size; i++) {
                    ObjectError objectError = errorList.get(i);
                    FieldError fieldError = (FieldError) objectError;
                    String defaultMessage = fieldError.getDefaultMessage();
                    if (i == 0) {
                        errorMsg = defaultMessage;
                    }
                    sb.append(defaultMessage).append(";");
                }
            }
            log.error("异常:" + sb);
            result.setMsg(errorMsg);
        } else if (ex instanceof BusinessException) {
            BusinessException businessException = ((BusinessException) ex);
            result.setStatus(businessException.getErrCode());
            result.setMsg(businessException.getErrMsg());
            log.info("业务异常:{}", businessException.getErrMsg());
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            result.setStatus(ResCodeEnum.ERROR.getCode());
            result.setMsg("请求方式错误");
        } else if (ex instanceof MissingServletRequestParameterException || ex instanceof NumberFormatException) {
            result.setStatus(ResCodeEnum.ERROR.getCode());
            result.setMsg("参数错误");
        } else if (ex instanceof NoHandlerFoundException) {
            result.setStatus(ResCodeEnum.NOT_FUND.getCode());
            result.setMsg(null);
        } else {
            ex.printStackTrace();
            log.error("未知异常:message:{},stackTrace:{}", ex.getMessage(), ex.getStackTrace());
        }
        return result;
    }
}
