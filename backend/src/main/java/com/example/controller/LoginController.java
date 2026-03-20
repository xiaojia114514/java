package com.example.controller; // 改成自己包名

import com.example.domain.User;
import com.example.service.UserService;
import com.example.utils.JwtUtils;
import com.example.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;
    @PostMapping("/api/login")
    public Result login(@RequestBody User user){
        User u = userService.getById(user.getUaccount());
        if (u == null){
            return Result.error("用户名不存在");
        }

        // 如果登陆成功，生成令牌，下发令牌
        if (u.getUpassword().equals(user.getUpassword())){
            Map<String,Object> claims = new HashMap<>();
            claims.put("account",u.getUaccount());
            claims.put("password",u.getUpassword());
            claims.put("phone",u.getUphone());
            claims.put("Ucategory",u.getUcategory());
            String jwtcode = JwtUtils.generateJwt(claims);
            return Result.success(jwtcode);
        }
        // 登陆失败，返回错误信息
        else{
            return Result.error("用户名或密码失败~");
        }
    }
}
