package com.spc.common;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class PageInfo<T>{


    private Integer pageNo;
    private Integer pageSize;
    private Long totalElements;
    private List<T> content;
    private Integer totalPages;


    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }


    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalPageSize() {
        totalPages = (int)Math.ceil(totalElements/((pageSize*1.0)));
    }


    public void setPageInfo(Pagenation<T> pagenation,long totalSize) {
        pageSize = pagenation.getPageSize();
        pageNo = pagenation.getPageNum();
        setTotalElements(totalSize);
        setTotalPageSize();
    }


    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    @Override
    public String toString(){
        return JSONObject.toJSONString(this);
    }
}
