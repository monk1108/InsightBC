package com.proj.base.exception;

/**
 * @Description: Project error class
 * @Author: Yinuo
 * @Date: 2023/10/9 0:58
 */
public class ProjException extends RuntimeException{
    private String errMessage;

    public ProjException() {
        super();
    }

    public ProjException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public static void cast(CommonError commonError){
        throw new ProjException(commonError.getErrMessage());
    }
    public static void cast(String errMessage){
        throw new ProjException(errMessage);
    }
}
