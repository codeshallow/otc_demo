package com.innda.otcdemo.common.enums;

/**
 * 短信模版
 */
public enum SmsTemplateEnum {
    /**
     * 重置密码
     */
    RESET_PWD("resetPwd");

    private String type;

    SmsTemplateEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }}
