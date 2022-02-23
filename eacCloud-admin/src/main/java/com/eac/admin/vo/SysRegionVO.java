package com.eac.admin.vo;

import com.eac.common.core.vo.CommonVO;

import java.io.Serializable;

/**
 * <pre>
 * Title :  区域信息的参数类
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-12-15
 **/
public class SysRegionVO extends CommonVO implements Serializable {

    private static final long serialVersionUID = 7389723555724532049L;
    private String regionName;// 区域名称
    private String regionCode;// 区域代码
    private String regionType;// 区域类型

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionType() {
        return regionType;
    }

    public void setRegionType(String regionType) {
        this.regionType = regionType;
    }

}

