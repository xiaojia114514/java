package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.domain.Address;
import com.example.service.AddressService;
import com.example.mapper.AddressMapper;
import org.springframework.stereotype.Service;

/**
* @author 小假
* @description 针对表【Address】的数据库操作Service实现
* @createDate 2025-03-25 16:39:41
*/
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address>
    implements AddressService{

}




