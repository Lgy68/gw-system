package com.xcx.controller;

import com.xcx.common.annotation.Log;
import com.xcx.common.domain.Response;
import com.xcx.common.domain.entiy.SysNotice;
import com.xcx.common.domain.page.TableDataInfo;
import com.xcx.common.enums.BusinessType;
import com.xcx.system.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知公告表
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController{

    @Autowired
    private ISysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public Response getInfo(@PathVariable Long noticeId)
    {
        return Response.success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public Response add(@Validated @RequestBody SysNotice notice)
    {
        notice.setCreateBy(getUsername());
        int success = noticeService.insertNotice(notice);
        if (success <= 0)
            return Response.error();
        return Response.success();
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public Response edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(getUsername());
        int success = noticeService.updateNotice(notice);
        if (success <= 0)
            return Response.error();
        return Response.success();
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public Response remove(@PathVariable Long[] noticeIds)
    {
        int success = noticeService.deleteNoticeByIds(noticeIds);
        if (success <= 0)
            return Response.error();
        return Response.success();
    }
}
