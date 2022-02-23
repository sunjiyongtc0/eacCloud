package com.eac.auth.controller;

import com.eac.auth.service.AuthService;
import com.eac.common.core.result.ListResult;
import com.eac.common.core.result.ResultBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;

/**
 * <pre>
 * Title : 用户认证授权信息的控制层
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-18
 **/
@RestController
@RequestMapping("/users")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 查询当前用户信息
     */
    @GetMapping(path = "/getSysUser")
    public ListResult<Object> getSysUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LinkedHashMap<String, Object> data = authService.getSysUser(authentication.getName());
        return ResultBuilder.buildListSuccess(data);
    }

    @GetMapping(value = "/current")
    public Principal getSysUser(Principal principal) {
        return principal;
    }

}
