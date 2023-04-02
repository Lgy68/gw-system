package com.xcx.system.service;

import com.xcx.common.domain.entiy.SysRole;
import com.xcx.common.domain.entiy.SysUserRole;

import java.util.List;

/**
 * 角色管理
 */
public interface ISysRoleService {

    int insertRole(SysRole role);

    String checkRoleNameUnique(SysRole role);

    int updateRole(SysRole role);

    int deleteRoleByIds(Long[] roleIds);

    List<SysRole> selectRoleList(SysRole role);

    SysRole selectRoleById(Long role);

    int checkRoleKeyUnique(SysRole role);

    void checkRoleAllowed(SysRole role);

    void checkRoleDataScope(Long roleId);

    List<SysRole> selectRoleAll();

    int authDataScope(SysRole role);

    int updateRoleStatus(SysRole role);

    int deleteAuthUser(SysUserRole userRole);

    int deleteAuthUsers(Long roleId, Long[] userIds);

    int insertAuthUsers(Long roleId, Long[] userIds);

    List<SysRole> selectRolesByUserId(Long userId);
}
