package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.Goods;
import com.example.service.GoodsService;
import com.example.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

/**
* @author 小假
* @description 针对表【Goods】的数据库操作Service实现
* @createDate 2024-11-03 21:05:15
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

}




