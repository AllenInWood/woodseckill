package com.seckill.woodseckill.result;

public class CodeMsg {
    private int code;
    private String msg;

    // general error
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "server side errors");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "attrs validation errors: %s");
    // login error 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "session not exist or expire");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "password cannot be empty");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "mobile number cannot be empty");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "mobile number format errors");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "mobile number not exist");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "password errors");

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

    public void addMsg(String addMsg) {
        this.msg += "\n" + addMsg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
