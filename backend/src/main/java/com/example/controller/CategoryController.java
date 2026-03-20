package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.domain.Category;
import com.example.service.CategoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cat")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @RequestMapping("/list_cid_by_parent")
    public int[] list_cid_by_parent(@RequestBody Map<String, Object> catMap) {
        Object idObject = catMap.get("id");
        if (idObject == null) {
            throw new IllegalArgumentException("The 'id' field is required.");
        }
        // 确保 idObject 是数组类型
        if (!(idObject instanceof List)) {
            throw new IllegalArgumentException("The 'id' field must be an array of integers.");
        }
        List<Integer> idList = (List<Integer>) idObject;
        int[] id = idList.stream().mapToInt(i -> i).toArray();
        ArrayList<Integer> rs = new ArrayList<>();
        for (int i = 0; i < id.length; i++) {
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Category::getCparentid, id[i]);
            List<Category> categories = categoryService.list(queryWrapper);
            for (Category category : categories) {
                rs.add(category.getCid());
            }
        }
        int[] resultArray = new int[rs.size()];
        for (int i = 0; i < rs.size(); i++) {
            resultArray[i] = rs.get(i);
        }
        return resultArray;
    }

    @RequestMapping("/list_all_child")
    public int[] list_all_child(@RequestBody Map<String, Object> catMap) {
        Object idObject = catMap.get("id");
        if (idObject == null) {
            throw new IllegalArgumentException("The 'id' field is required.");
        }
        // 确保 idObject 是数组类型
        if (!(idObject instanceof List)) {
            throw new IllegalArgumentException("The 'id' field must be an array of integers.");
        }
        List<Integer> idList = (List<Integer>) idObject;
        int[] id = idList.stream().mapToInt(i -> i).toArray();
        return categoryService.get_all_child(id);
    }

    @RequestMapping("/list")
    public List<Category> list(@RequestBody(required = false) Category category) {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (category != null) {

            // 动态添加查询条件，只有属性不为 null 时才添加
            if (category.getCid() != null) {
                lambdaQueryWrapper.eq(Category::getCid, category.getCid());
            }
            if (category.getCname() != null) {
                lambdaQueryWrapper.eq(Category::getCname, category.getCname());
            }
            if (category.getCparentid() != null) {
                lambdaQueryWrapper.eq(Category::getCparentid, category.getCparentid());
            }
        }

        return categoryService.list(lambdaQueryWrapper);
    }

    @RequestMapping("/creat")
    public boolean creat(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @RequestMapping("/delete/{id}")
    public boolean delete(@PathVariable int id) {
        return categoryService.removeById(id);
    }

    @RequestMapping("/get_by_cid/{id}")
    public Category get_by_cid(@PathVariable int id) {
        return categoryService.getById(id);
    }
}
