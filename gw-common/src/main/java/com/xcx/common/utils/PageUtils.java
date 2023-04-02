package com.xcx.common.utils;

import com.github.pagehelper.PageHelper;
import com.xcx.common.domain.page.PageDomain;
import com.xcx.common.domain.page.TableSupport;

/**
 * 分页工具类
 */
public class PageUtils extends PageHelper{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = pageDomain.getOrderBy();
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
