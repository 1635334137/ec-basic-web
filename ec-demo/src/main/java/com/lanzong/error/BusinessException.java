package com.lanzong.error;

//设计模式：装饰器模式
//自定义异常信息类-用于主动抛出异常（还需要一个异常捕捉类进行处理）
public class BusinessException extends Exception implements CommonError{

    private CommonError commonError;

    public BusinessException(CommonError error){
        super();
        this.commonError = error;
    }

    //接收自定义errMsg的方式构建业务异常
    //这里再次体现了setErrMsg方法的灵活性
    public BusinessException(CommonError commonError,String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
