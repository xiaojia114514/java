package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.domain.Goods;
import com.example.domain.Goodspics;
import com.example.domain.Stockhistory;
import com.example.service.CategoryService;
import com.example.service.GoodsService;
import com.example.service.GoodspicsService;
import com.example.service.StockhistoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @Resource
    private GoodspicsService goodspicsService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private StockhistoryService stockhistoryService;

    @RequestMapping("/update_Ginfo")
    public boolean update_Ginfo(@RequestBody Map<String, Object> goodsMap) {
        int gid = (int) goodsMap.get("gid");
        String name = (String) goodsMap.get("name");
        String desc = (String) goodsMap.get("desc");
        int stock = (int) goodsMap.get("stock");
        int value = (int) goodsMap.get("value");
        int cid = (int) goodsMap.get("cid");
        Goods g = goodsService.getById(gid);
        LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Goods::getGid, gid);
        Goods goods = new Goods();
        goods.setGname(name);
        goods.setGdesc(desc);
        goods.setGstock(stock);
        goods.setGvalue(value);
        goods.setCid(cid);
        if (goodsService.update(goods, updateWrapper)) {
            if (stock != g.getGstock()) {
                Stockhistory stockhistory = new Stockhistory();
                stockhistory.setGID(gid);
                stockhistory.setSHStock_O(g.getGstock());
                stockhistory.setSHStock_N(stock);
                stockhistory.setSHTime(new Date());
                stockhistory.setSHReason(1);//0表示该库存记录变更原因为手动修改
                return stockhistoryService.save(stockhistory);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @RequestMapping("/update_Gshelf")
    public boolean update_Gshelf(@RequestBody Map<String, Object> goodsMap) {
        int gid = (int) goodsMap.get("gid");
        int shelf = (int) goodsMap.get("shelf");
        if (shelf == 1) {
            QueryWrapper<Goodspics> wrapper = new QueryWrapper<>();
            wrapper.eq("gid", gid);
            if (goodspicsService.count(wrapper) == 0) return false;
        }
        LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Goods::getGid, gid);
        Goods goods = new Goods();
        goods.setGshelf(shelf);
        return goodsService.update(goods, updateWrapper);
    }

    @RequestMapping("/get_by_id/{id}")
    public Goods get_by_id(@PathVariable int id) {
        return goodsService.getById(id);
    }

    @RequestMapping("/list_By_Category")
    public List<Goods> list_By_Category(@RequestBody Map<String, Object> goodsMap) {
        Object cidObject = goodsMap.get("cid");
        if (cidObject == null) {
            throw new IllegalArgumentException("The 'cid' field is required.");
        }
        // 确保 idObject 是数组类型
        if (!(cidObject instanceof List)) {
            throw new IllegalArgumentException("The 'cid' field must be an array of integers.");
        }
        List<Integer> cidList = (List<Integer>) cidObject;
        int[] cid = cidList.stream().mapToInt(i -> i).toArray();
        int[] cids = categoryService.get_all_child(cid);
        List<Goods> goods = new ArrayList<>();
        for (int id : cids) {
            LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Goods::getCid, id);
            goods.addAll(goodsService.list(lambdaQueryWrapper));
        }
        return goods;
    }

    @RequestMapping("/list_by_name")
    public List<Goods> list_by_name(@RequestBody Map<String, Object> goodsMap) {
        String name = (String) goodsMap.get("name");
        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Goods::getGname, name);
        return goodsService.list(lambdaQueryWrapper);
    }

    @RequestMapping("/create")
    public boolean create(@RequestBody Goods goods) {
        return goodsService.save(goods);
    }

    @RequestMapping("/delete/{id}")
    public boolean delete(@PathVariable int id) {
        return goodsService.removeById(id);
    }

    //新接口：用于获取某商家的特定商品
    @RequestMapping("/list")//用此接口替全部查询接口
    public List<Goods> list(@RequestBody(required = false) Goods goods) {
        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (goods != null) {

            // 动态添加查询条件，只有属性不为 null 时才添加
            if (goods.getGid() != null) {
                lambdaQueryWrapper.eq(Goods::getGid, goods.getGid());
            }
            if (goods.getGname() != null) {
                lambdaQueryWrapper.eq(Goods::getGname, goods.getGname());
            }
            if (goods.getGdesc() != null) {
                lambdaQueryWrapper.eq(Goods::getGdesc, goods.getGdesc());
            }
            if (goods.getGstock() != null) {
                lambdaQueryWrapper.eq(Goods::getGstock, goods.getGstock());
            }
            if (goods.getGvalue() != null) {
                lambdaQueryWrapper.eq(Goods::getGvalue, goods.getGvalue());
            }
            if (goods.getCid() != null) {
                lambdaQueryWrapper.eq(Goods::getCid, goods.getCid());
            }
            if (goods.getGseller() != null) {
                lambdaQueryWrapper.eq(Goods::getGseller, goods.getGseller());
            }
            if (goods.getGshelf() != null) {
                lambdaQueryWrapper.eq(Goods::getGshelf, goods.getGshelf());
            }
        }

        return goodsService.list(lambdaQueryWrapper);
    }

    @RequestMapping("/list_all")
    public List<Goods> list() {
        return goodsService.list();
    }
}
