package com.eac.admin.mapper;

import com.eac.admin.entity.SysPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <pre>
 * Title :  岗位信息的数据持久接口层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
@Mapper
public interface SysPostMapper {

	/**
	 * 查询岗位分页或导出数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysPost(Map<String, Object> paramMap);

	/**
	 * 查询岗位信息
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysPostInfo(Map<String, Object> paramMap);

	/**
	 * 查询岗位的树数据列表
	 * 
	 * @param tenantCode 租户编码
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysPostTree(String tenantCode);

	/**
	 * 查询是否已存在此岗位编码
	 * 
	 * @param postCode 岗位编码
	 * @return
	 */
	Integer getSysPostByPostCode(String postCode);

	/**
	 * 新增岗位
	 * 
	 * @param sysPost 岗位对象
	 * @return
	 */
	int insertSysPost(SysPost sysPost);

	/**
	 * 编辑岗位
	 * 
	 * @param sysPost 岗位对象
	 * @return
	 */
	int updateSysPost(SysPost sysPost);

	/**
	 * 删除岗位
	 * 
	 * @param id 岗位ID
	 * @return
	 */
	int deleteSysPost(Long[] id);

}
