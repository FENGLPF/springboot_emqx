package com.lpf.core.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.lpf.constants.ServiceExceptionEnum;
import com.lpf.core.exception.ServiceException;
import com.lpf.core.vo.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice(basePackages = "com.yihuan.controller")
public class GlobalExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理ServiceException异常
     * @param request
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult serviceExceptionHandler(HttpServletRequest request, ServiceException ex) {
        return new CommonResult(false, ex.getCode(), null, ex.getMessage());
    }

    /**
     * 处理 MissingServletRequestParameterException 异常
     *
     * SpringMVC 参数不正确
     */
    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public CommonResult missingServletRequestParameterExceptionHandler(HttpServletRequest request,
                                                                       MissingServletRequestParameterException ex) {
        return new CommonResult(false, ServiceExceptionEnum.MISSING_REQUEST_PARAM_ERROR.getCode(),null,
                ServiceExceptionEnum.MISSING_REQUEST_PARAM_ERROR.getMessage());
    }

    /**
     * 处理参数校验异常
     * @param request
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult methodArgumentNotValidExceptionHandler(HttpServletRequest request,
                                                               MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();

        return new CommonResult(false, ServiceExceptionEnum.PARAM_VALID_FALSE.getCode(), null,
                errors.get(0).getDefaultMessage());

    }

    /**
     * 处理参数转换异常
     * @param request
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = InvalidFormatException.class)
    public CommonResult invalidFormatExceptionHandler(HttpServletRequest request,
                                                      InvalidFormatException ex) {
        String messages = String.format("字段 [%s] 的值 [%s] 不能转换成 [%s] 类型", ex.getPathReference(),
                ex.getValue(), ex.getTargetType().getName());

        return new CommonResult(false, ServiceExceptionEnum.PARAM_CONVERT_FALSE.getCode(), null,
                messages);
    }

    /**
     * 处理其它 Exception 异常
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public CommonResult exceptionHandler(HttpServletRequest req, Exception e) {
        // 记录异常日志
        log.error("[exceptionHandler]", e);
        // 返回 ERROR CommonResult
        return new CommonResult(false,ServiceExceptionEnum.SYS_ERROR.getCode(),null,
                ServiceExceptionEnum.SYS_ERROR.getMessage());
    }

}
