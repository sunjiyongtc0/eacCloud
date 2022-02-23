package com.eac.bigdata.service.impl;

import com.eac.api.system.admin.SysUserServiceClient;
import com.eac.bigdata.entity.CPPinfo;
import com.eac.bigdata.mapper.CPPinfoMapper;
import com.eac.bigdata.service.CPinfoService;
import com.eac.common.datasource.aspect.DataSource;
import com.eac.common.datasource.aspect.DataSourceNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <pre>
 * Title :  TODO
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-17
 **/
@Service
@Transactional
public class CPinfoServiceImpl  implements CPinfoService {


    @Autowired
    private  CPPinfoMapper cpPinfoMapper;



    @Override
    @DataSource(DataSourceNames.CRATEDB)//这里确定所使用的数据源
    public CPPinfo findProbeInfoById(String id) {
        return cpPinfoMapper.selectByProbeId(id);
    }
}
