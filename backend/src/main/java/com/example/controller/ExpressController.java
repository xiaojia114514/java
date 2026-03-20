package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.domain.Express;
import com.example.service.ExpressService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/express")
public class ExpressController {
    @Resource
    private ExpressService expressService;

    @RequestMapping("/list")
    public List<Express> list(@RequestBody(required = false) Express express) {
        LambdaQueryWrapper<Express> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (express != null) {

            // 动态添加查询条件，只有属性不为 null 时才添加
            if (express.getEid() != null) {
                lambdaQueryWrapper.eq(Express::getEid, express.getEid());
            }
            if (express.getEtext() != null) {
                lambdaQueryWrapper.eq(Express::getEtext, express.getEtext());
            }

        }
        return expressService.list(lambdaQueryWrapper);
    }
}
