package com.innda.otcdemo.common.enums;

public enum OrderStatus {
    //code码 状态 1:待支付 2:已支付 3:已放行 4:已取消 5:申述 6:已处理

    UN_PAY((byte) 1, "待支付"),
    PAY((byte) 2, "已支付"),
    RELEASE((byte) 3, "已放行"),
    CANCEL((byte) 4, "已取消"),
    COMPLAIN((byte) 5, "申述"),
    HANDLE((byte) 6, "已处理");

    private Byte status;
    private String message;

    OrderStatus(Byte status, String message) {
        this.status = status;
        this.message = message;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
