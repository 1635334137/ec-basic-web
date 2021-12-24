package com.lanzong.error;

/**
 * 抽象的错误码格式定义
 */
public interface CommonError {
    public int getErrCode();
    public String getErrMsg();

    //这个方法定义的解释在EmBusinessError类说明
    public CommonError setErrMsg(String errMsg);
}
