package com.example.controller;

import com.example.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/create-payment-intent")
    public Map<String, String> createPaymentIntent(@RequestBody Map<String, Object> request) throws StripeException {
        Integer amount = (Integer) request.get("amount");
        String currency = (String) request.get("currency");
        String orderId = (String) request.get("orderId");
        PaymentIntent paymentIntent = stripeService.createPaymentIntent(amount, currency, orderId);

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());
        return response;
    }
    @GetMapping("/payment-status/{paymentIntentId}")
    public String getPaymentStatus(@PathVariable("paymentIntentId") String paymentIntentId) throws StripeException {

        return stripeService.handlePaymentResult(paymentIntentId);
    }
}