package com.example.listmanager.model;

import lombok.Data;

import java.util.List;


@Data
public class CustomPage<T> {
    private int totalCount;
    private int totalPageCount;
    private int pageIndex;
    private int pageSize;
    private List<T> data;

    public CustomPage() {
    }

    public CustomPage(List<T> data, int totalCount, int totalPageCount, int pageIndex, int pageSize) {
        this.totalCount = totalCount;
        this.totalPageCount = totalPageCount;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
