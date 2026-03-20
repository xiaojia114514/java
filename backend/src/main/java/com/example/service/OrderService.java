package com.example.service;

import com.example.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 小假
* @description 针对表【Order】的数据库操作Service
* @createDate 2024-11-03 21:05:20
*/
public interface OrderService extends IService<Order> {
    List<Order> get_by_oid(int id);

    List<Order> Show_All();

    List<Order> get_History_By_Account(String account);

    List<Order> get_History_By_Account_And_State(String account,int state);

    boolean update_oinfo(int id, String phone, String address, String remark);

    boolean update_ostate(int id, int state);

    boolean creat(Order order);
}
