package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.domain.Stockhistory;
import com.example.service.StockhistoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class StockhistoryController {
    @Resource
    private StockhistoryService stockhistoryService;

    @RequestMapping("/list")
    public List<Stockhistory> list(@RequestBody(required = false) Stockhistory stockhistory) {
        LambdaQueryWrapper<Stockhistory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (stockhistory != null) {
            // 动态添加查询条件，只有属性不为 null 时才添加
            if (stockhistory.getSHID() != null) {
                lambdaQueryWrapper.eq(Stockhistory::getSHID, stockhistory.getSHID());
            }
            if (stockhistory.getSHTime() != null) {
                lambdaQueryWrapper.eq(Stockhistory::getSHTime, stockhistory.getSHTime());
            }
            if (stockhistory.getSHReason() != null) {
                lambdaQueryWrapper.eq(Stockhistory::getSHReason, stockhistory.getSHReason());
            }
            if (stockhistory.getGID() != null) {
                lambdaQueryWrapper.eq(Stockhistory::getGID, stockhistory.getGID());
            }
            if (stockhistory.getSHStock_O() != null) {
                lambdaQueryWrapper.eq(Stockhistory::getSHStock_O, stockhistory.getSHStock_O());
            }
            if (stockhistory.getSHStock_N() != null) {
                lambdaQueryWrapper.eq(Stockhistory::getSHStock_N, stockhistory.getSHStock_N());
            }
        }

        return stockhistoryService.list(lambdaQueryWrapper);
    }
}
