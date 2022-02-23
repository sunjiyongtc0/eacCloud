package com.eac.job.mapper;





import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <pre>
 * Title :  TODO
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2022-02-22
 **/
@Mapper
public interface  TestMapper {
    @Select("select * from sys_org limit 1 ")
    LinkedHashMap<String,Object> findSysOrgDatas();

    @Select("select * from sys_menu ")
    List<LinkedHashMap<String,Object>> findSysMenuDatas();
}
