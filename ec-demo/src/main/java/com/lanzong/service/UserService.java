package com.lanzong.service;

import com.lanzong.error.BusinessException;
import com.lanzong.service.model.UserModel;

//功能注释应该写在接口上，这样不用到具体实现的方法中看也能知道方法实现的是什么功能，写在具体实现上反而难查找，因为实现代码可能非常的长，而接口清晰
public interface UserService {
    //通过用户ID获取用户对象的方法
    UserModel getUserById(Integer id);

    //用户注册
    void register(UserModel userModel) throws BusinessException;

    /**
     * 用户登录
     * 登录参数校验
     * telphone:用户注册手机
     * password:用户加密后的密码
     */
    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;
}
