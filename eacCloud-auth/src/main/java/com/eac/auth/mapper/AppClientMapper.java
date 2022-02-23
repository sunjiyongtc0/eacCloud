package com.eac.auth.mapper;


import com.eac.auth.entity.AppClient;
import org.apache.ibatis.annotations.Mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Title : 应用信息的数据持久接口层
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-18
 **/
@Mapper
public interface AppClientMapper {

    /**
     * 查询应用分页或导出数据
     *
     * @param paramMap 参数Map
     */
    List<LinkedHashMap<String, Object>> queryAppClient(Map<String, Object> paramMap);

    /**
     * 查询是否已存在此应用编码
     *
     * @param id         应用ID
     * @param clientCode 应用编码
     */
    Integer getAppClientByClientCode(Long id, String clientCode);

    /**
     * 新增应用
     *
     * @param appClient 应用对象
     */
    int insertAppClient(AppClient appClient);

    /**
     * 编辑应用
     *
     * @param appClient 应用对象
     */
    int updateAppClient(AppClient appClient);

    /**
     * 删除应用
     *
     * @param id 应用ID
     */
    int deleteAppClient(Long[] id);

}
