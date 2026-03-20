package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.DTO.Response;
import com.example.domain.Goods;
import com.example.domain.Order;
import com.example.domain.Stockhistory;
import com.example.service.GoodsService;
import com.example.service.OrderService;
import com.example.service.StockhistoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Resource
    private OrderService orderService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private StockhistoryService stockhistoryService;

    //查
    @RequestMapping("/list")
    public List<Order> list(@RequestBody(required = false) Order order) {
        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (order != null) {

            // 动态添加查询条件，只有属性不为 null 时才添加
            if (order.getOid() != null) {
                lambdaQueryWrapper.eq(Order::getOid, order.getOid());
            }
            if (order.getGid() != null) {
                lambdaQueryWrapper.eq(Order::getGid, order.getGid());
            }
            if (order.getEid() != null) {
                lambdaQueryWrapper.eq(Order::getEid, order.getEid());
            }
            if (order.getUaccount() != null) {
                lambdaQueryWrapper.eq(Order::getUaccount, order.getUaccount());
            }
            if (order.getOphone() != null) {
                lambdaQueryWrapper.eq(Order::getOphone, order.getOphone());
            }
            if (order.getOaddress() != null) {
                lambdaQueryWrapper.eq(Order::getOaddress, order.getOaddress());
            }
            if (order.getOnumber() != null) {
                lambdaQueryWrapper.eq(Order::getOnumber, order.getOnumber());
            }
            if (order.getOremark() != null) {
                lambdaQueryWrapper.eq(Order::getOremark, order.getOremark());
            }
            if (order.getOstate() != null) {
                lambdaQueryWrapper.eq(Order::getOstate, order.getOstate());
            }
            if (order.getOpaid() != null) {
                lambdaQueryWrapper.eq(Order::getOpaid, order.getOpaid());
            }
        }
        return orderService.list(lambdaQueryWrapper);
    }

    @RequestMapping("/creat")
    public boolean creat(@RequestBody Order order) {
        return orderService.save(order);
    }

    @RequestMapping("/update")
    public Response update(@RequestBody Order order) {
        LambdaUpdateWrapper<Order> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Order::getOid, order.getOid());
        if (order.getOstate() == 2) {//如果是备货完成
            //先判断库存是否足够
            Order o = orderService.getById(order.getOid());
            Goods goods = goodsService.getById(o.getGid());
            if (o.getOnumber() > goods.getGstock()) return new Response(401, "库存不足", null);
            Goods g = new Goods();
            g.setGstock(goods.getGstock() - o.getOnumber());
            //出库后如果库存为0
            if (g.getGstock() == 0) g.setGshelf(2);
            //修改Goods表
            LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Goods::getGid, o.getGid());
            if (!goodsService.update(g, updateWrapper)) return new Response(402, "商品库存修改失败", null);
            //同步添加出库记录
            Stockhistory stockhistory = new Stockhistory();
            stockhistory.setGID(o.getGid());
            stockhistory.setSHStock_O(goods.getGstock());
            stockhistory.setSHStock_N(g.getGstock());
            stockhistory.setSHTime(new Date());
            stockhistory.setSHReason(0);//0表示该库存记录变更原因为备货出库
            if (!stockhistoryService.save(stockhistory)) return new Response(403, "库存记录插入失败", null);
            ;
        }
        if (orderService.update(order, lambdaUpdateWrapper)) {
            return new Response(200, "修改成功", null);
        } else {
            return new Response(200, "修改失败", null);
        }
    }

    @RequestMapping("/cancel")
    public boolean cancel(@RequestBody Map<String, Object> orderMap) {
        int oid = (int) orderMap.get("oid");
        int ucategory = (int) orderMap.get("ucategory");
        LambdaUpdateWrapper<Order> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Order::getOid, oid);
        Order order = orderService.getById(oid);//获取订单信息
        Goods goods = goodsService.getById(order.getGid());//获取订单对应商品信息
        Order o = new Order();
        if (order.getOstate() >= 2) {
            //修改商品表
            LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Goods::getGid, goods.getGid());
            Goods g = new Goods();
            g.setGstock(goods.getGstock() + order.getOnumber());
            goodsService.update(g, updateWrapper);
            //修改库存表
            Stockhistory stockhistory = new Stockhistory();
            stockhistory.setGID(order.getGid());
            stockhistory.setSHStock_O(goods.getGstock());
            stockhistory.setSHStock_N(g.getGstock());
            stockhistory.setSHTime(new Date());
            stockhistory.setSHReason(2);//2表示该库存记录变更原因为取消订单
            stockhistoryService.save(stockhistory);
        }
        if (ucategory == 0) {
            o.setOstate(0);
        } else {
            o.setOstate(-1);
        }
        return orderService.update(o, lambdaUpdateWrapper);
    }
}
