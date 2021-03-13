package com.lpf.core.exception;

import com.lpf.constants.ServiceExceptionEnum;
import lombok.Data;

@Data
public class ServiceException extends RuntimeException {

    private final Integer code;

    public ServiceException(ServiceExceptionEnum serviceExceptionEnum) {
        super(serviceExceptionEnum.getMessage());
        this.code = serviceExceptionEnum.getCode();
    }

}
