package com.proj.base.exception;

import java.io.Serializable;

/**
 * @Description: Parameter wrapper for error response
 * @Author: Yinuo
 * @Date: 2023/10/9 0:56
 */
public class RestErrorResponse implements Serializable {

    private String errMessage;

    public RestErrorResponse(String errMessage){
        this.errMessage= errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}