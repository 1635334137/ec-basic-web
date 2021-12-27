package com.lanzong.service;

import com.lanzong.error.BusinessException;
import com.lanzong.service.model.ItemModel;

import java.util.List;

public interface ItemService {
    //创建商品
    ItemModel createItemModel(ItemModel itemModel) throws BusinessException;
    //商品列表浏览
    List<ItemModel> listItem();
    //商品详情浏览
    ItemModel getItemById(Integer id);
}
