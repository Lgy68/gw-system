package com.xcx.system.service;

import com.xcx.common.domain.TreeSelect;
import com.xcx.common.domain.entiy.SysMenu;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 */
public interface ISysMenuService {

    /**
     * 根据角色ID查询权限
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);

    public Set<String> selectMenuPermsByUserId(Long userId);

    int insertMenu(SysMenu menu);

    int updateMenu(SysMenu menu);

    boolean hasChildByMenuId(Long menuId);

    boolean checkMenuExistRole(Long menuId);

    int removeMenu(Long menuId);

    List<SysMenu> queryMenuList(Long userId);

    List<SysMenu> queryMenuList(SysMenu menu, Long userId);

    SysMenu selectMenuById(Long menuId);

    List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    List<Long> selectMenuListByRoleId(Long roleId);
}
