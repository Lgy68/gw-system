package com.xcx.framework.config;

import org.springframework.security.core.Authentication;

/**
 * 身份验证信息
 */
public class AuthenticationContextHolder {

    public static final ThreadLocal<Authentication> contextHodler = new ThreadLocal<>();

    public static void setContext(Authentication authentication){
        contextHodler.set(authentication);
    }

    public static void clearContext(){
        contextHodler.remove();
    }

    public static Authentication getContext(){
        return contextHodler.get();
    }
}
