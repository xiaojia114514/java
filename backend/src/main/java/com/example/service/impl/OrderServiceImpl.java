package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.Order;
import com.example.service.OrderService;
import com.example.mapper.OrderMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 小假
* @description 针对表【Order】的数据库操作Service实现
* @createDate 2024-11-03 21:05:20
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

    @Resource
    private OrderMapper orderMapper;

    @Override
    public List<Order> get_by_oid(int id) {
        List<Order> orders = orderMapper.get_by_oid(id);
        return orders;
    }

    @Override
    public List<Order> Show_All() {
        List<Order> orders = orderMapper.list_Order();
        return orders;
    }

    @Override
    public List<Order> get_History_By_Account(String account) {
        return orderMapper.get_History_By_Account(account);
    }

    @Override
    public List<Order> get_History_By_Account_And_State(String account, int state) {
        return orderMapper.get_History_By_Account_And_State(account,state);
    }

    @Override
    public boolean update_oinfo(int id, String phone, String address, String remark) {
        int affectedRows = orderMapper.update_oinfo(id,phone,address,remark);
        return affectedRows > 0;
    }

    @Override
    public boolean update_ostate(int id, int state) {
        int affectedRows = orderMapper.update_ostate(id,state);
        return affectedRows > 0;
    }

    @Override
    public boolean creat(Order order) {
        int affectedRows = orderMapper.creat(order);
        return affectedRows > 0;
    }
}




