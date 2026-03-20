package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.DTO.Response;
import com.example.domain.User;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    //查
    @RequestMapping("/list")
    public List<User> list(@RequestBody(required = false) User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (user != null) {
            // 动态添加查询条件，只有属性不为 null 时才添加
            if (user.getUaccount() != null) {
                lambdaQueryWrapper.eq(User::getUaccount, user.getUaccount());
            }
            if (user.getUpassword() != null) {
                lambdaQueryWrapper.eq(User::getUpassword, user.getUpassword());
            }
            if (user.getUphone() != null) {
                lambdaQueryWrapper.eq(User::getUphone, user.getUphone());
            }
            if (user.getUcategory() != null) {
                lambdaQueryWrapper.eq(User::getUcategory, user.getUcategory());
            }

        }
        return userService.list(lambdaQueryWrapper);
    }

    @RequestMapping("/get_by_account/{account}")
    public User get_by_account(@PathVariable String account) {
        return userService.getById(account);
    }

    //增
    @RequestMapping("/create")
    public Response create(@RequestBody User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUaccount, user.getUaccount());
        if (!userService.list(lambdaQueryWrapper).isEmpty()) {
            return new Response(500, "注册失败", null); // 假设Response构造函数接受消息和状态码
        } else {
            if (userService.save(user)) {
                return new Response(200, "注册成功", user);
            } else {
                return new Response(401, "账号已存在", null); // 假设Response构造函数接受消息和状态码
            }
        }
    }

    //改
    @RequestMapping("/update")
    public boolean Update_Pwd(@RequestBody User user) {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getUaccount, user.getUaccount());
        return userService.update(user, lambdaUpdateWrapper);
    }

    @RequestMapping("/reset_admin")
    public boolean reset() {
        User user = new User();
        user.setUaccount("admin123");
        user.setUpassword("admin123");
        return userService.saveOrUpdate(user);
    }
}
