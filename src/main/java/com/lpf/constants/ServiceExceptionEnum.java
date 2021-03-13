package com.lpf.constants;

public enum ServiceExceptionEnum {

    SUCCESS(0, "操作成功"),
    SYS_ERROR(2001001000, "服务器异常"),
    MISSING_REQUEST_PARAM_ERROR(2001001001, "参数缺失"),
    PARAM_VALID_FALSE(2001001002, "参数校验错误"),
    PARAM_CONVERT_FALSE(2001001003,"参数转换错误"),

    USER_NOT_FOUNT(1001002000, "用户不存在"),

    ;

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ServiceExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
