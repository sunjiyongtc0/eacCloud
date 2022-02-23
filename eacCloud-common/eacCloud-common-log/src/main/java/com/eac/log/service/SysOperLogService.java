package com.eac.log.service;

import com.eac.log.entity.SysOperLog;

/**
 * <pre>
 * Title :  操作日志 服务层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-17
 **/
public interface SysOperLogService {


    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     * @return 结果
     */
    public int insertOperlog(SysOperLog operLog);
}
