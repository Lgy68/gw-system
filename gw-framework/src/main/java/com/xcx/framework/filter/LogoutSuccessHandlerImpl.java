package com.xcx.framework.filter;

import com.alibaba.fastjson2.JSON;
import com.xcx.common.domain.HttpStatus;
import com.xcx.common.domain.LoginUser;
import com.xcx.common.domain.Response;
import com.xcx.common.usu.Constants;
import com.xcx.common.utils.ServletUtils;
import com.xcx.common.utils.StringUtils;
import com.xcx.framework.manager.AsyncManager;
import com.xcx.framework.manager.factory.AsyncFactory;
import com.xcx.framework.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录处理类
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private TokenService tokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String username = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.sendData(response, JSON.toJSONString(Response.error(HttpStatus.SUCCESS, "退出成功")));
    }
}
