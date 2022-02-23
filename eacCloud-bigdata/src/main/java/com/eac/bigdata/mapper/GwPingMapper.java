package com.eac.bigdata.mapper;


import com.eac.bigdata.entity.GwPing;

import java.util.List;

public interface GwPingMapper {
    int insert(GwPing gwPing);

    int insertSelective(GwPing gwPing);

    List<GwPing> findLimt(int num);

    GwPing findById(String id);

}
