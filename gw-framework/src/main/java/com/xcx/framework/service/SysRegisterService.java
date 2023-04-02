package com.xcx.framework.service;

import com.xcx.common.Exception.CaptchaExpireException;
import com.xcx.common.Exception.ServiceException;
import com.xcx.common.common.RedisCache;
import com.xcx.common.constant.UserConstants;
import com.xcx.common.domain.entiy.SysUser;
import com.xcx.common.usu.Constants;
import com.xcx.common.utils.MessageUtils;
import com.xcx.common.utils.SecurityUtils;
import com.xcx.common.utils.StringUtils;
import com.xcx.framework.manager.AsyncManager;
import com.xcx.framework.manager.factory.AsyncFactory;
import com.xcx.system.domain.RegisterBody;
import com.xcx.system.service.ISysConfigService;
import com.xcx.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 注册校验方法
 */
@Component
public class SysRegisterService {

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 注册
     *
     * @param registerBody
     */
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        //如果验证码开关打开则验证 验证码
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }
        if (StringUtils.isEmpty(username)) {
            msg = "账户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(username))) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else {
            SysUser user = new SysUser();
            user.setUserName(username);
            user.setNickName(username);
            user.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(user);
            if (!regFlag) {
                msg = "注册失败，请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, "注册成功"));
            }
        }
        return msg;
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
        //注册过删除验证码
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaExpireException();
        }
    }
}
