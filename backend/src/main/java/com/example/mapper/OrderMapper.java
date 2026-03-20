package com.example.mapper;

import com.example.domain.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 小假
* @description 针对表【Order】的数据库操作Mapper
* @createDate 2024-11-03 21:05:20
* @Entity com.example.domain.Order
*/
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    List<Order> list_Order();
    List<Order> get_History_By_Account(String account);

    List<Order> get_History_By_Account_And_State(String account, int state);

    int update_oinfo(int id, String phone, String address, String remark);

    int update_ostate(int id, int state);

    List<Order> get_by_oid(int id);

    int creat(Order order);
}




