package com.eac.admin.vo;


import com.eac.common.core.validator.InsertValidator;
import com.eac.common.core.vo.CommonVO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <pre>
 * Title :  角色信息的参数类
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
public class SysRoleVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 4928960051913469176L;
	private String roleName;// 角色名称
	@NotNull(groups = { InsertValidator.class })
	private Long roleId;// 角色ID
	@NotEmpty(groups = { InsertValidator.class })
	private String[][] userId;// 用户ID

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String[][] getUserId() {
		return userId;
	}

	public void setUserId(String[][] userId) {
		this.userId = userId;
	}

}
