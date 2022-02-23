package com.eac.admin.controller;

import com.eac.admin.entity.SysDict;
import com.eac.admin.service.SysDictService;
import com.eac.admin.vo.SysDictVO;
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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Title :  字典信息的控制层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
@RestController
@RequestMapping("/sysdict")
public class SysDictController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysDictService sysDictService;

	/**
	 * 查询字典分页
	 *
	 * @param sysDictVO 字典前端参数
	 * @return
	 */
	@GetMapping(path = "/querySysDict")
	public ListResult<Object> querySysDict(SysDictVO sysDictVO) {
		Map<String, Object> data = sysDictService.querySysDict(sysDictVO.getCurrentPage(), sysDictVO.getPageSize(), sysDictVO.getDictType(),
				sysDictVO.getDictName(), sysDictVO.getDictValue());
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 查询字典的树数据
	 *
	 * @return
	 */
	@GetMapping(path = "/querySysDictTree")
	public ListResult<Object> querySysDictTree() {
		LinkedHashMap<String, Object> data = sysDictService.querySysDictTree();
		return ResultBuilder.buildListSuccess(data);
	}

	/**
	 * 新增字典
	 *
	 * @param sysDict 字典对象
	 * @return
	 */
	@PostMapping(path = "/addSysDict")
	public ActionResult addSysDict(@Validated(InsertValidator.class) @RequestBody SysDict sysDict) {
		sysDictService.insertSysDict(sysDict);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 编辑字典
	 *
	 * @param sysDict 字典对象
	 * @return
	 */
	@PutMapping(path = "/updateSysDict")
	public ActionResult updateSysDict(@Validated(UpdateValidator.class) @RequestBody SysDict sysDict) {
		sysDictService.updateSysDict(sysDict);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 删除字典
	 *
	 * @param id 字典ID
	 * @return
	 */
	@PostMapping(path = "/deleteSysDict")
	public ActionResult deleteSysDict(@RequestParam(name = "id", required = true) Long[] id) {
		sysDictService.deleteSysDict(id);
		return ResultBuilder.buildActionSuccess();
	}

	/**
	 * 根据查询条件导出字典
	 *
	 * @param response 响应对象
	 * @param paramMap 参数Map
	 */
	@PostMapping(path = "/exportSysDict")
	public void exportSysDict(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
		try {
			List<String> headList = Arrays.asList("ID", "字典名称", "字典值", "字典类型", "排序", "上级字典ID", "创建时间");
			List<LinkedHashMap<String, Object>> dataList = sysDictService.querySysDictForExcel(paramMap);
			ExcelUtils.exportExcel(headList, dataList, "字典管理", response);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
	}

	/**
	 * 根据字典类型查询下拉框数据列表
	 *
	 * @param dictType 字典类型
	 * @return
	 */
	@PreAuthorize("#oauth2.hasScope('server')")
	@GetMapping(value = "/queryDictByDictType")
	public List<LinkedHashMap<String, Object>> queryDictByDictType(@RequestParam(name = "dictType", required = true) String dictType) {
		return sysDictService.queryDictByDictType(dictType);
	}

}
