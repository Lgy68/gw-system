package com.xcx.controller;

import com.xcx.common.domain.Response;
import com.xcx.common.domain.entiy.SysMenu;
import com.xcx.common.utils.SecurityUtils;
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
public class MenuController {

    @Autowired
    private ISysMenuService menuService;

    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    public Response insertMenu(@Validated @RequestBody SysMenu menu){
        String username = SecurityUtils.getLoginUser().getUsername();
        menu.setCreateBy(username);
        int success = menuService.insertMenu(menu);
        if (success > 0)
            return Response.error();
        return Response.success();
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    public Response updateMenu(@Validated @RequestBody SysMenu menu){
        String username = SecurityUtils.getLoginUser().getUsername();
        menu.setUpdateBy(username);
        if (menu.getMenuId().equals(menu.getParentId())){
            return Response.error("修改菜单" + menu.getMenuName() + "’失败‘,上级菜单不能选择自己");
        }
        int success = menuService.updateMenu(menu);
        if (success > 0)
            return Response.error();
        return Response.success();
    }

    @DeleteMapping("/{menuId}")
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    public Response removeMenu(@PathVariable("menuId") Long menuId){
        if (menuService.hasChildByMenuId(menuId)){
            return Response.error("该菜单存在子级菜单，不能删除");
        }
        if (menuService.checkMenuExistRole(menuId)){
            return Response.error("该菜单已分配，不能删除");
        }
        int success = menuService.removeMenu(menuId);
        if (success > 0)
            return Response.error();
        return Response.success();
    }

    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    public Response queryMenuList(SysMenu menu){
        List<SysMenu> menus = menuService.queryMenuList(menu);
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
}
