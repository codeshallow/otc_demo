package com.innda.otcdemo.common.enums;

/**
 * http头类型
 */
public enum Headers {

    /**
     * 加密方法
     */
    ENCODE_METHOD("EncodeMethod", "加密方法"),
    /**
     * 渠道
     */
    CHANNEL("Channel", "渠道"),
    /**
     * token
     */
    Token("Token", "Token"),
    /**
     * header内容类型
     */
    HEADER_CONTENT_TYPE("Content-Type", "application/json;charset=UTF-8");

    private String type;
    private String val;

    Headers(String type, String val) {
        this.val = val;
        this.type = type;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }}
