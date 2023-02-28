package com.xcx.framework.service;

import com.xcx.common.domain.entiy.SysRole;
import com.xcx.common.domain.entiy.SysUser;
import com.xcx.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户权限处理
 */
@Component
public class SysPermissionService {

    @Autowired
    private ISysMenuService menuService;

    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<>();
        //如果是管理员添加所有的权限
        if (user.isAdmin()) {
            perms.add("*.*.*");
        } else {
            List<SysRole> roles = user.getRoles();
            if (!roles.isEmpty() && roles.size() > -1) {
                for (SysRole role : roles) {
                    Set<String> rolePerms = menuService.selectMenuPermsByRoleId(user.getRoleId());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            } else {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
}
