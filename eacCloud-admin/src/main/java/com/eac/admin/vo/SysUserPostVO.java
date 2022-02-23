package com.eac.admin.vo;

import com.eac.common.core.vo.CommonVO;

import java.io.Serializable;

/**
 * <pre>
 * Title :  用户与岗位关联信息的参数类
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
public class SysUserPostVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = -3240949575175545937L;
	private Long userId;// 用户ID
	private String postCode;// 岗位编码
	private String postName;// 岗位名称

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

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
