package com.eac.auth.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <pre>
 * Title : 用户认证授权信息的数据持久接口层
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-18
 **/
@Mapper
public interface AuthMapper {

    /**
     * 根据用户名查询当前用户信息
     *
     * @param username   用户名
     * @param tenantCode 当前用户的租户编码
     */
    LinkedHashMap<String, Object> getSysUser(String username, String tenantCode);

    /**
     * 根据用户名查询用户详细信息
     *
     * @param username 用户名
     */
    LinkedHashMap<String, String> getSysUserByUsername(String username);

    /**
     * 根据手机号查询用户详细信息
     *
     * @param mobile 手机号
     */
    LinkedHashMap<String, String> getSysUserByMobile(String mobile);

    /**
     * 根据用户名查询角色编码
     *
     * @param username 用户名
     */
    List<String> queryRoleCodeByUsername(String username);

    /**
     * 根据手机号查询角色编码
     *
     * @param mobile 手机号
     */
    List<String> queryRoleCodeByMobile(String mobile);

}
