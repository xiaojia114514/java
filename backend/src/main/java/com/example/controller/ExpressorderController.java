package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.domain.Expressorder;
import com.example.service.ExpressorderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expressorder")
public class ExpressorderController {
    @Resource
    private ExpressorderService expressorderService;

    @RequestMapping("/create")
    public boolean create(@RequestBody Expressorder expressorder) {
        return expressorderService.save(expressorder);
    }

    @RequestMapping("/delete/{id}")
    public boolean delete(@PathVariable int id) {
        return expressorderService.removeById(id);
    }

    @RequestMapping("/update")
    public boolean update(@RequestBody Expressorder expressorder) {
        LambdaUpdateWrapper<Expressorder> UpdateWrapper = new LambdaUpdateWrapper<>();
        UpdateWrapper.eq(Expressorder::getEoid, expressorder.getEoid());
        return expressorderService.update(expressorder, UpdateWrapper);
    }

    @RequestMapping("/list")
    public List<Expressorder> list(@RequestBody(required = false) Expressorder expressorder) {
        LambdaQueryWrapper<Expressorder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (expressorder != null) {

            // 动态添加查询条件，只有属性不为 null 时才添加
            if (expressorder.getEoid() != null) {
                lambdaQueryWrapper.eq(Expressorder::getEoid, expressorder.getEoid());
            }
            if (expressorder.getOid() != null) {
                lambdaQueryWrapper.eq(Expressorder::getOid, expressorder.getOid());
            }
            if (expressorder.getEostate() != null) {
                lambdaQueryWrapper.eq(Expressorder::getEostate, expressorder.getEostate());
            }
            if (expressorder.getEotime() != null) {
                lambdaQueryWrapper.eq(Expressorder::getEotime, expressorder.getEotime());
            }
            if (expressorder.getEoaddress() != null) {
                lambdaQueryWrapper.eq(Expressorder::getEoaddress, expressorder.getEoaddress());
            }
        }

        return expressorderService.list(lambdaQueryWrapper);
    }

    @RequestMapping("/get_by_EOid/{id}")
    public Expressorder get_by_EOid(@PathVariable int id) {
        return expressorderService.getById(id);
    }
}
