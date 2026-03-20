package com.example.mapper;

import com.example.domain.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小假
* @description 针对表【Category】的数据库操作Mapper
* @createDate 2024-11-03 21:05:10
* @Entity com.example.domain.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}




