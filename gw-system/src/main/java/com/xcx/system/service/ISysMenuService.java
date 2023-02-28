package com.xcx.system.service;

import org.springframework.context.annotation.Primary;

import java.util.Set;

/**
 * 菜单 业务层
 * 
 * @author ruoyi
 */
@Primary
public interface ISysMenuService
{

    /**
     * 根据角色ID查询权限
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);

    public Set<String> selectMenuPermsByUserId(Long userId);
}
