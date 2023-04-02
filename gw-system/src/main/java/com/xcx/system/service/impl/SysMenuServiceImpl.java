package com.xcx.system.service.impl;

import com.xcx.common.domain.TreeSelect;
import com.xcx.common.domain.entiy.SysMenu;
import com.xcx.common.domain.entiy.SysRole;
import com.xcx.common.domain.entiy.SysUser;
import com.xcx.common.utils.StringUtils;
import com.xcx.system.mapper.SysMenuMapper;
import com.xcx.system.mapper.SysRoleMapper;
import com.xcx.system.mapper.SysRoleMenuMapper;
import com.xcx.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl implements ISysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

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

    @Override
    public List<SysMenu> queryMenuList(Long userId) {
        return queryMenuList(new SysMenu(), userId);
    }

    /**
     * 查询菜单列表
     *
     * @param menu
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> queryMenuList(SysMenu menu, Long userId) {
        List<SysMenu> menus = null;
        if (SysUser.isAdmin(userId)) {
            menus = menuMapper.queryMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menus = menuMapper.selectMenuListByUserId(menu);
        }
        return menus;
    }

    @Override
    public SysMenu selectMenuById(Long menuId) {
        return menuMapper.selectMenuById(menuId);
    }

    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus) {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        SysRole role = roleMapper.selectRoleById(roleId);
        return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param menus 菜单列表
     * @return 树结构列表
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysMenu sysMenu : menus) {
            tempList.add(sysMenu.getMenuId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext(); ) {
            SysMenu menu = iterator.next();
            //判断它是不是最顶级节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menus, menu);
                returnList.add(menu);
            }

        }
        if (returnList.isEmpty()) {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * 递归实现子列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        //获取顶级节点的子节点
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu menu : childList) {
            //判断子节点下面是否还有子节点,有的话就继续找子节点下的子节点
            if (hasChild(list, menu)) {
                recursionFn(list, menu);
            }
        }
    }

    /**
     * 获取子节点列表
     *
     * @param list
     * @param t
     * @return
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> childList = new ArrayList<>();
        Iterator<SysMenu> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getParentId().longValue() == t.getMenuId().longValue()) {
                childList.add(iterator.next());
            }
        }
        return childList;
    }

    /**
     * 判断子节点下面是否还有子节点
     *
     * @param list
     * @param menu
     * @return
     */
    private boolean hasChild(List<SysMenu> list, SysMenu menu) {
        return getChildList(list, menu).size() > 0;
    }

}
