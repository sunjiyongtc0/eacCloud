package com.eac.admin.vo;

import com.eac.common.core.vo.CommonVO;

import java.io.Serializable;

/**
 * <pre>
 * Title :  岗位信息的参数类
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
public class SysPostVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = -649384245100651595L;
	private String postCode;// 岗位编码
	private String postName;// 岗位名称

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

}
