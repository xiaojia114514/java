package com.example.mapper;

import com.example.domain.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 小假
* @description 针对表【Address】的数据库操作Mapper
* @createDate 2025-03-25 16:39:41
* @Entity com.example.domain.Address
*/
@Mapper
public interface AddressMapper extends BaseMapper<Address> {

}




