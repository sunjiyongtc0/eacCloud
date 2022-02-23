package com.eac.job.service.impl;

import com.eac.job.mapper.TestMapper;
import com.eac.job.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <pre>
 * Title :  TODO
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-22
 **/
@Service
public class TestServiceImpl  implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public Map<String, Object> findDatas() {
        System.out.println(testMapper.findSysMenuDatas());
        System.out.println(testMapper.findSysOrgDatas());
        return null;
    }
}
