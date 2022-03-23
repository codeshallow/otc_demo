package com.innda.otcdemo.common.enums;

/**
 * @author qianyu
 * @title   常量数据定义
 * @Package com.innda.otcdemo.common.enums
 * @date 2022/3/23 14:31
 */
public enum ConstantEnum {
    //test
    TEST("test");
    private String val;

    ConstantEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
