package com.lanzong.service;

import com.lanzong.error.BusinessException;
import com.lanzong.service.model.UserModel;

public interface UserService {
    //通过用户ID获取用户对象的方法
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    /**
     * 登录参数校验
     * telphone:用户注册手机
     * password:用户加密后的密码
     */
    UserModel validateLogin(String telphone,String encrptPassword) throws BusinessException;
}
