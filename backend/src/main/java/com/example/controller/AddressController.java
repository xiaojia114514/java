package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.domain.Address;
import com.example.service.AddressService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Resource
    private AddressService addressService;

    @RequestMapping("/create")
    public boolean create(@RequestBody Address address) {
        return addressService.save(address);
    }

    @RequestMapping("/delete/{id}")
    public boolean delete(@PathVariable int id) {
        return addressService.removeById(id);
    }

    @RequestMapping("/update")
    public boolean update(@RequestBody Address address) {
        LambdaUpdateWrapper<Address> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Address::getAid,address.getAid());
        return addressService.update(address,lambdaUpdateWrapper);
    }
    @RequestMapping("/list")
    public List<Address> list(@RequestBody Address address) {
        LambdaQueryWrapper<Address> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 动态添加查询条件，只有属性不为 null 时才添加
        if (address.getAid() != null) {
            lambdaQueryWrapper.eq(Address::getAid, address.getAid());
        }
        if (address.getUaccount() != null) {
            lambdaQueryWrapper.eq(Address::getUaccount, address.getUaccount());
        }
        if (address.getAtext() != null) {
            lambdaQueryWrapper.eq(Address::getAtext, address.getAtext());
        }
        if (address.getAdefault() != null) {
            lambdaQueryWrapper.eq(Address::getAdefault, address.getAdefault());
        }
        return addressService.list(lambdaQueryWrapper);
    }
}
