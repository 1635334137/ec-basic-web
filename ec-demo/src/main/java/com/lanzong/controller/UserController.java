package com.lanzong.controller;

import com.lanzong.controller.viewobject.UserVO;
import com.lanzong.error.BusinessException;
import com.lanzong.error.EmBusinessError;
import com.lanzong.response.CommonReturnType;
import com.lanzong.service.UserService;
import com.lanzong.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


//allowCredentials为true需要配置具体的url地址，和其他的一些header配置，但是目前我无法解决，因为指定了具体的url，前端还是403CORS问题
//allowCredentials为true 需要前端的配合 xhrFields:{withCredentials:true}
//问题先留着，暂时不做验证
@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",originPatterns = "*")//跨域处理(如果不定义allowCredentials参数默认的配置是false,仅仅允许来自任何域的访问，解决的访问安全受限问题，但是无法达到Session的共享)
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    //这种方式注入，Spring是以单例形式来创建的实例，那是如何进行并发的访问控制？
    //实际上经过Spring的包装，它的本质是一个proxy，让用户可以在自己的线程处理自己的request
    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户登录接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone")String telphone,
                                  @RequestParam(name = "password")String password) throws BusinessException, NoSuchAlgorithmException {

        //入参校验
        if(StringUtils.isEmpty(telphone)||StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        //用户登录服务，用来校验用户登录是否合法
        UserModel userModel = userService.validateLogin(telphone, this.EncodeByMd5(password));

        //将登录凭证加入到用户登录成功的session内，这里情况是假设用户是单点登录，不做分布式的session共享。
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);//是否传参数要看前端是否需要数据，在前端钩子方法success里可以获取到数据

    }

    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Integer gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "password")String password) throws BusinessException, NoSuchAlgorithmException {

        //验证手机号和对应的otpcode相符合
        String inSessionOtpCode = (String) this.httpServletRequest.getSession().getAttribute(telphone);
        //暂时把otp验证注释，等后面CORS解决了再说
//        if(!StringUtils.equals(otpCode,inSessionOtpCode)){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码错误");
//        }
        //用户的注册流程
        //一开始我以为controller操作的应该是VO，其实应该是Model，Model才是业务逻辑操作的模型【核心领域模型】，VO是供前端展示用的，DO是数据库映射用的
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telphone);//如果用户使用同一个手机号是可以重复注册的，所以要给手机号在数据库里设置唯一索引
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMd5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMd5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String newstr = base64Encoder.encode(md5.digest(str.getBytes(StandardCharsets.UTF_8)));
        return newstr;
    }


    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telphone){
        //按照一定规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        //将OTP验证码同对应用户的手机号关联，通常使用redis的键值对来实现有天然优势，这里使用httpsession的方式绑定手机号和otpCode
        httpServletRequest.getSession().setAttribute(telphone,otpCode);


        //将OTP验证码通过短信通道发送给用户，省略
        System.out.println("telphone="+telphone+"&otpCode="+otpCode);

        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id")Integer id) throws BusinessException {
        UserModel userModel = userService.getUserById(id);

        //若获取的对应用户信息不存在
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
            //用于测试不是主动抛出异常，能否被捕获到，提供给用户良好的反馈
            //userModel.setEncrptPassword("1123");
        }

        //将核心领域模型对象转化为可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);

        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }
}
