package com.eac.common.core.entity;



import com.eac.common.core.validator.InsertValidator;
import com.eac.common.core.validator.UpdateValidator;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 实体类共用字段
 *
 */
public class CommonEntity extends TimeEntity implements Serializable {

	private static final long serialVersionUID = 6272491356994391729L;
	@NotNull(groups = { InsertValidator.class, UpdateValidator.class })
	private Long parentId;// 上级ID

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}
