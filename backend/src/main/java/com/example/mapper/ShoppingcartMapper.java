package com.example.mapper;

import com.example.domain.Shoppingcart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小假
* @description 针对表【ShoppingCart】的数据库操作Mapper
* @createDate 2024-12-29 17:18:17
* @Entity com.example.domain.Shoppingcart
*/
@Mapper
public interface ShoppingcartMapper extends BaseMapper<Shoppingcart> {

}




