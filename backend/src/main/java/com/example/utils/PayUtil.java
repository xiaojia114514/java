package com.example.utils;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Component
public class PayUtil {
    @Value("${alipay.appId}")
    private String APP_ID;

    @Value("${alipay.appPrivateKey}")
    private String APP_PRIVATE_KEY;

    @Value("${alipay.alipayPublicKey}")
    private String ALIPAY_PUBLIC_KEY;

    @Value("${alipay.gatewayUrl}")
    private String GATEWAY_URL;

    @Value("${alipay.notifyUrl}")
    private String NOTIFY_URL;

    @Value("${alipay.returnUrl}")
    private String RETURN_URL;

    private final String CHARSET = "UTF-8";
    private final String FORMAT = "JSON";
    private final String SIGN_TYPE = "RSA2";
    private AlipayClient alipayClient = null;

    private static final Logger log = LoggerFactory.getLogger(PayUtil.class);

    //支付宝官方提供的接口
    public String sendRequestToAlipay(String outTradeNo, String totalAmount, String subject) throws AlipayApiException, UnsupportedEncodingException {
        //获得初始化的AlipayClient
        if (alipayClient == null) {
            alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, APP_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
        }

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(RETURN_URL);
        alipayRequest.setNotifyUrl(NOTIFY_URL);

        String out_trade_no = new String(outTradeNo.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        String total_amount = new String(totalAmount.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        String Subject = new String(subject.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        String timeout_express = "5m";
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\","
                + "\"total_amount\":\"" + total_amount + "\","
                + "\"subject\":\"" + Subject + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\","
                + "\"timeout_express\":\"" + timeout_express + "\"}");
        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        System.out.println("返回的结果是："+result );
        return result;
    }
    private String extractActionUrl(String formHtml) {
        // 简单实现，实际应用中可能需要更健壮的HTML解析
        int start = formHtml.indexOf("action=\"") + 8;
        int end = formHtml.indexOf("\"", start);
        return formHtml.substring(start, end);
    }
    //    通过订单编号查询
    public String query(String outTradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        request.setBizContent(bizContent.toString());

        try {
            // 使用execute而不是pageExecute
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("订单查询成功，订单号: {}", outTradeNo);
                return response.getBody();
            } else {
                log.error("订单查询失败，订单号: {}, 原因: {}", outTradeNo, response.getSubMsg());
                return null;
            }
        } catch (AlipayApiException e) {
            log.error("支付宝查询接口调用异常", e);
            return null;
        }
    }
}
