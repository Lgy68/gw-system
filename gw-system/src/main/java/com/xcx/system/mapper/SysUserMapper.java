package com.xcx.system.mapper;

import com.xcx.common.domain.entiy.SysUser;

import java.util.List;

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

    List<SysUser> selectAllocatedList(SysUser user);

    List<SysUser> selectUnallocatedList(SysUser user);

    List<SysUser> selectUserList(SysUser user);

    int updateUser(SysUser user);

    int deleteUserByIds(Long[] userIds);

    SysUser selectUserById(Long userId);

    SysUser checkPhoneUnique(String phonenumber);

    SysUser checkEmailUnique(String email);

    int resetUserPwd(String userName, String password);
}
