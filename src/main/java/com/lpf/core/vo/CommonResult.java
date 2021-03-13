package com.lpf.core.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommonResult<T> implements Serializable {

    public static Integer SUCCESS_CODE = 0;

    private Boolean success;

    private Integer code;

    private String message;

    private T data;

    public CommonResult(boolean success, Integer code, T data, String message) {
        super();
        this.success = success;
        this.code = code;
        this.data = data;
        this.message = message;
    }

}
