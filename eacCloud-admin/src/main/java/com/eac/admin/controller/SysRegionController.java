package com.eac.admin.controller;

import com.eac.admin.entity.SysRegion;
import com.eac.admin.service.SysRegionService;
import com.eac.admin.vo.SysRegionVO;
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
 * Title :  区域信息的控制层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-12-15
 **/
@RestController
@RequestMapping("/sysregion")
public class SysRegionController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysRegionService sysRegionService;

    /**
     * 查询区域分页
     *
     * @param sysRegionVO 区域前端参数
     * @return
     */
    @GetMapping(path = "/querySysRegion")
    public ListResult<Object> querySysRegion(SysRegionVO sysRegionVO) {
        Map<String, Object> data = sysRegionService.querySysRegion(sysRegionVO.getCurrentPage(), sysRegionVO.getPageSize(), sysRegionVO.getRegionName(),
                sysRegionVO.getRegionCode(), sysRegionVO.getRegionType());
        return ResultBuilder.buildListSuccess(data);
    }

    /**
     * 查询区域的树数据
     *
     * @return
     */
    @GetMapping(path = "/querySysRegionTree")
    public ListResult<Object> querySysRegionTree() {
        LinkedHashMap<String, Object> data = sysRegionService.querySysRegionTree();
        return ResultBuilder.buildListSuccess(data);
    }

    /**
     * 查询区域类型的下拉框数据
     *
     * @return
     */
    @GetMapping(path = "/queryRegionType")
    public ListResult<Object> queryRegionType() {
        LinkedHashMap<String, Object> data = sysRegionService.queryRegionType();
        return ResultBuilder.buildListSuccess(data);
    }

    /**
     * 查询全部省份数据
     *
     * @return
     */
    @GetMapping(path = "/queryProvince")
    public ListResult<Object> queryProvince() {
        LinkedHashMap<String, Object> data = sysRegionService.queryProvince();
        return ResultBuilder.buildListSuccess(data);
    }

    /**
     * 根据省份代码查询对应地市数据
     *
     * @return
     */
    @GetMapping(path = "/queryCity/{province}")
    public ListResult<Object> queryCity(@PathVariable String province) {
        LinkedHashMap<String, Object> data = sysRegionService.queryCity(province);
        return ResultBuilder.buildListSuccess(data);
    }

    /**
     * 新增区域
     *
     * @param sysRegion 区域对象
     * @return
     */
    @PostMapping(path = "/addSysRegion")
    public ActionResult addSysRegion(@Validated(InsertValidator.class) @RequestBody SysRegion sysRegion) {
        sysRegionService.insertSysRegion(sysRegion);
        return ResultBuilder.buildActionSuccess();
    }

    /**
     * 编辑区域
     *
     * @param sysRegion 区域对象
     * @return
     */
    @PutMapping(path = "/updateSysRegion")
    public ActionResult updateSysRegion(@Validated(UpdateValidator.class) @RequestBody SysRegion sysRegion) {
        sysRegionService.updateSysRegion(sysRegion);
        return ResultBuilder.buildActionSuccess();
    }

    /**
     * 删除区域
     *
     * @param regionCode 区域代码
     * @return
     */
    @PostMapping(path = "/deleteSysRegion")
    public ActionResult deleteSysRegion(@RequestParam(name = "regionCode", required = true) String[] regionCode) {
        sysRegionService.deleteSysRegion(regionCode);
        return ResultBuilder.buildActionSuccess();
    }

    /**
     * 根据查询条件导出区域
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @PostMapping(path = "/exportSysRegion")
    public void exportSysRegion(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList("ID", "区域名称", "区域代码", "区域类型", "区域类型名称", "上级区域代码", "上级区域ID", "创建时间");
            List<LinkedHashMap<String, Object>> dataList = sysRegionService.querySysRegionForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "区域管理", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

}
