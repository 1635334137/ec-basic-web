package com.lanzong.service.impl;

import com.lanzong.dao.UserDOMapper;
import com.lanzong.dao.UserPasswordDOMapper;
import com.lanzong.dataobject.UserDO;
import com.lanzong.dataobject.UserPasswordDO;
import com.lanzong.error.BusinessException;
import com.lanzong.error.EmBusinessError;
import com.lanzong.service.UserService;
import com.lanzong.service.model.UserModel;
import com.lanzong.validator.ValidationResult;
import com.lanzong.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){//判空处理
            return null;
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO, userPasswordDO);
    }

    //访问权限：public 的方法才起作用。@Transactional 注解应该只被应用到 public 方法上，这是由 Spring AOP 的本质决定的。
    @Override
    @Transactional//声明式事务：将标签放置在需要进行事务管理的方法上，而不是放在所有接口实现类上：只读的接口就不需要事务管理，由于配置了@Transactional就需要AOP拦截及事务的处理，可能影响系统性能。
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //在执行insert操作前肯定是要对参数进行校验的，但是这里校验只是做了初略的校验，而且写法也很麻烦
//        if(StringUtils.isEmpty(userModel.getName())
//                || userModel.getGender() == null
//                || userModel.getAge() == null
//                || StringUtils.isEmpty(userModel.getTelphone())){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }

        //参数校验
        ValidationResult result = validator.validate(userModel);
        if(result.isHasErrors()){//该类里定义了一个标识如果为true则表示有校验失败的参数
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //实现model->dataobject方法
        //需要在同一事务中
        UserDO userDO = convertFromModel(userModel);

        try {
            //使用insertSelective和insert的区别：insertSelective在用户没有传递参数时，使用数据库默认的参数【如果数据库不设置默认参数会报错】，具体可以看SQL
            userDOMapper.insertSelective(userDO);
        }catch (DuplicateKeyException e){
            //DuplicateKeyException为主键冲突异常，也就是设置了telphone唯一索引后，如果还使用同样的手机号注册，则系统会抛出该异常
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已重复注册");
        }

        userModel.setId(userDO.getId());//直接get是获取不到ID的，需要在mapper.xml的SQL中配置keyProperty="id" useGeneratedKeys="true" 参数意思可以百度了解
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if(userDO == null){//手机号未注册用户
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);
        //比对用户信息内加密的密码是否和传输进来的密码相匹配
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){//密码不正确
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }

        //这里返回Model是把model存储到session中，以供后面使用
        return userModel;
    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
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
