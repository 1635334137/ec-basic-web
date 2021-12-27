package com.lanzong.dao;

import com.lanzong.dataobject.ItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Mon Dec 27 21:23:13 CST 2021
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Mon Dec 27 21:23:13 CST 2021
     */
    int insert(ItemDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Mon Dec 27 21:23:13 CST 2021
     */
    int insertSelective(ItemDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Mon Dec 27 21:23:13 CST 2021
     */
    ItemDO selectByPrimaryKey(Integer id);

    List<ItemDO> listItem();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Mon Dec 27 21:23:13 CST 2021
     */
    int updateByPrimaryKeySelective(ItemDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Mon Dec 27 21:23:13 CST 2021
     */
    int updateByPrimaryKey(ItemDO record);
}