package com.eac.log.service.impl;

import com.eac.log.entity.SysOperLog;
import com.eac.log.mapper.SysOperLogMapper;
import com.eac.log.service.SysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * Title :  操作日志 服务层处理
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-17
 **/
@Service
public class SysOperLogServiceImpl  implements SysOperLogService {

    @Autowired
    private SysOperLogMapper operLogMapper;


    @Override
    public int insertOperlog(SysOperLog operLog) {
            return operLogMapper.insertOperlog(operLog);
    }
}
