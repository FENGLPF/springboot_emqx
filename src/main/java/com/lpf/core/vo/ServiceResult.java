package com.lpf.core.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ServiceResult<T> implements Serializable {

    private T data;
    private Boolean success;
    private String message;
    private Integer code = 0;

    public static <T> ServiceResult<T> ok(String message) {
        ServiceResult<T> serviceResult = new ServiceResult<T>();
        serviceResult.setSuccess(true);
        serviceResult.setMessage(message);
        return serviceResult;
    }

    public static <T> ServiceResult<T> error(Integer code, String message){
        ServiceResult<T> serviceResult = new ServiceResult<>();
        serviceResult.setSuccess(false);
        serviceResult.setMessage(message);
        return serviceResult;
    }

}
