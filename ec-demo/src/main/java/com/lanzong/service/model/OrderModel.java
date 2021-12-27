package com.lanzong.service.model;

import java.math.BigDecimal;

//用户下单的交易模型
public class OrderModel {
    //20211228000001224 下单的id一般以时间日期加具体的数值组合成
    private String id;
    //购买的用户id
    private Integer userId;
    //购买的商品id
    private Integer itemId;
    //购买商品的单价（由于商品的价格在不同时期是会发生变化的，这个值记录购买这个商品时，商品的价格）
    private BigDecimal itemPrice;
    //购买数量
    private Integer amount;
    //购买金额（真实的电商较为复杂，存在不同的商品购买不同的数量的情况，这里假设用户购买一个商品支付一次价钱的情况）
    private BigDecimal orderPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
}
