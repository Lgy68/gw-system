package com.xcx.system.mapper;

import com.xcx.common.domain.Response;
import com.xcx.common.domain.entiy.SysMenu;

import java.util.List;

/**
 * 菜单表 数据层
 *
 * @author ruoyi
 */
public interface SysMenuMapper
{
    /**
     * 根据角色ID查询权限
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    public List<String> selectMenuPermsByRoleId(Long roleId);

    public List<String> selectMenuPermsByUserId(Long userId);

    int insertMenu(SysMenu menu);

    int updateMenu(SysMenu menu);

    int hasChildByMenuId(Long menuId);

    int removeMenu(Long menuId);

    List<SysMenu> queryMenuList(SysMenu menu);

    SysMenu selectMenuById(Long menuId);
}
