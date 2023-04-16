package com.xcx.system.mapper;

/**
 * 角色与菜单关联表
 */
public interface SysRoleMenuMapper
{
    /**
     * 查询菜单使用数量
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    public int checkMenuExistRole(Long menuId);

    public int deleteRoleMenu(Long[] roleIds);
}
