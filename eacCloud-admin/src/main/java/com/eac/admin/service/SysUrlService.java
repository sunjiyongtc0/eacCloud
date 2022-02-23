package com.eac.admin.service;

import com.eac.admin.entity.SysUrl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <pre>
 * Title :  接口信息的业务逻辑接口层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-23
 **/
public interface SysUrlService {

	/**
	 * 查询接口分页
	 *
	 * @param currentPage 当前页数
	 * @param pageSize    每页记录数
	 * @param url         URL
	 * @param description 描述
	 * @param menuCode    菜单编码
	 * @param roleId      角色ID
	 * @param sorter      排序
	 * @return
	 */
	Map<String, Object> querySysUrl(Integer currentPage, Integer pageSize, String url, String description, String menuCode, Long[] roleId, String sorter);


	/**
	 * 查询接口的导出数据列表
	 *
	 * @param paramMap 参数Map
	 * @return
	 */
	List<LinkedHashMap<String, Object>> querySysUrlForExcel(Map<String, Object> paramMap);

	/**
	 * 新增接口
	 *
	 * @param sysUrl 接口对象
	 */
	void insertSysUrl(SysUrl sysUrl);

	/**
	 * 编辑接口
	 *
	 * @param sysUrl 接口对象
	 */
	void updateSysUrl(SysUrl sysUrl);

	/**
	 * 删除接口
	 *
	 * @param id 接口ID
	 */
	void deleteSysUrl(Long[] id);

	/**
	 * 将接口授权给角色
	 *
	 * @param urlId  接口ID
	 * @param roleId 角色ID
	 */
	void insertUrlIdRoleId(Long urlId, Long[] roleId);

	/**
	 * 根据接口ID查询对应的角色ID
	 *
	 * @param urlId 接口ID
	 * @return
	 */
	List<String> queryRoleIdByUrlId(Long urlId);

}
