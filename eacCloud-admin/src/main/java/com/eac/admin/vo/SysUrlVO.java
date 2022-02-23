package com.eac.admin.vo;

import com.eac.common.core.vo.CommonVO;

import java.io.Serializable;

/**
 * <pre>
 * Title :  接口信息的参数类
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-23
 **/
public class SysUrlVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = -1887383111996273369L;
	private String url;// URL
	private String description;// 描述
	private String menuCode; // 菜单编码
	private Long[] roleId;// 角色ID

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public Long[] getRoleId() {
		return roleId;
	}

	public void setRoleId(Long[] roleId) {
		this.roleId = roleId;
	}

}
