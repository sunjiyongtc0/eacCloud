package com.eac.auth.service;

import java.util.LinkedHashMap;

/**
 * <pre>
 * Title : 用户认证授权信息的业务逻辑接口层
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-18
 **/
public interface AuthService {

    /**
     * 根据用户名查询当前用户信息
     *
     * @param username 用户名
     */
    LinkedHashMap<String, Object> getSysUser(String username);

}
