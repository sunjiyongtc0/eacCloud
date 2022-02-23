package com.eac.api.system.account;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 * Title :  TODO
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-12-08
 **/
@FeignClient(name = "eac-base-account", path = "/account/sysuserdetail", fallback = SysUserDetailServiceCleintFallback.class)
public interface SysUserDetailServiceCleint {

    @PostMapping(path = "/queryCurrentAuthority")
    String queryCurrentAuthority(@RequestParam(name = "username", required = false) String username,
                                 @RequestParam(name = "mobile", required = false) String mobile, @RequestParam(name = "parentId", required = false) String parentId,
                                 @RequestParam(name = "postCode", required = false) String postCode, @RequestParam(name = "userId", required = false) Long userId);

}
