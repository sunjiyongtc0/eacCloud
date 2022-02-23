package com.eac.bigdata.service;

import com.eac.bigdata.entity.GwPing;

import java.util.List;

/**
 * <pre>
 * Title :  TODO
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-17
 **/
public interface GwPingService {

    List<GwPing> findLimt(int num);

    GwPing findById(String id);
}
