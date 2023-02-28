package com.xcx.system.service;


import com.xcx.common.domain.entiy.SysUser;
import org.springframework.context.annotation.Primary;

@Primary
public interface ISysUserService {

    SysUser selectUserByUserName(String username);

    int updateUserProfile(SysUser sysUser);

    String checkUserNameUnique(String username);

    boolean insertUser(SysUser user);
}
