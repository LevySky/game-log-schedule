package com.spc.common;

import com.alibaba.fastjson.JSONObject;

public class Pagenation<T> {
	
	
	private Integer pageNum;
	private Integer pageSize;
	private T search;
	
	
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public T getSearch() {
		return search;
	}
	public void setSearch(T search) {
		this.search = search;
	}

	public Integer getSkip(){
		  Integer value = pageNum - 1;
		  value = value > -1?value:0 ;
          return  value;
	}


	public Integer getLimit(){
           return pageSize;
	}

	@Override
	public String toString(){
		return JSONObject.toJSONString(this);
	}

}
