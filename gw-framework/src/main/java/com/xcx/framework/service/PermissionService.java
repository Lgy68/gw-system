package com.xcx.framework.service;

import com.xcx.common.domain.LoginUser;
import com.xcx.common.utils.SecurityUtils;
import com.xcx.common.utils.StringUtils;
import com.xcx.framework.config.PermissionContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 自定义权限实现
 */
@Service("ss")
public class PermissionService {

    /**所有权限标识*/
    private static final String ALL_PERMISSION = "*:*:*";

    /** 管理员角色权限标识 */
    private static final String SUPER_ADMIN = "admin";

    private static final String ROLE_DELIMETER = ",";

    private static final String PERMISSION_DELIMETER = ",";

    /*
     * 查询用户是否有某权限
     */
    public boolean hasPermi(String permission){
        if (StringUtils.isEmpty(permission)) {
            return false;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNull(loginUser) || StringUtils.isEmpty(loginUser.getPermissions())){
            return false;
        }
        PermissionContextHolder.setContext(permission);
        return hasPermissions(loginUser.getPermissions(),permission);
    }

    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }
}
