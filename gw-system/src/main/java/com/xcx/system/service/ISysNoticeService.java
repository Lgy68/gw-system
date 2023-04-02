package com.xcx.system.service;


import com.xcx.common.domain.entiy.SysNotice;

import java.util.List;

/**
 * 公告
 */
public interface ISysNoticeService {

    List<SysNotice> selectNoticeList(SysNotice notice);

    SysNotice selectNoticeById(Long noticeId);

    int insertNotice(SysNotice notice);

    int updateNotice(SysNotice notice);

    int deleteNoticeByIds(Long[] noticeIds);

}
