package com.xcx.framework.exception;

import com.xcx.common.Exception.ServiceException;
import com.xcx.common.domain.Response;
import com.xcx.common.utils.HttpUtils;
import com.xcx.common.utils.ServletUtils;
import com.xcx.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /*
     * 捕获抛出ServiceException的异常
     */
    @ExceptionHandler(value = ServiceException.class)
    public Response excepetionHandler(ServiceException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        int code = e.getCode();
        return StringUtils.isNotNull(code) ? Response.error(code, e.getMessage()) : Response.error(e.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public Response handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return Response.error(e.getMessage());
    }
}
