package com.xcx.controller;

import com.xcx.common.annotation.Log;
import com.xcx.common.domain.Response;
import com.xcx.common.domain.entiy.SysMenu;
import com.xcx.common.enums.BusinessType;
import com.xcx.common.utils.StringUtils;
import com.xcx.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController{

    @Autowired
    private ISysMenuService menuService;

    @PostMapping
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    public Response insertMenu(@Validated @RequestBody SysMenu menu){
        menu.setCreateBy(getUsername());
        int success = menuService.insertMenu(menu);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    @PutMapping
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    public Response updateMenu(@Validated @RequestBody SysMenu menu){
        menu.setUpdateBy(getUsername());
        if (menu.getMenuId().equals(menu.getParentId())){
            return Response.error("修改菜单" + menu.getMenuName() + "’失败‘,上级菜单不能选择自己");
        }
        int success = menuService.updateMenu(menu);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    @DeleteMapping("/{menuId}")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    public Response removeMenu(@PathVariable("menuId") Long menuId){
        if (menuService.hasChildByMenuId(menuId)){
            return Response.error("该菜单存在子级菜单，不能删除");
        }
        if (menuService.checkMenuExistRole(menuId)){
            return Response.error("该菜单已分配，不能删除");
        }
        //应删除所有表中的与角色相关的信息
        int success = menuService.removeMenu(menuId);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    public Response queryMenuList(SysMenu menu){
        List<SysMenu> menus = menuService.queryMenuList(menu,getUserId());
        if (StringUtils.isEmpty(menus))
            return Response.error();
        return Response.success(menus);
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @GetMapping(value = "/{menuId}")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    public Response getInfo(@PathVariable("menuId") Long menuId)
    {
        return Response.success(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping(value = "/treeselect")
    public Response treeselect(SysMenu menu){
        List<SysMenu> menus = menuService.queryMenuList(menu, getUserId());
        return Response.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * 获取某个角色的下拉树列表
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public Response roleMenuTreeselect(@PathVariable("roleId") Long roleId){
        List<SysMenu> menus = menuService.queryMenuList(getUserId());
        Response response = Response.success();
        response.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        response.put("menus", menuService.buildMenuTreeSelect(menus));
        return response;
    }
}
