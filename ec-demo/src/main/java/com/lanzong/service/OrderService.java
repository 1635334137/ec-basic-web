package com.lanzong.service;

import com.lanzong.error.BusinessException;
import com.lanzong.service.model.OrderModel;

public interface OrderService {

    OrderModel createOrder(Integer userId,Integer itemId,Integer amount) throws BusinessException;
}
