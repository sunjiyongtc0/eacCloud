package com.eac.admin.controller;

import com.eac.admin.entity.SysUserPost;
import com.eac.admin.service.SysUserPostService;
import com.eac.admin.vo.SysUserPostVO;
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
 * Title :  用户与岗位关联信息的控制层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
@RestController
@RequestMapping("/sysuserpost")
public class SysUserPostController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysUserPostService sysUserPostService;

	/**
	 * 查询用户与岗位关联信息分页
	 *
	 * @param sysUserPostVO 用户与岗位关联信息前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysUserPost")
	public ListResult<Object> querySysUserPost(SysUserPostVO sysUserPostVO) {
		Map<String, Object> data = sysUserPostService.querySysUserPost(sysUserPostVO.getCurrentPage(), sysUserPostVO.getPageSize(), sysUserPostVO.getUserId(),
				sysUserPostVO.getPostCode(), sysUserPostVO.getPostName(), sysUserPostVO.getSorter());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询用户与岗位关联信息的下拉框数据
	 *
	 * @param userId 用户ID
	 * @return
	 */
	@GetMapping(path = "/querySysUserPostTree")
	public ListResult<Object> querySysUserPostTree(@RequestParam(name = "userId", required = true) String userId) {
		List<LinkedHashMap<String, Object>> data = sysUserPostService.querySysUserPostTree(userId);
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增用户与岗位关联信息
	 *
	 * @param sysUserPost 用户与岗位关联对象
	 * @return
	 */
	@PostMapping(path = "/addSysUserPost")
	public ActionResult addSysUserPost(@Validated(InsertValidator.class) @RequestBody SysUserPost sysUserPost) {
		sysUserPostService.insertSysUserPost(sysUserPost);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑用户与岗位关联信息
	 *
	 * @param sysUserPost 用户与岗位关联对象
	 * @return
	 */
	@PutMapping(path = "/updateSysUserPost")
	public ActionResult updateSysUserPost(@Validated(UpdateValidator.class) @RequestBody SysUserPost sysUserPost) {
		sysUserPostService.updateSysUserPost(sysUserPost);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除用户与岗位关联信息
	 *
	 * @param id 用户与岗位关联ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysUserPost")
	public ActionResult deleteSysUserPost(@RequestParam(name = "id", required = true) Long[] id) {
		sysUserPostService.deleteSysUserPost(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出用户与岗位关联信息
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysUserPost")
	public void exportSysUserPost(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "用户ID", "岗位编码", "岗位上下级名称", "岗位描述", "岗位类型", "状态", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysUserPostService.querySysUserPostForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "用户与岗位关联信息", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
