package com.example.service;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.domain.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.bind.annotation.*;

@Service
public class StripeService {

    @Resource
    private OrderService orderService;
    // 创建支付意图
    @Value("${stripe.secret-key}")
    private String secretKey;

    public PaymentIntent createPaymentIntent(Integer amount, String currency, String orderId) throws StripeException {
        Stripe.apiKey = secretKey;
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(Long.valueOf(amount))
                .setCurrency(currency)
                .putMetadata("orderId", orderId) // 将orderId存入元数据
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        return PaymentIntent.create(params);
    }

    // 确认支付
    @PostMapping("/create-payment-intent")
    public PaymentIntent confirmPaymentIntent(String paymentIntentId) throws StripeException {
        return PaymentIntent.retrieve(paymentIntentId);
    }

    // 处理支付结果
    public String handlePaymentResult(String paymentIntentId) throws StripeException {
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

        if (paymentIntent.getStatus().equals("succeeded")) {
            // 支付成功，更新订单状态
            System.out.println("Payment succeeded: " + paymentIntentId);
            String orderIdStr = paymentIntent.getMetadata().get("orderId");

            LambdaUpdateWrapper<Order> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(Order::getOid,orderIdStr);
            Order o = new Order();
            o.setOpaid(1);
            orderService.update(o,lambdaUpdateWrapper);
            return "succeeded";
        } else {
            // 支付失败，处理错误
            System.out.println("Payment failed: " + paymentIntent.getStatus());
            return "failed";
        }
    }
}