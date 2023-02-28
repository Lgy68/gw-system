package com.xcx.common.Exception;

import com.xcx.common.domain.HttpStatus;

/**
 * 业务异常处理
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private int code;
    /**
     * 错误提示
     */
    private String msg;
    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {
    }

    public ServiceException(String message) {
        new ServiceException(HttpStatus.ERROR, message);
    }

    public ServiceException(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public ServiceException(int code, String msg, String detailMessage) {
        this.code = code;
        this.msg = msg;
        this.detailMessage = detailMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}
