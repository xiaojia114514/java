package com.example.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.controller.AliPayController;
import com.example.domain.Alipay;
import com.example.service.AlipayService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AlipayTimeoutTask {
    @Resource
    private AlipayService aliPayService;

    private static final Logger log = LoggerFactory.getLogger(AlipayTimeoutTask.class);

    // 每分钟检查一次(可以根据需求调整频率)
    @Scheduled(fixedRate = 60 * 1000)
    public void checkAndCancelTimeoutOrders() {
        // 获取当前时间
        Date now = new Date();
        log.debug("当前时间戳(毫秒): {}", now.getTime());
        // 获取5分钟前的时间
        long fiveMinutesAgo = now.getTime() - (5 * 60 * 1000);
        Date fiveMinutesAgoDate = new Date(fiveMinutesAgo);
        log.debug("5分钟前的时间戳(毫秒): {}", fiveMinutesAgoDate.getTime());

        // 查询创建超过5分钟且未支付的订单(状态为0)
        List<Alipay> timeoutOrders = aliPayService.list(
                new LambdaQueryWrapper<Alipay>()
                        .eq(Alipay::getState, 0)
                        .lt(Alipay::getAdd_time, fiveMinutesAgo)
                        .isNotNull(Alipay::getOut_trade_no)
        );

        if (timeoutOrders == null || timeoutOrders.isEmpty()) {
            log.debug("没有需要处理的超时订单");
            return;
        }

        // 检查timeoutOrders中是否存在null元素
        if (timeoutOrders.contains(null)) {
            log.error("查询结果中包含null元素，可能存在问题");
            return;
        }

        // 批量更新
        timeoutOrders.forEach(order -> {
            if (order == null) {
                log.warn("订单对象为null，跳过处理");
                return;
            }

            boolean updated = aliPayService.update(
                    new LambdaUpdateWrapper<Alipay>()
                            .eq(Alipay::getAlid, order.getAlid())
                            .set(Alipay::getState, 2)
                            .set(Alipay::getUpdate_time, now)
            );

            if (updated) {
                log.info("订单超时未支付已取消，订单号: {}", order.getAlid());
            } else {
                log.warn("订单取消失败(可能已被处理)，订单号: {}", order.getAlid());
            }
        });
    }
}
