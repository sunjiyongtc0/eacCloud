package com.eac.bigdata.service.impl;

import com.eac.bigdata.entity.GwPing;
import com.eac.bigdata.mapper.GwPingMapper;
import com.eac.bigdata.service.GwPingService;
import com.eac.common.datasource.aspect.DataSource;
import com.eac.common.datasource.aspect.DataSourceNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <pre>
 * Title :  TODO
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-21
 **/
@Service
@Transactional
public class GwPingServiceImpl implements GwPingService {

    @Autowired
    private GwPingMapper gwPingMapper;

    @Override
    @DataSource(DataSourceNames.CLICKHOUSE)//这里确定所使用的数据源
    public List<GwPing> findLimt(int num) {
        return gwPingMapper.findLimt(num);
    }

    @Override
    @DataSource(DataSourceNames.CLICKHOUSE)//这里确定所使用的数据源
    public GwPing findById(String id) {
        return gwPingMapper.findById(id);
    }
}
