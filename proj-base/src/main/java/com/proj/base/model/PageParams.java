package com.proj.base.model;

import lombok.Data;
import lombok.ToString;
import lombok.extern.java.Log;

/**
 * @description Paging query common parameters
 * @author Yinuo Yao
 * @date 2023/10/05
 * @version 1.0
 */
@Data
@ToString
public class PageParams {

    // Current Page
    private Long pageNo = 1L;

    // default value of numbers of records in each page
    private Long pageSize =10L;

    public PageParams(){

    }

    public PageParams(long pageNo,long pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

}
