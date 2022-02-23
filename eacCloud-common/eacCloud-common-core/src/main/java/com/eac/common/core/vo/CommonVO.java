package com.eac.common.core.vo;

import java.io.Serializable;


/**
 * <pre>
 * Title :  参数类共用字段
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
public class CommonVO implements Serializable {

	private static final long serialVersionUID = 6332697217948480782L;

	Integer currentPage = 1;// 当前页数
	Integer pageSize = 10;// 每页记录数
	String sorter;// 排序

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSorter() {
		return sorter;
	}

	public void setSorter(String sorter) {
		this.sorter = sorter;
	}

}
