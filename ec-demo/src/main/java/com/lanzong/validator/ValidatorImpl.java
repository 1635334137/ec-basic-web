package com.lanzong.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，
 * 凡是继承该接口的类，在初始化bean的时候都会执行该方法。
 * 在Spring容器初始化bean完成后，会调用afterPropertiesSet方法（回调）
 */
@Component
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    //实现校验方式并返回校验结果
    public ValidationResult validate(Object bean){
        final ValidationResult result = new ValidationResult();
        //开始校验，校验规则通过注解来设置
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if(constraintViolationSet.size() > 0){//校验失败会把错误信息存入里面，所以大于0就是有参数不合法
            result.setHasErrors(true);
            constraintViolationSet.forEach(constraintViolation->{
                String errMsg = constraintViolation.getMessage();//错误信息
                String propertyName = constraintViolation.getPropertyPath().toString();//错误的参数名，这样才能让我们找到是哪里的哪个参数校验失败
                result.getErrorMsgMap().put(propertyName,errMsg);//把参数名和错误信息，存储到我们定义的通用错误格式类里面
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //通过工厂的初始化方式实例化一个validator
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
