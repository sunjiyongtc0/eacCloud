package com.eac.admin.controller;

import com.eac.admin.entity.SysOrg;
import com.eac.admin.service.SysOrgService;
import com.eac.admin.vo.SysOrgVO;
import com.eac.common.core.result.ActionResult;
import com.eac.common.core.result.ListResult;
import com.eac.common.core.result.ResultBuilder;
import com.eac.common.core.utils.ExcelUtils;
import com.eac.common.core.validator.InsertValidator;
import com.eac.common.core.validator.UpdateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <pre>
 * Title :  机构信息的控制层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
@RestController
@RequestMapping("/sysorg")
public class SysOrgController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysOrgService sysOrgService;

	/**
	 * 查询机构分页
	 *
	 * @param sysOrgVO 机构前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysOrg")
	public ListResult<Object> querySysOrg(SysOrgVO sysOrgVO) {
		Map<String, Object> data = sysOrgService.querySysOrg(sysOrgVO.getCurrentPage(), sysOrgVO.getPageSize(), sysOrgVO.getOrgName(), sysOrgVO.getOrgType(),
				sysOrgVO.getOrgDescription());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询机构的树数据
	 *
	 * @return
	 */
	@GetMapping(path = "/querySysOrgTree")
	public ListResult<Object> querySysOrgTree() {
		LinkedHashMap<String, Object> data = sysOrgService.querySysOrgTree();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询机构类型的下拉框数据
	 *
	 * @return
	 */
	@GetMapping(path = "/queryOrgType")
	public ListResult<Object> queryOrgType() {
		LinkedHashMap<String, Object> data = sysOrgService.queryOrgType();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询机构用户的树数据
	 *
	 * @param roleId 角色ID
	 * @param assign 是否分配（0是未分配，1是已分配）
	 * @return
	 */
	@GetMapping(path = "/queryOrgUserTree")
	public ListResult<Object> queryOrgUserTree(@RequestParam(name = "roleId", required = true) Long roleId,
			@RequestParam(name = "assign", required = true) Short assign) {
		List<LinkedHashMap<String, Object>> data = sysOrgService.queryOrgUserTree(roleId, assign);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增机构
	 *
	 * @param sysOrg 机构对象
	 * @return
	 */
	@PostMapping(path = "/addSysOrg")
	public ActionResult addSysOrg(@Validated(InsertValidator.class) @RequestBody SysOrg sysOrg) {
		sysOrgService.insertSysOrg(sysOrg);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑机构
	 *
	 * @param sysOrg 机构对象
	 * @return
	 */
	@PutMapping(path = "/updateSysOrg")
	public ActionResult updateSysOrg(@Validated(UpdateValidator.class) @RequestBody SysOrg sysOrg) {
		sysOrgService.updateSysOrg(sysOrg);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除机构
	 *
	 * @param id 机构ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysOrg")
	public ActionResult deleteSysOrg(@RequestParam(name = "id", required = true) Long[] id) {
		sysOrgService.deleteSysOrg(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出机构
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysOrg")
	public void exportSysOrg(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "机构名称", "机构类型", "机构类型名称", "机构描述", "排序", "上级机构ID", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysOrgService.querySysOrgForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "机构管理", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 根据机构类型查询机构列表
	 *
	 * @param orgType 机构类型
	 * @return
	 */
	@PreAuthorize("#oauth2.hasScope('server')")
	@PostMapping(value = "/querySysOrgList")
	public List<LinkedHashMap<String, Object>> querySysOrgList(@RequestParam(name = "orgType", required = true) String orgType) {
		Map<String, Object> paramMap = new HashMap<>();
		if (orgType != "null") {
			paramMap.put("orgType", orgType);
		}
		return sysOrgService.querySysOrg(paramMap);
	}

}
