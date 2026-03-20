package com.example.mapper;

import com.example.domain.Goodspics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 小假
* @description 针对表【GoodsPics】的数据库操作Mapper
* @createDate 2024-11-06 18:47:05
* @Entity com.example.domain.Goodspics
*/
@Mapper
public interface GoodspicsMapper extends BaseMapper<Goodspics> {
    List<Goodspics> find_data(int gid);
}
