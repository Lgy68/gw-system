package com.xcx.controller;

import com.xcx.common.annotation.Log;
import com.xcx.common.constant.UserConstants;
import com.xcx.common.domain.LoginUser;
import com.xcx.common.domain.Response;
import com.xcx.common.domain.entiy.SysUser;
import com.xcx.common.enums.BusinessType;
import com.xcx.common.utils.SecurityUtils;
import com.xcx.common.utils.StringUtils;
import com.xcx.framework.service.TokenService;
import com.xcx.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 个人信息
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @GetMapping
    private Response profile(){
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        Response response = Response.success(user);
        response.put("roleGroup",userService.selectUserRoleGroup(loginUser.getUsername()));
        response.put("postGroup",userService.selectUserPostGroup(loginUser.getUsername()));
        return response;
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public Response updateProfile(@RequestBody SysUser user)
    {
        LoginUser loginUser = getLoginUser();
        SysUser sysUser = loginUser.getUser();
        user.setUserName(sysUser.getUserName());
        if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return Response.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return Response.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUserId(sysUser.getUserId());
        user.setPassword(null);
        user.setAvatar(null);
        user.setDeptId(null);
        if (userService.updateUserProfile(user) > 0)
        {
            // 更新缓存用户信息
            sysUser.setNickName(user.getNickName());
            sysUser.setPhonenumber(user.getPhonenumber());
            sysUser.setEmail(user.getEmail());
            sysUser.setSex(user.getSex());
            tokenService.setLoginUser(loginUser);
            return Response.success();
        }
        return Response.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public Response updatePwd(String oldPassword, String newPassword)
    {
        LoginUser loginUser = getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return Response.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return Response.error("新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0)
        {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            tokenService.setLoginUser(loginUser);
            return Response.success();
        }
        return Response.error("修改密码异常，请联系管理员");
    }

    /*
     * 修改头像
     */
    public Response avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        String upload = userService.upload(file, getLoginUser().getUserId());
        if (StringUtils.isEmpty(upload) || StringUtils.isNull(upload))
            return Response.error("存储到oss上出现异常！");
        return Response.success();
    }
}
