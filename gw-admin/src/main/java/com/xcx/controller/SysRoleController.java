package com.xcx.controller;

import com.xcx.common.annotation.Log;
import com.xcx.common.constant.UserConstants;
import com.xcx.common.domain.LoginUser;
import com.xcx.common.domain.Response;
import com.xcx.common.domain.entiy.*;
import com.xcx.common.domain.page.TableDataInfo;
import com.xcx.common.enums.BusinessType;
import com.xcx.common.utils.SecurityUtils;
import com.xcx.common.utils.StringUtils;
import com.xcx.framework.service.TokenService;
import com.xcx.system.service.ISysDeptService;
import com.xcx.system.service.ISysRoleService;
import com.xcx.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/system")
public class SysRoleController extends BaseController{

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysDeptService deptService;

    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    public Response insertRole(@Validated @RequestBody SysRole role){
        role.setCreateBy(getUsername());
        int success = roleService.insertRole(role);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    public Response updateRole(@Validated @RequestBody SysRole role){
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(getUsername());
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))){
            return Response.error("修改角色" + role.getRoleName() + "’失败‘,角色名称已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))){
            return Response.error("修改角色" + role.getRoleName() + "’失败‘,角色名称已存在");
        }
        //TODO 修改角色信息时应更新redis中的登录用户信息缓存
        int success = roleService.updateRole(role);
        if (success > 0)
            return Response.success();
        return Response.error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    @DeleteMapping("/{roleIds}")
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    public Response removeRole(@PathVariable("roleIds") Long[] roleIds){
        int success = roleService.deleteRoleByIds(roleIds);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    public TableDataInfo queryRoleList(SysRole role){
        startPage();
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    public Response getInfo(@PathVariable Long roleId){
        return Response.success(roleService.selectRoleById(roleId));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public Response changeStatus(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(getUsername());
        int success = roleService.updateRoleStatus(role);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    /**
     * 获取角色选择框列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public Response optionselect()
    {
        return Response.success(roleService.selectRoleAll());
    }

    /**
     * 修改保存数据权限 (更多里的数据权限按钮)
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @PutMapping("/dataScope")
    public Response dataScope(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        int success = roleService.authDataScope(role);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    /**
     * 查询已分配用户角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * 查询未分配用户角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * 取消授权用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public Response cancelAuthUser(@RequestBody SysUserRole userRole)
    {
        int success = roleService.deleteAuthUser(userRole);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    /**
     * 批量取消授权用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public Response cancelAuthUserAll(Long roleId, Long[] userIds)
    {
        int success = roleService.deleteAuthUsers(roleId, userIds);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    /**
     * 批量选择用户授权
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public Response selectAuthUserAll(Long roleId, Long[] userIds)
    {
        roleService.checkRoleDataScope(roleId);
        int success = roleService.insertAuthUsers(roleId, userIds);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    /**
     * 获取对应角色部门树列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/deptTree/{roleId}")
    public Response deptTree(@PathVariable("roleId") Long roleId)
    {
        Response ajax = Response.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.selectDeptTreeList(new SysDept()));
        return ajax;
    }
}
