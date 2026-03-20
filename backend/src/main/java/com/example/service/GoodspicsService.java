package com.example.service;

import com.example.domain.Goodspics;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 小假
* @description 针对表【GoodsPics】的数据库操作Service
* @createDate 2024-11-06 18:47:05
*/
public interface GoodspicsService extends IService<Goodspics> {

    List<byte[]> find_data(int gid);
}
