package com.xcx.system.service.impl;

import com.xcx.common.Exception.ServiceException;
import com.xcx.common.constant.UserConstants;
import com.xcx.common.domain.entiy.SysRole;
import com.xcx.common.domain.entiy.SysRoleDept;
import com.xcx.common.domain.entiy.SysUser;
import com.xcx.common.domain.entiy.SysUserRole;
import com.xcx.common.utils.SecurityUtils;
import com.xcx.common.utils.SpringUtils;
import com.xcx.common.utils.StringUtils;
import com.xcx.system.mapper.SysRoleDeptMapper;
import com.xcx.system.mapper.SysRoleMapper;
import com.xcx.system.mapper.SysRoleMenuMapper;
import com.xcx.system.mapper.SysUserRoleMapper;
import com.xcx.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ISysRoleServiceImpl implements ISysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public int insertRole(SysRole role) {
        return roleMapper.insertRole(role);
    }

    @Override
    public String checkRoleNameUnique(SysRole role) {
        long roleId = role.getRoleId().longValue();
        SysRole sysRole = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(roleId) && roleId != sysRole.getRoleId().longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int updateRole(SysRole role) {
        return roleMapper.updateRole(role);
    }

    @Override
    public int deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }
        // 删除角色与菜单关联
        roleMenuMapper.deleteRoleMenu(roleIds);
        // 删除角色与部门关联
        roleDeptMapper.deleteRoleDept(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }

    private int countUserRoleByRoleId(Long roleId) {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        return roleMapper.selectRoleList(role);
    }

    @Override
    public SysRole selectRoleById(Long roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    @Override
    public int checkRoleKeyUnique(SysRole role) {
        return roleMapper.checkRoleKeyUnique(role);
    }

    @Override
    public void checkRoleAllowed(SysRole role) {
        if (StringUtils.isNull(role.getRoleId()) || !role.isAdmin()) {
            throw new ServiceException("该角色不能操作超级管理员的权限");
        }
    }

    @Override
    public void checkRoleDataScope(Long roleId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            SysRole role = new SysRole();
            role.setRoleId(roleId);
            List<SysRole> sysRoles = SpringUtils.getAopProxy(this).selectRoleList(role);
            if (StringUtils.isEmpty(sysRoles)) {
                throw new ServiceException("没有权限访问角色数据!");
            }
        }
    }

    @Override
    public List<SysRole> selectRoleAll() {
        return roleMapper.selectRoleAll();
    }

    @Override
    public int authDataScope(SysRole role) {
        roleMapper.updateRole(role);
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        return insertRoleDept(role);
    }

    @Override
    public int updateRoleStatus(SysRole role) {
        return roleMapper.updateRole(role);
    }

    @Override
    public int deleteAuthUser(SysUserRole userRole)
    {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds) {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        List<SysRole> userRoles = roleMapper.selectRolePermissionByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles)
        {
            for (SysRole userRole : userRoles)
            {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue())
                {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds) {
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : userIds)
        {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }

    private int insertRoleDept(SysRole role) {
        int row = 1;
        List<SysRoleDept> list = new ArrayList<>();
        for (Long deptId : role.getDeptIds()) {
            SysRoleDept roleDept = new SysRoleDept();
            roleDept.setDeptId(deptId);
            roleDept.setRoleId(role.getRoleId());
            list.add(roleDept);
        }
        if (list.size() > 0) {
            row = roleDeptMapper.batchRoleDept(list);
        }
        return row;
    }
}
