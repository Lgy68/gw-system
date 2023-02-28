package com.xcx.system.mapper;

import com.xcx.common.domain.entiy.SysUser;

/**
 * 用户表 数据层
 */
public interface SysUserMapper {

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);

    int updateUserProfile(SysUser sysUser);

    int checkUserNameUnique(String username);

    int insertUser(SysUser user);
}
