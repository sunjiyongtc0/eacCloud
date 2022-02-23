package com.eac.admin;

import com.eac.admin.mapper.SysRoleMapper;
import com.eac.admin.mapper.SysUrlMapper;
import com.eac.common.redis.constants.ApplicationConstants;
import com.eac.common.redis.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 * Title : 初始化数据
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-26
 **/
@Order
@Component
public class BuildApplication {
        //implements CommandLineRunner {

//    private final SysUrlMapper sysUrlMapper;
//
//    private final SysRoleMapper sysRoleMapper;
//
//    private final RedisUtils redisUtils;
//
//    @Autowired
//    public BuildApplication(SysUrlMapper sysUrlMapper, SysRoleMapper sysRoleMapper, RedisUtils redisUtils) {
//        this.sysUrlMapper = sysUrlMapper;
//        this.sysRoleMapper = sysRoleMapper;
//        this.redisUtils = redisUtils;
//    }
//
//    /**
//     * 初始化角色编码对应的URL授权数据到Redis缓存，供网关验证权限
//     */
//    @Override
//    public void run(String... args) {
//        List<String> roleCodeList = sysRoleMapper.queryRoleCodeList();
//        for (String roleCode : roleCodeList) {
//            List<String> url = sysUrlMapper.queryRoleUrl(roleCode);
//            redisUtils.psetex(ApplicationConstants.URL_ROLECODE_PREFIX + roleCode, url == null ? Collections.emptyList().toString() : url.toString());
//        }
//    }
}
