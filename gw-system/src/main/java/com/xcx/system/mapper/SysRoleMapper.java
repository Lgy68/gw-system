package com.xcx.system.mapper;

import com.xcx.common.domain.entiy.SysRole;

import java.util.List;

public interface SysRoleMapper {

    int insertRole(SysRole role);

    SysRole selectRoleById(Long roleId);

    SysRole checkRoleNameUnique(String roleName);

    int updateRole(SysRole role);

    int deleteRoleByIds(Long[] roleIds);

    List<SysRole> selectRoleList(SysRole role);

    int checkRoleKeyUnique(SysRole role);

    List<SysRole> selectRoleAll();

    List<SysRole> selectRolePermissionByUserId(Long userId);

    List<SysRole> selectRolesByUserName(String username);
}
