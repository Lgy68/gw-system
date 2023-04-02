package com.xcx.controller;

import com.xcx.common.annotation.Log;
import com.xcx.common.domain.Response;
import com.xcx.common.domain.entiy.SysOperLog;
import com.xcx.common.domain.page.TableDataInfo;
import com.xcx.common.enums.BusinessType;
import com.xcx.common.utils.poi.ExcelUtil;
import com.xcx.system.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作日志
 */
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController{

    @Autowired
    private ISysOperLogService operLogService;

    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog)
    {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOperLog operLog)
    {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        util.exportExcel(response, list, "操作日志");
    }

    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public Response remove(@PathVariable Long[] operIds)
    {
        int success = operLogService.deleteOperLogByIds(operIds);
        if (success <= 0)
            return Response.error();
        return Response.success();
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public Response clean()
    {
        operLogService.cleanOperLog();
        return Response.success();
    }
}
