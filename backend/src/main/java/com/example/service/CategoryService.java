package com.example.service;

import com.example.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 小假
* @description 针对表【Category】的数据库操作Service
* @createDate 2024-11-03 21:05:10
*/
public interface CategoryService extends IService<Category> {
    int[] get_child(int id);
    int[] get_all_child(int[] ids, List<Integer> allIds);
    int[] get_all_child(int[] ids);
}
