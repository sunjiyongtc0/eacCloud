package com.eac.admin.vo;


import com.eac.common.core.vo.CommonVO;

import java.io.Serializable;

/**
 * <pre>
 * Title :  字典信息的参数类
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
public class SysDictVO extends CommonVO implements Serializable {

	private static final long serialVersionUID = 3956287435612937606L;
	private String dictType;// 字典类型
	private String dictName;// 字典名称
	private String dictValue;// 字典值

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

}
