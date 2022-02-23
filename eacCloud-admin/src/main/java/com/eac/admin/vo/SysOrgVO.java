package com.eac.admin.vo;
import com.eac.common.core.vo.CommonVO;

import java.io.Serializable;

/**
 * <pre>
 * Title :  机构信息的参数类
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
public class SysOrgVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 6537854989905372885L;
	private String orgName;// 机构名称
	private String orgType;// 机构类型
	private String orgDescription;// 机构描述

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOrgDescription() {
		return orgDescription;
	}

	public void setOrgDescription(String orgDescription) {
		this.orgDescription = orgDescription;
	}

}
