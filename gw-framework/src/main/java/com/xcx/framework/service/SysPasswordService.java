package com.xcx.framework.service;

import com.xcx.common.Exception.UserPasswordNotMatchException;
import com.xcx.common.common.RedisCache;
import com.xcx.common.domain.entiy.SysUser;
import com.xcx.common.utils.SecurityUtils;
import com.xcx.framework.config.AuthenticationContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * 登录密码方法
 */
@Component
public class SysPasswordService {

    @Autowired
    private RedisCache redisCache;

    public void getCacheKey(String loginName){
        redisCache.hasKey(loginName);
    }

    public void validate(SysUser user) {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        if (!SecurityUtils.matchesPassword(password,user.getPassword())) {
            throw new UserPasswordNotMatchException();
        }
    }
}
