package com.eac.auth.controller;

import com.eac.auth.entity.AppClient;
import com.eac.auth.service.AppClientService;
import com.eac.auth.vo.AppClientVO;

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
 * Title : 应用信息的控制层
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-18
 **/
@RestController
@RequestMapping("/appclient")
public class AppClientController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppClientService appClientService;

    /**
     * 查询应用分页
     *
     * @param appClientVO 应用前端参数
     */
    @GetMapping(path = "/queryAppClient")
    public ListResult<Object> queryAppClient(AppClientVO appClientVO) {
        Map<String, Object> data = appClientService.queryAppClient(appClientVO.getCurrentPage(), appClientVO.getPageSize(), appClientVO.getClientCode(),
                appClientVO.getAuthType(), appClientVO.getSorter());
        return ResultBuilder.buildListSuccess(data);
    }

    /**
     * 新增应用
     *
     * @param appClient 应用对象
     */
    @PostMapping(path = "/addAppClient")
    public ActionResult addAppClient(@Validated(InsertValidator.class) @RequestBody AppClient appClient) {
        appClientService.insertAppClient(appClient);
        return ResultBuilder.buildActionSuccess();
    }

    /**
     * 编辑应用
     *
     * @param appClient 应用对象
     */
    @PutMapping(path = "/updateAppClient")
    public ActionResult updateAppClient(@Validated(UpdateValidator.class) @RequestBody AppClient appClient) {
        appClientService.updateAppClient(appClient);
        return ResultBuilder.buildActionSuccess();
    }

    /**
     * 删除应用
     *
     * @param id 应用ID
     */
    @PostMapping(path = "/deleteAppClient")
    public ActionResult deleteAppClient(@RequestParam(name = "id") Long[] id) {
        appClientService.deleteAppClient(id);
        return ResultBuilder.buildActionSuccess();
    }

    /**
     * 根据查询条件导出应用
     *
     * @param response 响应对象
     * @param paramMap 参数Map
     */
    @PostMapping(path = "/exportAppClient")
    public void exportAppClient(HttpServletResponse response, @RequestParam Map<String, Object> paramMap) {
        try {
            List<String> headList = Arrays.asList("ID", "应用编码", "应用密钥", "授权类型", "授权范围", "令牌秒数", "刷新秒数", "回调地址", "应用描述", "创建时间");
            List<LinkedHashMap<String, Object>> dataList = appClientService.queryAppClientForExcel(paramMap);
            ExcelUtils.exportExcel(headList, dataList, "应用管理", response);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

}
