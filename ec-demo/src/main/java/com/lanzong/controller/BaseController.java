package com.lanzong.controller;

import com.lanzong.error.BusinessException;
import com.lanzong.error.EmBusinessError;
import com.lanzong.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//这个异常处理不仅仅是UserController使用，而是所有Controller都用
//所以定义一个Controller基类，放置公共方法。让其他controller继承基类
public class BaseController {

    //定义exceptionhandler解决未被controller层吸收的exception
    //防止异常抛出到容器Tomcat，让容器来处理异常信息。不利于前端处理
    //controller是业务处理的最后一道关卡，controller的异常被处理掉了，在返回到前端时用户的体验感会好些
    @ExceptionHandler(Exception.class)//指定捕获哪种异常，这里指定根类
    @ResponseStatus(HttpStatus.OK)//异常一般为业务逻辑错误，并不是服务器不能处理，所以定义200
    @ResponseBody//如果不加还是会去找相关页面，而不能处理类型
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String,Object> responseData = new HashMap<>();
        if(ex instanceof BusinessException){
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode",businessException.getErrCode());
            responseData.put("errMsg",businessException.getErrMsg());
        }else{//当抛出的异常不是BusinessException类型时，也不能让用户感知，而是给出一个未知异常信息
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg",EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonReturnType.create(responseData,"fail");
    }
}
