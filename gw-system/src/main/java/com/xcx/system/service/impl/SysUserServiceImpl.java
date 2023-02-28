package com.xcx.system.service.impl;

import com.xcx.common.constant.UserConstants;
import com.xcx.common.domain.entiy.SysUser;
import com.xcx.common.utils.StringUtils;
import com.xcx.system.mapper.SysUserMapper;
import com.xcx.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser selectUserByUserName(String username) {
        return sysUserMapper.selectUserByUserName(username);
    }

    @Override
    public int updateUserProfile(SysUser sysUser) {
        return sysUserMapper.updateUserProfile(sysUser);
    }

    @Override
    public String checkUserNameUnique(String username) {
        int count = sysUserMapper.checkUserNameUnique(username);
        if (count > 0){
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public boolean insertUser(SysUser user) {
        return sysUserMapper.insertUser(user) > 0;
    }
}
