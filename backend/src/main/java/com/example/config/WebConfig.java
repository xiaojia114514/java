package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.interceptor.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")  // 只拦截/api下的请求
                .excludePathPatterns(
                        "/api/user/create",
                        "/api/login",
                        "/api/alipay/**",
                        "/api/stripe/**",
                        "/**/options"
                );
    }
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 匹配所有非API路径，返回index.html
        registry.addViewController("/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
    }
}

