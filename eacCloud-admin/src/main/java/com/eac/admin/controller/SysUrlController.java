package com.eac.admin.controller;

import com.eac.admin.entity.SysUrl;
import com.eac.admin.service.SysUrlService;
import com.eac.admin.vo.SysUrlVO;
import com.eac.common.core.result.ActionResult;
import com.eac.common.core.result.ListResult;
import com.eac.common.core.result.ResultBuilder;
import com.eac.common.core.utils.ExcelUtils;
import com.eac.common.core.validator.InsertValidator;
import com.eac.common.core.validator.UpdateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Title :  接口信息的控制层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-23
 **/
@RestController
@RequestMapping("/sysurl")
public class SysUrlController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysUrlService sysUrlService;

	/**
	 * 查询接口分页
	 *
	 * @param sysUrlVO 接口前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysUrl")
	public ListResult<Object> querySysUrl(SysUrlVO sysUrlVO) {
		Map<String, Object> data = sysUrlService.querySysUrl(sysUrlVO.getCurrentPage(), sysUrlVO.getPageSize(), sysUrlVO.getUrl(), sysUrlVO.getDescription(),
				sysUrlVO.getMenuCode(), sysUrlVO.getRoleId(), sysUrlVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增接口
	 *
	 * @param sysUrl 接口对象
	 * @return
	 */
	@PostMapping(path = "/addSysUrl")
	public ActionResult addSysUrl(@Validated(InsertValidator.class) @RequestBody SysUrl sysUrl) {
		sysUrlService.insertSysUrl(sysUrl);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑接口
	 *
	 * @param sysUrl 接口对象
	 * @return
	 */
	@PutMapping(path = "/updateSysUrl")
	public ActionResult updateSysUrl(@Validated(UpdateValidator.class) @RequestBody SysUrl sysUrl) {
		sysUrlService.updateSysUrl(sysUrl);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除接口
	 *
	 * @param id 接口ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysUrl")
	public ActionResult deleteSysUrl(@RequestParam(name = "id", required = true) Long[] id) {
		sysUrlService.deleteSysUrl(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出接口
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysUrl")
	public void exportSysUrl(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "URL", "描述", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysUrlService.querySysUrlForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "接口权限", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 将接口授权给角色
	 *
	 * @param urlId  接口ID
	 * @param roleId 角色ID
	 * @return
	 */
	@PostMapping(path = "/authorizeRoleToUrl")
	public ActionResult authorizeRoleToUrl(@RequestParam(name = "urlId", required = true) Long urlId,
			@RequestParam(name = "roleId", required = true) Long[] roleId) {
		sysUrlService.insertUrlIdRoleId(urlId, roleId);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据接口ID查询对应的角色ID
	 *
	 * @param urlId 接口ID
	 * @return
	 */
	@GetMapping(path = "/queryRoleIdByUrlId")
	public ListResult<Object> queryRoleIdByUrlId(@RequestParam(name = "urlId", required = true) Long urlId) {
		List<String> data = sysUrlService.queryRoleIdByUrlId(urlId);
		return ResultBuilder.buildListSuccess(data);
	}

}
