package com.xcx.controller;

import com.xcx.common.annotation.Log;
import com.xcx.common.constant.UserConstants;
import com.xcx.common.domain.Response;
import com.xcx.common.domain.entiy.SysPost;
import com.xcx.common.domain.page.TableDataInfo;
import com.xcx.common.enums.BusinessType;
import com.xcx.common.utils.poi.ExcelUtil;
import com.xcx.system.service.ISysPostService;
import org.jodconverter.office.utils.Lo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 岗位管理
 */
@RestController
@RequestMapping("/system/post")
public class SysPostController extends BaseController{

    @Autowired
    private ISysPostService postService;

    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize("@ss.hasPermi('system:post:add')")
    public Response add(@Validated @RequestBody SysPost post)
    {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post)))
        {
            return Response.error("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post)))
        {
            return Response.error("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(getUsername());
        int success = postService.insertPost(post);
        if (success < 1)
            return Response.error();
        return Response.success();
    }

    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    public Response edit(@Validated @RequestBody SysPost post)
    {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post)))
        {
            return Response.error("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post)))
        {
            return Response.error("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(getUsername());
        int success = postService.updatePost(post);
        if (success < 1)
            return Response.error();
        return Response.success();
    }

    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @PostMapping("/{postIds}")
    @PreAuthorize("@ss.hasPermi('system:post:remove')")
    public Response remove(@PathVariable Long[] postIds)
    {
        int success = postService.deletePostByIds(postIds);
        if (success < 1)
            return Response.error();
        return Response.success();
    }

    @GetMapping("/optionselect")
    public Response optionselect()
    {
        List<SysPost> posts = postService.selectPostAll();
        return Response.success(posts);
    }

    /**
     * 获取岗位列表
     */
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPost post)
    {
        startPage();
        List<SysPost> list = postService.selectPostList(post);
        return getDataTable(list);
    }

    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:post:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPost post)
    {
        List<SysPost> sysPosts = postService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        util.exportExcel(response,sysPosts,"岗位数据");
    }

    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @PostMapping("/{postId}")
    public Response getInfo(@PathVariable Long postId)
    {
        return Response.success(postService.selectPostById(postId));
    }
}
