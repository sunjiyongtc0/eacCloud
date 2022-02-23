package com.eac.api.system.account;

import org.springframework.stereotype.Component;

/**
 * <pre>
 * Title :  获取当前用户详情
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-12-08
 **/
@Component
public class SysUserDetailServiceCleintFallback implements SysUserDetailServiceCleint {

    @Override
    public String queryCurrentAuthority(String username, String mobile, String parentId, String postCode, Long userId) {
        return null;
    }


}
