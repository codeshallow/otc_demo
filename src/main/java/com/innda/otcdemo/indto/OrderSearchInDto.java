package com.innda.otcdemo.indto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.indto
 * @date 2022/3/15 22:34
 */
@Data
public class OrderSearchInDto {
    @In(value = "1,2", message = "type错误")
    private Byte type;
    @Min(value = 1, message = "pageNum错误")
    private Integer pageNum = 1;
    @Min(value = 1, message = "pageSize最小为1")
    @Max(value = 20, message = "pageSize不能超过20")
    private Integer pageSize = 10;

    private Integer uid;
}
