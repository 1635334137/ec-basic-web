package com.lanzong.controller;

import com.lanzong.error.BusinessException;
import com.lanzong.error.EmBusinessError;
import com.lanzong.response.CommonReturnType;
import com.lanzong.service.OrderService;
import com.lanzong.service.model.OrderModel;
import com.lanzong.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(allowCredentials = "true",originPatterns = "*")
public class OrderController extends BaseController{
    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //封装下单请求
    @RequestMapping(value = "/createorder",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId")Integer itemId,
                                        @RequestParam(name = "amount")Integer amount) throws BusinessException {
        //获取用户的登录信息（跨域问题，由于跨域解决不掉【无法携带cookie】导致每次请求都是一个新的session，这里就无法获取Attribute）
        Boolean is_login = (Boolean) this.httpServletRequest.getSession().getAttribute("IS_LOGIN");
//        if(is_login == null || !is_login.booleanValue()){
//            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
//        }
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        //这里只能先写死了，获取不到session存储的值
        OrderModel orderModel = orderService.createOrder(8, itemId, amount);
        return CommonReturnType.create(null);
    }
}
