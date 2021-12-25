package com.lanzong.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
//定义通用校验错误格式
public class ValidationResult {
    //校验结果是否有错,设置默认值防止空指针异常(成员变量有默认值吧)
    private boolean hasErrors = false;

    //存放错误信息的Map
    private Map<String,String> errorMsgMap = new HashMap<>();

    //实现通用的通过格式化字符串信息获取错误结果的msg方法
    public String getErrMsg(){
        //错误信息存在map里，可能有很多错误信息，通过","进行分割拼接起来
        return StringUtils.join(errorMsgMap.values().toArray(),",");
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }
}
