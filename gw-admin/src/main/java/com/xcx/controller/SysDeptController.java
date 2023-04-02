package com.xcx.controller;

import com.xcx.common.annotation.Log;
import com.xcx.common.constant.UserConstants;
import com.xcx.common.domain.Response;
import com.xcx.common.domain.entiy.SysDept;
import com.xcx.common.enums.BusinessType;
import com.xcx.common.utils.StringUtils;
import com.xcx.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理
 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController{

    @Autowired
    private ISysDeptService deptService;

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Response add(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return Response.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(getUsername());
        int success = deptService.insertDept(dept);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Response edit(@Validated @RequestBody SysDept dept)
    {
        Long deptId = dept.getDeptId();
        deptService.checkDeptDataScope(deptId);
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return Response.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        else if (dept.getParentId().equals(deptId))
        {
            return Response.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus()) && deptService.selectNormalChildrenDeptById(deptId) > 0)
        {
            return Response.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(getUsername());
        int success = deptService.updateDept(dept);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public Response remove(@PathVariable Long deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return Response.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return Response.error("部门下存在用户,不允许删除");
        }
        deptService.checkDeptDataScope(deptId);
        int success = deptService.deleteDeptById(deptId);
        if (success > 0)
            return Response.success();
        return Response.error();
    }

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public Response list(SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return Response.success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public Response excludeChild(@PathVariable(value = "deptId", required = false) Long deptId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        return Response.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public Response getInfo(@PathVariable Long deptId)
    {
        deptService.checkDeptDataScope(deptId);
        return Response.success(deptService.selectDeptById(deptId));
    }

}
