package com.example.mapper;

import com.example.domain.Stockhistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小假
* @description 针对表【StockHistory】的数据库操作Mapper
* @createDate 2024-12-04 20:27:52
* @Entity com.example.domain.Stockhistory
*/
@Mapper
public interface StockhistoryMapper extends BaseMapper<Stockhistory> {

}




