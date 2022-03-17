package com.innda.otcdemo.indto;

import lombok.Data;

/**
 * @author qianyu
 * @title
 * @Package com.innda.otcdemo.indto
 * @date 2022/3/7 20:57
 */
@Data
public class AdvertisingTypeInDto {
    @In(value = "1,2",message = "type错误")
    private Byte type;
}
