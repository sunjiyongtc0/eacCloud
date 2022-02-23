package com.eac.bigdata.controller;

import com.eac.api.system.admin.SysRoleServiceClient;
import com.eac.api.system.admin.SysUserServiceClient;
import com.eac.bigdata.service.CPinfoService;
import com.eac.bigdata.service.GwPingService;
import com.eac.common.core.result.ActionResult;
import com.eac.common.core.result.ResultBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 * Title :  TODO
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-17
 **/
@RestController
@RequestMapping("/show")
public class ShowController {

    @Autowired
    private CPinfoService cpInfoService;

    @Autowired
    private GwPingService gwPingService;

    @Autowired
    private SysUserServiceClient sysUserServiceClient;

    @GetMapping(path = "/cp")
    public ActionResult tt(){
        System.out.println("crate---->"+cpInfoService.findProbeInfoById("7cd2113c16f05e1f0849e709a2424bab"));
        System.out.println(sysUserServiceClient.querySysUserList(920482363759607808l));
        return ResultBuilder.buildActionSuccess();
    }

    @GetMapping(path = "/ck")
    public ActionResult ck(){
        System.out.println(gwPingService.findLimt(3));
        System.out.println(gwPingService.findById("Gf3SlVIP"));
        return ResultBuilder.buildActionSuccess();
    }
}
