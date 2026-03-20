package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.domain.Aftersale;
import com.example.domain.Order;
import com.example.service.AftersaleService;
import com.example.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aftersale")
public class AftersaleController {
    @Resource
    private AftersaleService aftersaleService;
    @Resource
    private OrderService orderService;

    @RequestMapping("/creat")
    public boolean creat(@RequestBody Aftersale aftersale) {
        return aftersaleService.save(aftersale);
    }

    @RequestMapping("/delete/{id}")
    public boolean delete(@PathVariable int id) {
        return aftersaleService.removeById(id);
    }

    @RequestMapping("/update")
    public boolean update(@RequestBody Aftersale aftersale) {
        LambdaUpdateWrapper<Aftersale> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Aftersale::getAfid, aftersale.getAfid());
        return aftersaleService.update(aftersale, lambdaUpdateWrapper);
    }

    @RequestMapping("/list_by_account")
    public List<Aftersale> list_by_account(@RequestBody Map<String, Object> map) {
        LambdaQueryWrapper<Order> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Order::getUaccount, map.get("account"));
        List<Order> orders = orderService.list(lambdaQueryWrapper1);
        List<Integer> oids = orders.stream()
                .map(Order::getOid)
                .toList();
        LambdaQueryWrapper<Aftersale> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.in(Aftersale::getOid, oids);
        if (map.get("state") != null) lambdaQueryWrapper2.eq(Aftersale::getAfstate, map.get("state"));
        return aftersaleService.list(lambdaQueryWrapper2);
    }

    @RequestMapping("/list")
    public List<Aftersale> list(@RequestBody(required = false) Aftersale aftersale) {
        LambdaQueryWrapper<Aftersale> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (aftersale != null) {

            // 动态添加查询条件，只有属性不为 null 时才添加
            if (aftersale.getAfid() != null) {
                lambdaQueryWrapper.eq(Aftersale::getAfid, aftersale.getAfid());
            }
            if (aftersale.getOid() != null) {
                lambdaQueryWrapper.eq(Aftersale::getOid, aftersale.getOid());
            }
            if (aftersale.getAfname() != null) {
                lambdaQueryWrapper.eq(Aftersale::getAfname, aftersale.getAfname());
            }
            if (aftersale.getAftext() != null) {
                lambdaQueryWrapper.eq(Aftersale::getAftext, aftersale.getAftext());
            }
            if (aftersale.getAfstate() != null) {
                lambdaQueryWrapper.eq(Aftersale::getAfstate, aftersale.getAfstate());
            }
            if (aftersale.getAftime() != null) {
                lambdaQueryWrapper.eq(Aftersale::getAftime, aftersale.getAftime());
            }
        }
        return aftersaleService.list(lambdaQueryWrapper);
    }

}
