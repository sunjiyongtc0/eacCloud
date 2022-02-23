package com.eac.auth.operation;

import com.eac.auth.entity.SysLogininfor;
import com.eac.auth.service.SysLogininforService;
import com.eac.common.core.utils.IpUtils;
import com.eac.common.core.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * Title :  TODO
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-16
 **/
@Component
public class SaveLogininfor {

    @Autowired
    private SysLogininforService sysLogininforService;

    public void recordLogininfor(String username, String status, String message)
    {
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        logininfor.setMsg(message);
        logininfor.setStatus(status);
        sysLogininforService.insertLogininfor(logininfor);
    }

}
