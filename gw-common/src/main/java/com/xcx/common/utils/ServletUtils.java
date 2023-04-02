package com.xcx.common.utils;

import com.xcx.common.usu.Constants;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 客户端工具类
 */
public class ServletUtils {

    /*
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /*
     * 渲染字符串到客户端
     */
    public static void sendData(HttpServletResponse response, String data) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding(Constants.UTF8);
            response.getWriter().write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name, String defaultValue)
    {
        return Convert.toStr(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name)
    {
        return getRequest().getParameter(name);
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name)
    {
        return Convert.toBool(getRequest().getParameter(name));
    }
}
