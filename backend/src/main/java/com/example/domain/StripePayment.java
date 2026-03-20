package com.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("stripe_payment")
public class StripePayment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String paymentIntentId;  // Stripe支付意图ID
    private String orderId;         // 业务订单ID
    private BigDecimal amount;      // 支付金额
    private String currency;        // 货币类型
    private String description;     // 支付描述
    private Integer status;         // 支付状态: 0-未支付, 1-支付成功, 2-支付失败
    private Date createTime;        // 创建时间
    private Date updateTime;        // 更新时间
    private String clientSecret;    // Stripe客户端密钥
    private String paymentMethod;   // 支付方式
    private String receiptEmail;    // 收据邮箱
}