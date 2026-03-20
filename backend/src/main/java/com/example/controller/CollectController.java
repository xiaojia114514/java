package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.domain.Collect;
import com.example.domain.User;
import com.example.service.CollectService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/collect")
public class CollectController {
    @Resource
    private CollectService CollectService;

    @RequestMapping("/create")
    public boolean create(@RequestBody Collect collect) {
        return CollectService.save(collect);
    }

    @RequestMapping("/delete")
    public boolean delete(@RequestBody Collect collect) {
        LambdaQueryWrapper<Collect> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Collect::getUAccount, collect.getUAccount());
        lambdaQueryWrapper.eq(Collect::getGID, collect.getGID());
        return CollectService.remove(lambdaQueryWrapper);
    }

    @RequestMapping("/list")
    public List<Collect> list(@RequestBody(required = false) Collect collect) {
        LambdaQueryWrapper<Collect> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (collect != null) {

            // 动态添加查询条件，只有属性不为 null 时才添加
            if (collect.getCoID() != null) {
                lambdaQueryWrapper.eq(Collect::getCoID, collect.getCoID());
            }
            if (collect.getUAccount() != null) {
                lambdaQueryWrapper.eq(Collect::getUAccount, collect.getUAccount());
            }
            if (collect.getGID() != null) {
                lambdaQueryWrapper.eq(Collect::getGID, collect.getGID());
            }
        }
        return CollectService.list(lambdaQueryWrapper);
    }

    @RequestMapping("/check_collect")
    public boolean check_collect(@RequestBody Collect collect) {
        LambdaQueryWrapper<Collect> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Collect::getUAccount, collect.getUAccount());
        lambdaQueryWrapper.eq(Collect::getGID, collect.getGID());
        return !CollectService.list(lambdaQueryWrapper).isEmpty();
    }
}
