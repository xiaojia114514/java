package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.domain.Category;
import com.example.domain.Goodspics;
import com.example.service.GoodspicsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/pic")
public class PicController {
    @Resource
    private GoodspicsService goodspicsService;

    @RequestMapping("/create")
    public boolean create(@RequestBody Map<String,Object> picMap){
        int gid = (int) picMap.get("gid");
        byte[] data = Base64.getDecoder().decode((String) picMap.get("data"));
        Goodspics goodspics = new Goodspics();
        goodspics.setGid(gid);
        goodspics.setPicdata(data);
        return goodspicsService.save(goodspics);
    }
    @RequestMapping("/delete/{id}")
    public boolean delete(@PathVariable int id){
        return goodspicsService.removeById(id);
    }
    @RequestMapping("/list_pic")
    public Map<Integer,String> list_pic(@RequestBody Map<String,Object> picMap){
        int gid = (int) picMap.get("gid");
        LambdaQueryWrapper<Goodspics> queryWrapper = Wrappers.<Goodspics>lambdaQuery()
                .eq(Goodspics::getGid, gid) // 假设您要根据 gid 查询
                .select(Goodspics::getPicid); // 指定只查询 Picid 列
        List<Integer> pids = goodspicsService.listObjs(queryWrapper);
        List<byte[]> data = goodspicsService.find_data(gid);
        List<String> picdata=data.stream()
                .map(Base64.getEncoder()::encodeToString)
                .collect(Collectors.toList());
        if (pids.size() != picdata.size()) {
            throw new IllegalStateException("The length of pids array does not match the size of picdata list.");
        }
        // 将pids和picdata组成Map
        Map<Integer, String> result = IntStream.range(0, pids.size())
                .boxed()
                .collect(Collectors.toMap(pids::get, picdata::get));
        return result;
    }
}
