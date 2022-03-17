package com.innda.otcdemo.common.enums;

/**
 * @author qianyu
 * @title   返回码
 * @Package com.innda.otcdemo.common
 * @date 2022/3/7 20:48
 */
public enum ResCodeEnum {

    //code码
    SUCCESS(200,"success"),
    ERROR(-1,"系统繁忙"),
    TOKEN_ERROR(2105,"token异常"),
    BODY_ERROR(401,"数据解析异常"),
    NOT_FOUND(404,"数据解析异常");

    private int code;
    private String msg;

    ResCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
