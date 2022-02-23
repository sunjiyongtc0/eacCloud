package com.eac.admin.service;

import com.eac.admin.entity.SysUserPost;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Title :  用户与岗位关联信息的业务逻辑接口层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
public interface SysUserPostService {
	/**
	 * 查询用户与岗位关联信息的数据分页
	 * 
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param userId      用户ID
	 * @param postCode    岗位编码
	 * @param postName    岗位名称
	 * @param sorter      排序
	 * @return
	 */
	Map<String, Object> querySysUserPost(Integer currentPage, Integer pageSize, Long userId, String postCode, String postName, String sorter);

	/**
	 * 查询用户与岗位关联信息的下拉框数据
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUserPostTree(String userId);

	/**
	 * 查询用户与岗位关联信息的导出数据列表
	 * 
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUserPostForExcel(Map<String, Object> paramMap);

	/**
	 * 新增用户与岗位关联信息
	 * 
	 * @param sysUserPost 用户与岗位关联对象
	 */
	void insertSysUserPost(SysUserPost sysUserPost);

	/**
	 * 编辑用户与岗位关联信息
	 * 
	 * @param sysUserPost 用户与岗位关联对象
	 */
	void updateSysUserPost(SysUserPost sysUserPost);

	/**
	 * 删除用户与岗位关联信息
	 * 
	 * @param id 用户与岗位关联ID
	 */
	void deleteSysUserPost(Long[] id);

}
