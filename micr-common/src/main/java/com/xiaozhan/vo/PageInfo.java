package com.xiaozhan.vo;

public class PageInfo {
    private Integer pageNo;
    private Integer pageSize;
    private Integer totalRecord;
    private Integer totalPages;

    @Override
    public String toString() {
        return "PageInfo{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", totalRecord=" + totalRecord +
                ", totalPages=" + totalPages +
                '}';
    }

    public PageInfo(Integer pageNo, Integer pageSize, Integer totalRecord) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
    }

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

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Integer getTotalPages() {
        //计算总页数
        if(totalRecord%pageSize==0){
            totalPages=totalRecord/pageSize;
        }else{
            totalPages=totalRecord/pageSize+1;
        }
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public PageInfo(Integer pageNo, Integer pageSize, Integer totalRecord, Integer totalPages) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
        this.totalPages = totalPages;
    }

    public PageInfo() {
    }
}
