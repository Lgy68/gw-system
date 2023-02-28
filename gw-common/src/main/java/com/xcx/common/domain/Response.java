package com.xcx.common.domain;

import com.xcx.common.utils.StringUtils;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 返回数据
 */
@Data
public class Response extends HashMap implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    public static String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static String DATA_TAG = "data";

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public Response(int code, String msg) {
        super.put(CODE_TAG,code);
        super.put(MSG_TAG,msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public Response(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static Response success() {
        return Response.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static Response success(Object data) {
        return Response.success("操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static Response success(String msg) {
        return Response.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static Response success(String msg, Object data) {
        return new Response(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static Response error() {
        return Response.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static Response error(String msg) {
        return Response.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param code 返回状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static Response error(int code, String msg) {
        return new Response(code, msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Response error(String msg, Object data) {
        return new Response(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static Response error(int code, String msg, Object data) {
        return new Response(code, msg, data);
    }

}
