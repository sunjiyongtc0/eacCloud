package com.eac.log.mapper;

import com.eac.log.entity.SysOperLog;

/**
 * <pre>
 * Title :  操作日志 数据层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-17
 **/
public interface SysOperLogMapper {
    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    public int insertOperlog(SysOperLog operLog);
}
