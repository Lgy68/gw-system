package com.xcx.framework.service;

import com.xcx.common.Exception.CaptchaExpireException;
import com.xcx.common.Exception.ServiceException;
import com.xcx.common.Exception.UserPasswordNotMatchException;
import com.xcx.common.common.RedisCache;
import com.xcx.common.domain.LoginUser;
import com.xcx.common.domain.entiy.SysUser;
import com.xcx.common.usu.Constants;
import com.xcx.common.utils.MessageUtils;
import com.xcx.common.utils.StringUtils;
import com.xcx.framework.config.AuthenticationContextHolder;
import com.xcx.framework.manager.AsyncManager;
import com.xcx.framework.manager.factory.AsyncFactory;
import com.xcx.system.service.ISysConfigService;
import com.xcx.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 登录校验方法
 */
@Component
public class SysLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService configService;

    /*
     * 登录验证
     */
    public String login(String username, String password, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, code, uuid);
        }
        // 用户验证
        Authentication authenticate = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, "用户不存在/密码错误"));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        return tokenService.createToken(loginUser);
    }

    /*
     * 记录登录日志
     */
    private void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginDate(new Date());
        userService.updateUserProfile(sysUser);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = "captcha_codes:" + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaExpireException();
        }
    }

}
