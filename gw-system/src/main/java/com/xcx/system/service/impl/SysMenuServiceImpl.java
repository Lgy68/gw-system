package com.xcx.system.service.impl;

import com.xcx.common.domain.entiy.SysMenu;
import com.xcx.common.utils.StringUtils;
import com.xcx.system.mapper.SysMenuMapper;
import com.xcx.system.mapper.SysRoleMenuMapper;
import com.xcx.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单 业务层处理
 *
 * @author ruoyi
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByRoleId(Long roleId) {
        List<String> perms = menuMapper.selectMenuPermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /*
     * 根据用户id查询权限
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 新增菜单信息
     *
     * @param menu
     * @return
     */
    @Override
    public int insertMenu(SysMenu menu) {
        return menuMapper.insertMenu(menu);
    }

    /**
     * 修改菜单信息
     *
     * @param menu
     * @return
     */
    @Override
    public int updateMenu(SysMenu menu) {
        return menuMapper.updateMenu(menu);
    }

    /**
     * 判断该菜单是否有子菜单
     *
     * @param menuId
     * @return
     */
    @Override
    public boolean hasChildByMenuId(Long menuId) {
        int ret = menuMapper.hasChildByMenuId(menuId);
        return ret > 0;
    }

    /**
     * 判断菜单是否被分配
     *
     * @param menuId
     * @return
     */
    @Override
    public boolean checkMenuExistRole(Long menuId) {
        int ret = roleMenuMapper.checkMenuExistRole(menuId);
        return ret > 0;
    }

    /**
     * 删除菜单信息
     *
     * @param menuId
     * @return
     */
    @Override
    public int removeMenu(Long menuId) {
        return menuMapper.removeMenu(menuId);
    }

    /**
     * 查询菜单列表
     *
     * @param menu
     * @return
     */
    @Override
    public List<SysMenu> queryMenuList(SysMenu menu) {
        return menuMapper.queryMenuList(menu);
    }

    @Override
    public SysMenu selectMenuById(Long menuId) {
        return menuMapper.selectMenuById(menuId);
    }

}
