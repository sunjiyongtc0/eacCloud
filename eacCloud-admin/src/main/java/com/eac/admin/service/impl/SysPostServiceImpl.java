package com.eac.admin.service.impl;

import com.eac.admin.entity.SysPost;
import com.eac.admin.mapper.SysPostMapper;
import com.eac.admin.mapper.SysRoleMapper;
import com.eac.admin.service.SysPostService;
import com.eac.common.core.result.PaginationBuilder;
import com.eac.common.core.utils.CollectionUtils;
import com.eac.common.core.utils.RecursiveListUtils;
import com.eac.common.core.utils.SequenceGenerator;
import com.eac.common.security.utils.CurrentUserUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Map.Entry;

/**
 * <pre>
 * Title :  岗位信息的业务逻辑实现层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
@Service
@Transactional
public class SysPostServiceImpl implements SysPostService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static SequenceGenerator sequenceGenerator = new SequenceGenerator();

	@Autowired
	private SysPostMapper sysPostMapper;
	@Autowired
	private SysRoleMapper sysRoleMapper;

	/**
	 * 查询岗位的数据分页
	 */
	@Override
	public Map<String, Object> querySysPost(Integer currentPage, Integer pageSize, String postCode, String postName) {
		List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("postCode", postCode);
		paramMap.put("postName", postName);
		paramMap.put("tenantCode", CurrentUserUtils.getOAuth2AuthenticationDetailsInfo().get("tenantCode"));// 当前用户的租户编码

		List<LinkedHashMap<String, Object>> totalList = sysPostMapper.querySysPost(paramMap);
		String roleData = sysRoleMapper.queryRoleData("syspost", CurrentUserUtils.getOAuth2AuthenticationDetailsInfo().get("name"));
		String[] roleDataArray = roleData == null ? null : roleData.split(",");
		if (roleDataArray != null && roleDataArray.length > 0) {// 处理数据权限
			totalList = CollectionUtils.convertFilterList(totalList, roleDataArray);
		}

		if (StringUtils.isBlank(postCode) && StringUtils.isBlank(postName)) {
			resultList = RecursiveListUtils.queryRecursiveList(totalList);
		} else {
			resultList.addAll(totalList);
		}
		return PaginationBuilder.buildResult(resultList, (long) resultList.size(), currentPage, pageSize);
	}

	/**
	 * 查询岗位的树数据
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysPostTree() {
		List<LinkedHashMap<String, Object>> totalList = sysPostMapper.querySysPostTree(CurrentUserUtils.getOAuth2AuthenticationDetailsInfo().get("tenantCode"));// 当前用户的租户编码
		return RecursiveListUtils.queryRecursiveList(totalList);
	}

	/**
	 * 查询岗位的导出数据列表
	 */
	@Override
	public List<LinkedHashMap<String, Object>> querySysPostForExcel(Map<String, Object> paramMap) {
		return sysPostMapper.querySysPost(paramMap);
	}

	/**
	 * 新增岗位
	 */
	@Override
	public void insertSysPost(SysPost sysPost) {
		Integer existingPostCode = sysPostMapper.getSysPostByPostCode(sysPost.getPostCode());
		if (existingPostCode != null && existingPostCode > 0) {
			throw new IllegalArgumentException("岗位编码已存在");
		}
		sysPost.setId(sequenceGenerator.nextId());
		sysPost.setTenantCode(CurrentUserUtils.getOAuth2AuthenticationDetailsInfo().get("tenantCode"));// 当前用户的租户编码
		sysPostMapper.insertSysPost(sysPost);

		logger.info("岗位已新增： {}", sysPost.getPostCode());
	}

	/**
	 * 编辑岗位
	 */
	@Override
	public void updateSysPost(SysPost sysPost) {
		if (sysPost.getId().longValue() == sysPost.getParentId().longValue()) {
			throw new IllegalArgumentException("当前节点不能作为自身的父节点");
		}
		sysPostMapper.updateSysPost(sysPost);

		logger.info("岗位已编辑： {}", sysPost.getPostCode());
	}

	/**
	 * 删除岗位
	 */
	@Override
	public void deleteSysPost(Long[] id) {
		Set<Long> ids = new HashSet<>();
		for (int i = 0; i < id.length; i++) {
			ids.add(id[i]);
			getRecursiveIds(id[i], ids);
		}
		sysPostMapper.deleteSysPost(ids.stream().toArray(Long[]::new));
	}

	/**
	 * 使用递归的方式查询所有子节点的id
	 *
	 * @param id  子节点id
	 * @param ids 子节点id集
	 */
	private void getRecursiveIds(Long id, Set<Long> ids) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("parentId", id);
		List<LinkedHashMap<String, Object>> list = sysPostMapper.querySysPost(paramMap);
		for (int i = 0; i < list.size(); i++) {
			for (Entry<String, Object> entry : list.get(i).entrySet()) {
				String key = entry.getKey();
				Object value = entry.getValue();
				if (key.equals("id")) {
					ids.add(Long.valueOf(String.valueOf(value)));
					getRecursiveIds(Long.valueOf(String.valueOf(value)), ids);
				}
			}
		}
	}

}
