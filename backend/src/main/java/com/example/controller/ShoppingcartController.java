package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.domain.Shoppingcart;
import com.example.domain.User;
import com.example.service.ShoppingcartService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shoppingcart")
public class ShoppingcartController {
    @Resource
    private ShoppingcartService shoppingcartService;

    @RequestMapping("/create")
    public boolean create(@RequestBody Shoppingcart shoppingcart) {
        return shoppingcartService.save(shoppingcart);
    }

    @RequestMapping("/delete/{id}")
    public boolean delete(@PathVariable int id) {
        return shoppingcartService.removeById(id);
    }

    @RequestMapping("/update")
    public boolean update(@RequestBody Shoppingcart shoppingcart) {
        LambdaUpdateWrapper<Shoppingcart> UpdateWrapper = new LambdaUpdateWrapper<>();
        UpdateWrapper.eq(Shoppingcart::getSCID, shoppingcart.getSCID());
        return shoppingcartService.update(shoppingcart, UpdateWrapper);
    }

    @RequestMapping("/list")
    public List<Shoppingcart> list(@RequestBody(required = false) Shoppingcart shoppingcart) {
        LambdaQueryWrapper<Shoppingcart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (shoppingcart != null) {

            // 动态添加查询条件，只有属性不为 null 时才添加
            if (shoppingcart.getSCID() != null) {
                lambdaQueryWrapper.eq(Shoppingcart::getSCID, shoppingcart.getSCID());
            }
            if (shoppingcart.getGID() != null) {
                lambdaQueryWrapper.eq(Shoppingcart::getGID, shoppingcart.getGID());
            }
            if (shoppingcart.getUAccount() != null) {
                lambdaQueryWrapper.eq(Shoppingcart::getUAccount, shoppingcart.getUAccount());
            }
            if (shoppingcart.getSCNumber() != null) {
                lambdaQueryWrapper.eq(Shoppingcart::getSCNumber, shoppingcart.getSCNumber());
            }

        }
        return shoppingcartService.list(lambdaQueryWrapper);
    }

    @RequestMapping("/get_by_id/{id}")
    public Shoppingcart get_by_id(@PathVariable int id) {
        return shoppingcartService.getById(id);
    }
}
