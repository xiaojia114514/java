package com.example.mapper;

import com.example.domain.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小假
* @description 针对表【Goods】的数据库操作Mapper
* @createDate 2024-11-03 21:05:15
* @Entity com.example.domain.Goods
*/
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}




