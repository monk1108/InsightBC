package com.proj.base.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: Pagination query result model class
 * @Author: Yinuo
 * @Date: 2023/10/5 19:22
 */
@Data
@ToString
public class PageResult<T> implements Serializable {

    // Data list
    private List<T> items;

    // Total record number
    private long counts;

    // Current Page
    private long page;

    // number of records on each page
    private long pageSize;

    public PageResult(List<T> items, long counts, long page, long pageSize) {
        this.items = items;
        this.counts = counts;
        this.page = page;
        this.pageSize = pageSize;
    }



}