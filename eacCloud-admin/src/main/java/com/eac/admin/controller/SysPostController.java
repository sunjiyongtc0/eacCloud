package com.eac.admin.controller;

import com.eac.admin.entity.SysPost;
import com.eac.admin.service.SysPostService;
import com.eac.admin.vo.SysPostVO;
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
 * Title :  岗位信息的控制层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
@RestController
@RequestMapping("/syspost")
public class SysPostController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysPostService sysPostService;

	/**
	 * 查询岗位分页
	 *
	 * @param sysPostVO 岗位前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysPost")
	public ListResult<Object> querySysPost(SysPostVO sysPostVO) {
		Map<String, Object> data = sysPostService.querySysPost(sysPostVO.getCurrentPage(), sysPostVO.getPageSize(), sysPostVO.getPostCode(),
				sysPostVO.getPostName());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询岗位的树数据
	 *
	 * @return
	 */
	@GetMapping(path = "/querySysPostTree")
	public ListResult<Object> querySysPostTree() {
		List<LinkedHashMap<String, Object>> data = sysPostService.querySysPostTree();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增岗位
	 *
	 * @param sysPost 岗位对象
	 * @return
	 */
	@PostMapping(path = "/addSysPost")
	public ActionResult addSysPost(@Validated(InsertValidator.class) @RequestBody SysPost sysPost) {
		sysPostService.insertSysPost(sysPost);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑岗位
	 *
	 * @param sysPost 岗位对象
	 * @return
	 */
	@PutMapping(path = "/updateSysPost")
	public ActionResult updateSysPost(@Validated(UpdateValidator.class) @RequestBody SysPost sysPost) {
		sysPostService.updateSysPost(sysPost);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除岗位
	 *
	 * @param id 岗位ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysPost")
	public ActionResult deleteSysPost(@RequestParam(name = "id", required = true) Long[] id) {
		sysPostService.deleteSysPost(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出岗位
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysPost")
	public void exportSysPost(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "岗位编码", "岗位名称", "岗位排序", "上级岗位ID", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysPostService.querySysPostForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "岗位管理", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

}
