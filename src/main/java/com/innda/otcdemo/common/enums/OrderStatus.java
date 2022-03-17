package com.innda.otcdemo.common.enums;

public enum OrderStatus {
    //code码 状态 1:待支付 2:已支付 3:已放行 4:已取消 5:申述 6:已处理
    UN_PAY(1, "待支付"),
    PAY(2, "已支付"),
    RELEASE(3, "已放行"),
    CANCEL(4, "已取消"),
    COMPLAIN(5, "申述"),
    HANDLE(6, "已处理"),
    ;
    private Integer status;
    private String message;

    OrderStatus(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
