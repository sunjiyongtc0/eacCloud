package com.eac.auth.service.impl;

import com.eac.auth.mapper.AuthMapper;
import com.eac.auth.operation.SaveLogininfor;
import com.eac.auth.service.AuthService;
import com.eac.common.security.provider.CurrentUser;
import com.eac.common.security.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * <pre>
 * Title : 用户认证授权信息的业务逻辑实现层
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-18
 **/
@Service
@Transactional
public class AuthServiceImpl implements AuthService, UserDetailsService {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private SaveLogininfor saveLogininfor;
    /**
     * 根据用户名查询当前用户信息
     */
    @Override
    public LinkedHashMap<String, Object> getSysUser(String username) {
        String tenantCode = CurrentUserUtils.getOAuth2AuthenticationInfo().get("tenantCode");
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> sysUserMap = authMapper.getSysUser(username, tenantCode);
        if (sysUserMap == null) {
            throw new UsernameNotFoundException(String.format("找不到此租户里的用户名%s。", username));
        }
        Iterator<Entry<String, Object>> iterator = sysUserMap.entrySet().iterator();
        LinkedHashMap<String, Object> regionMap = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if ("provinceRegionCode".equals(key) || "provinceRegionName".equals(key) || "cityRegionCode".equals(key) || "cityRegionName".equals(key)) {
                regionMap.put(key, value);
                continue;
            }
            if ("roles".equals(key)) {
                resultMap.put(key, String.valueOf(value).split(","));
            } else {
                resultMap.put(key, value);
            }
        }
        resultMap.put("geographic", getGeographic(regionMap));
        return resultMap;
    }

    /**
     * 根据区域参数转为界面区域信息
     *
     * @param regionMap 区域参数
     */
    private LinkedHashMap<String, Object> getGeographic(LinkedHashMap<String, Object> regionMap) {
        LinkedHashMap<String, Object> geographicMap = new LinkedHashMap<>();
        LinkedHashMap<String, Object> provinceMap = new LinkedHashMap<>();
        provinceMap.put("key", regionMap.get("provinceRegionCode"));
        provinceMap.put("label", regionMap.get("provinceRegionName"));
        LinkedHashMap<String, Object> cityMap = new LinkedHashMap<>();
        cityMap.put("key", regionMap.get("cityRegionCode"));
        cityMap.put("label", regionMap.get("cityRegionName"));
        geographicMap.put("province", provinceMap);
        geographicMap.put("city", cityMap);
        return geographicMap;
    }

    /**
     * 根据用户名查询用户详细信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        LinkedHashMap<String, String> user = authMapper.getSysUserByUsername(username);
        if (user == null) {
            saveLogininfor.recordLogininfor(username,"1","找不到此用户名");
            throw new UsernameNotFoundException(String.format("找不到此用户名%s。", username));
        }
        List<String> roleCodeList = authMapper.queryRoleCodeByUsername(username);
        saveLogininfor.recordLogininfor(username,"0","登录成功");
        return new CurrentUser(username, user.get("PASSWORD"), AuthorityUtils.createAuthorityList(roleCodeList.toArray(new String[]{})),user);
    }

}
