package com.lanzong.error;

/**
 * 使用枚举可以从上往下依次清晰的可视化看到错误码对应的错误消息
 * 如：10000 ”用户不存在“
 *    10001 "用户密码错误"
 *    10002 ”用户名错误“
 *    20001 ”订单提交失败“
 *    20002 ”订单删除失败“
 * 这样清晰
 *
 * 具体的错误实现类
 */
public enum EmBusinessError implements CommonError{

    //通用错误类型10001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(10002,"未知错误"),

    //20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户手机号或密码不正确"),//不能提示得太明显，否则容易受到针对攻击，破解一个字段比破解两个字段容易
    USER_NOT_LOGIN(20003,"用户还未登录"),
    //30000开头为交易信息错误定义
    STOCK_NOT_ENOUGH(30001,"库存不足"),

    ;

    private int errCode;
    private String errMsg;

    private EmBusinessError(int errCode,String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }


    /**
     * 可以改动errMsg的方法
     * 为什么需要定义这个方法呢？
     * 举个例子：参数校验
     * 当参数错误有邮箱错误、手机号错误、用户名错误等等时
     * 定义一个通用的错误类型为参数不合法-通用错误类型10001
     * 但场景不同，具体msg会不同。通过这个方法就能修改msg
     * 而不用每个参数错误去定义不同的错误类型
     * 相当于都是参数类型错误，抽象一层，定义一个通用的错误码，具体是哪种参数错误则具体提供错误描述，但都是属于参数错误的定义范围
     * @param errMsg
     * @return
     */
    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
