package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.DTO.AlipaymentOrder;
import com.example.domain.Alipay;
import com.example.domain.Order;
import com.example.service.AlipayService;
import com.example.service.OrderService;
import com.example.utils.PayUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Value;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/alipay")
public class AliPayController {
    @Autowired
    private PayUtil payUtil;

    @Resource
    private AlipayService aliPayService;
    @Resource
    private OrderService orderService;
    @Value("${pay.return}")
    private String FIXED_RETURN_URL;
    @Value("${alipay.alipayPublicKey}")
    private String ALIPAY_PUBLIC_KEY;

    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";
    private static final Logger log = LoggerFactory.getLogger(AliPayController.class);
    @ResponseBody
    @PostMapping("/pay")
    public String alipay(@RequestBody AlipaymentOrder alipaymentOrder) throws AlipayApiException, UnsupportedEncodingException {
        //生成订单号（支付宝的要求？）
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String oid = String.valueOf(alipaymentOrder.getOid());
        String OrderNum = time + oid;
        System.out.println("商品名字是" + alipaymentOrder.getSubject());
        Alipay alipay = new Alipay();
        alipay.setOut_trade_no(OrderNum);
        alipay.setOid(alipaymentOrder.getOid());
        alipay.setTotalAmount(alipaymentOrder.getTotalAmount());
        alipay.setSubject(alipaymentOrder.getSubject());
        alipay.setAdd_time(new Date());
        alipay.setUpdate_time(new Date());
        alipay.setState(0);
        aliPayService.save(alipay);
        //调用封装好的方法（给支付宝接口发送请求）
        return payUtil.sendRequestToAlipay(OrderNum, alipaymentOrder.getTotalAmount(), alipaymentOrder.getSubject());

    }

    @GetMapping("/return")
    public String alipayReturn(HttpServletRequest request) {
        String outTradeNo = request.getParameter("out_trade_no");

        try {
            String queryResult = payUtil.query(outTradeNo);
            System.out.println(queryResult);
            if (queryResult == null) {
                return "redirect:" + FIXED_RETURN_URL + "?status=failed";
            }

            JSONObject jsonObject = JSONObject.parseObject(queryResult);
            JSONObject responseObj = jsonObject.getJSONObject("alipay_trade_query_response");

            if (responseObj != null && "TRADE_SUCCESS".equals(responseObj.getString("trade_status"))) {
                // 查询支付宝订单获取原始oid
                LambdaQueryWrapper<Alipay> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Alipay::getOut_trade_no, outTradeNo);
                Alipay alipay = aliPayService.getOne(queryWrapper);
                //
                LambdaUpdateWrapper<Alipay> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(Alipay::getOut_trade_no, outTradeNo)
                        .set(Alipay::getState, 1) // 1表示已支付
                        .set(Alipay::getUpdate_time, new Date());
                aliPayService.update(updateWrapper);

                LambdaUpdateWrapper<Order> updateWrapper1 = new LambdaUpdateWrapper<>();
                updateWrapper1.eq(Order::getOid,alipay.getOid()).set(Order::getOpaid,1);
                orderService.update(updateWrapper1);
                return "redirect:" + FIXED_RETURN_URL + "?status=success&oid=" + alipay.getOid();
            } else {
                return "redirect:" + FIXED_RETURN_URL + "?status=failed";
            }
        } catch (Exception e) {
            log.error("处理支付宝返回时发生异常", e);
            return "redirect:" + FIXED_RETURN_URL + "?status=error";
        }
    }

    // 处理支付宝异步通知
    @PostMapping("/notify")
    @ResponseBody
    public String alipayNotify(HttpServletRequest request) {
        // 1. 验证通知参数
        Map<String, String> params = convertRequestParamsToMap(request);

        try {
            // 2. 验证签名（重要！防止伪造通知）
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    ALIPAY_PUBLIC_KEY,
                    CHARSET,
                    SIGN_TYPE
            );

            if (!signVerified) {
                log.error("支付宝异步通知签名验证失败");
                return "failure";
            }

            // 3. 获取关键参数
            String outTradeNo = params.get("out_trade_no");
            String tradeStatus = params.get("trade_status");
            System.out.println("异步通知的订单号是" + outTradeNo);
            System.out.println("异步通知的交易状态是" + tradeStatus);
            // 4. 处理不同交易状态
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                // 支付成功处理
                LambdaUpdateWrapper<Alipay> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(Alipay::getOut_trade_no, outTradeNo)
                        .set(Alipay::getState, 1) // 1表示已支付
                        .set(Alipay::getUpdate_time, new Date());
                boolean updated = aliPayService.update(updateWrapper);

                LambdaQueryWrapper<Alipay> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Alipay::getOut_trade_no, outTradeNo);
                Alipay alipay = aliPayService.getOne(queryWrapper);

                LambdaUpdateWrapper<Order> updateWrapper1 = new LambdaUpdateWrapper<>();
                updateWrapper1.eq(Order::getOid,alipay.getOid()).set(Order::getOpaid,1);
                orderService.update(updateWrapper1);
                if (!updated) {
                    log.error("订单状态更新失败，订单号: {}", outTradeNo);
                }
                return "success";
            }
        } catch (Exception e) {
            log.error("系统处理支付宝通知异常", e);
        }
        return "failure";
    }

    // 将请求参数转换为Map
    private Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String[]> requestParams = request.getParameterMap();
        Map<String, String> params = new HashMap<>();

        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        return params;
    }

    // 添加订单查询接口
    @GetMapping("/queryOrder/{outTradeNo}")
    @ResponseBody
    public List<Alipay> queryOrder(@PathVariable String outTradeNo) {
        LambdaQueryWrapper<Alipay> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Alipay::getOut_trade_no,outTradeNo);
        return aliPayService.list(lambdaQueryWrapper);
    }

}
