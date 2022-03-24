package com.innda.otcdemo.common.model;

import com.innda.otcdemo.common.enums.ResCodeEnum;
import lombok.Data;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.common
 * @date 2022/3/7 20:44
 */
@Data
public class Result {

    private int status;
    private String msg;
    private Object data;

    public Result() {
    }

    public Result(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Result(int status, Object data) {
        this.status = status;
        this.data = data;
    }

    public static Result ok() {
        return new Result(ResCodeEnum.SUCCESS.getCode(), "success", null);
    }

    public static Result ok(Object data) {
        return new Result(ResCodeEnum.SUCCESS.getCode(),"success",data);
    }

    public static Result build(int code,String msg,Object data) {
        return new Result(code,msg,data);
    }

    public static Result build(int code,Object data){
        return new Result(code,data);
    }

}
