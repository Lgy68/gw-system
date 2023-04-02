package com.xcx.system.service;


import com.xcx.common.domain.entiy.SysUser;
import org.springframework.context.annotation.Primary;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 用戶管理
 */
public interface ISysUserService {

    SysUser selectUserByUserName(String username);

    int updateUserProfile(SysUser sysUser);

    String checkUserNameUnique(String username);

    int insertUser(SysUser user);

    List<SysUser> selectAllocatedList(SysUser user);

    List<SysUser> selectUnallocatedList(SysUser user);

    void checkUserAllowed(SysUser user);

    void checkUserDataScope(Long userId);

    int updateUser(SysUser user);

    int deleteUserByIds(Long[] userIds);

    List<SysUser> selectUserList(SysUser user);

    boolean registerUser(SysUser user);

    SysUser selectUserById(Long userId);

    String importUser(List<SysUser> userList, boolean updateSupport, String operName);

    int resetPwd(SysUser user);

    int updateUserStatus(SysUser user);

    void insertUserAuth(Long userId, Long[] roleIds);

    String selectUserRoleGroup(String username);

    String selectUserPostGroup(String username);

    String checkPhoneUnique(SysUser user);

    String checkEmailUnique(SysUser user);

    int resetUserPwd(String userName, String encryptPassword);

    String upload(MultipartFile file, Long userId) throws IOException;
}
