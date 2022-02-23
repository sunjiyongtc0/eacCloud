package com.eac.admin.mapper;

import com.eac.admin.entity.SysUserPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Title :  用户与岗位关联信息的数据持久接口层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
@Mapper
public interface SysUserPostMapper {

	/**
	 * 查询用户与岗位关联信息分页或导出数据
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUserPost(Map<String, Object> paramMap);

	/**
	 * 查询用户与岗位关联信息的下拉框数据
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUserPostTree(String userId);

	/**
	 * 校验用户与岗位关联信息
	 * 
	 * @param id       用户与岗位关联ID
	 * @param userId   用户ID
	 * @param postType 岗位类型
	 * @return
	 */
	Integer getSysUserPostInfo(Long id, Long userId, Short postType);

	/**
	 * 新增用户与岗位关联信息
	 * 
	 * @param sysUserPost 用户与岗位关联对象
	 * @return
	 */
	int insertSysUserPost(SysUserPost sysUserPost);

	/**
	 * 编辑用户与岗位关联信息
	 * 
	 * @param sysUserPost 用户与岗位关联对象
	 * @return
	 */
	int updateSysUserPost(SysUserPost sysUserPost);

	/**
	 * 删除用户与岗位关联信息
	 * 
	 * @param id 用户与岗位关联ID
	 * @return
	 */
	int deleteSysUserPost(Long[] id);

}
