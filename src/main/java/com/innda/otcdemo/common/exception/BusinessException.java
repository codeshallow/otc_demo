package com.innda.otcdemo.common.exception;

import lombok.Data;

/**
 * @author fugq
 */
@Data
public class BusinessException extends RuntimeException {
    private int errCode;
    private String errMsg;

    public BusinessException(String errMsg) {
        errCode = -1;
        this.errMsg = errMsg;
    }

    public BusinessException(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
