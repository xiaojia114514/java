package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.Category;
import com.example.mapper.GoodspicsMapper;
import com.example.service.CategoryService;
import com.example.mapper.CategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 小假
* @description 针对表【Category】的数据库操作Service实现
* @createDate 2024-11-03 21:05:10
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Override
    public int[] get_child(int id){
        ArrayList<Integer> rs = new ArrayList<>();
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getCparentid, id);
        List<Category> categories = this.list(queryWrapper);
        for (Category category : categories) {
            rs.add(category.getCid());
        }
        int[] resultArray = new int[rs.size()];
        for (int i = 0; i < rs.size(); i++) {
            resultArray[i] = rs.get(i);
        }
        return resultArray;
    }
    @Override
    public int[] get_all_child(int[] ids, List<Integer> allIds){
        // 遍历输入数组中的每个ID
        for (int id : ids) {
            allIds.add(id); // 将当前ID添加到列表中
            int[] childIds = get_child(id); // 获取当前ID的子类ID数组
            if (childIds.length > 0) {
                // 如果子类ID数组不为空，则递归调用traverseCategories
                get_all_child(childIds, allIds);
            }
        }
        // 将列表转换为数组并返回
        return allIds.stream().mapToInt(i -> i).toArray();
    }
    @Override
    public int[] get_all_child(int[] ids) {
        List<Integer> allIds = new ArrayList<>(); // 用于收集所有ID的列表
        return get_all_child(ids, allIds);
    }
}




