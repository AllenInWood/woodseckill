package com.seckill.woodseckill.result;

public class CodeMsg {
    private int code;
    private String msg;

    // general error
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "server side errors");
    // login error 5002XX

    // goods error 5003XX

    // order error 5004XX

    // seckill error 5005XX

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
