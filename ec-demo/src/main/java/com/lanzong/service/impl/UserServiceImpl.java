package com.lanzong.service.impl;

import com.lanzong.dao.UserDOMapper;
import com.lanzong.dao.UserPasswordDOMapper;
import com.lanzong.dataobject.UserDO;
import com.lanzong.dataobject.UserPasswordDO;
import com.lanzong.service.UserService;
import com.lanzong.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){//判空处理
            return null;
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO, userPasswordDO);
    }

    //把userDO和userpasswordDO组合成为业务需要的userModel
    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null){//判空处理
            return null;
        }
        UserModel userModel = new UserModel();
        //把UserDO的属性值copy到UserModel上去
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO != null){
            //因为UserPasswordDO中也有id属性，所以不能直接copy，所以通过简单的set方式设置属性值
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
