package com.eac.admin.controller;


import com.eac.admin.entity.SysRole;
import com.eac.admin.service.SysRoleService;
import com.eac.admin.vo.SysRoleVO;
import com.eac.common.core.result.ActionResult;
import com.eac.common.core.result.ListResult;
import com.eac.common.core.result.ResultBuilder;
import com.eac.common.core.utils.ExcelUtils;
import com.eac.common.core.validator.InsertValidator;
import com.eac.common.core.validator.UpdateValidator;
import com.eac.common.log.annotation.Log;
import com.eac.common.log.enums.BusinessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <pre>
 * Title :  角色信息的控制层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
@RestController
@RequestMapping("/sysrole")
public class SysRoleController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysRoleService sysRoleService;

	/**
	 * 查询角色分页
	 *
	 * @param sysRoleVO 角色前端参数
	 * @return
	 */
	@Log(title = "角色管理", businessType = BusinessType.OTHER)
	@GetMapping(path = "/querySysRole")
	public ListResult<Object> querySysRole(SysRoleVO sysRoleVO) {
		Map<String, Object> data = sysRoleService.querySysRole(sysRoleVO.getCurrentPage(), sysRoleVO.getPageSize(), sysRoleVO.getRoleName(),
				sysRoleVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询角色名称的下拉框数据
	 *
	 * @param userId   用户ID
	 * @param postCode 岗位编码
	 * @param assign   是否授权（0是未授权，1是已授权）
	 * @return
	 */
	@GetMapping(path = "/queryRoleName")
	public ListResult<Object> queryRoleName(@RequestParam(name = "userId", required = false) Long userId,
                                            @RequestParam(name = "postCode", required = false) String postCode, @RequestParam(name = "assign", required = false) Short assign) {
		List<LinkedHashMap<String, Object>> data = sysRoleService.queryRoleName(userId, postCode, assign);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询角色编码的下拉框数据
	 *
	 * @return
	 */
	@GetMapping(path = "/queryRoleCode")
	public ListResult<Object> queryRoleCode() {
		List<LinkedHashMap<String, Object>> data = sysRoleService.queryRoleCode();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询角色的多选框数据
	 *
	 * @return
	 */
	@GetMapping(path = "/queryRoleNameCheckbox")
	public ListResult<Object> queryRoleNameCheckbox() {
		List<LinkedHashMap<String, Object>> data = sysRoleService.queryRoleNameCheckbox();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增角色
	 *
	 * @param sysRole 角色对象
	 * @return
	 */
	@PostMapping(path = "/addSysRole")
	public ActionResult addSysRole(@Validated(InsertValidator.class) @RequestBody SysRole sysRole) {
		sysRoleService.insertSysRole(sysRole);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 将对应的用户授权给角色
	 *
	 * @param sysRoleVO 角色前端参数
	 * @return
	 */
	@PostMapping(path = "/authorizeUserToRole")
	public ActionResult authorizeUserToRole(@Validated(InsertValidator.class) @RequestBody SysRoleVO sysRoleVO) {
		sysRoleService.insertRoleIdUserId(sysRoleVO.getRoleId(), sysRoleVO.getUserId());
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑角色
	 *
	 * @param sysRole 角色对象
	 * @return
	 */
	@PutMapping(path = "/updateSysRole")
	public ActionResult updateSysRole(@Validated(UpdateValidator.class) @RequestBody SysRole sysRole) {
		sysRoleService.updateSysRole(sysRole);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除角色
	 *
	 * @param id 角色ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysRole")
	public ActionResult deleteSysRole(@RequestParam(name = "id", required = true) Long[] id) {
		sysRoleService.deleteSysRole(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出角色
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysRole")
	public void exportSysRole(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "角色编码", "角色名称", "角色描述", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysRoleService.querySysRoleForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "角色管理", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 查询当前用户的过滤数据字段
	 *
	 * @param menuCode 菜单编码
	 * @param username 用户名
	 * @return
	 */
//	@PreAuthorize("#oauth2.hasScope('server')")
	@GetMapping(value = "/queryRoleData")
	public String queryRoleData(@RequestParam(name = "menuCode", required = true) String menuCode,
			@RequestParam(name = "username", required = true) String username) {
		return sysRoleService.queryRoleData(menuCode, username);
	}

}
