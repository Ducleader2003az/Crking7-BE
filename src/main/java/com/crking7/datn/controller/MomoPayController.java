package com.crking7.datn.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


import com.crking7.datn.config.MomoConfig;
import com.crking7.datn.models.OrderItem;
import com.crking7.datn.models.Orders;
import com.crking7.datn.repositories.OrdersRepository;
import com.crking7.datn.services.OrdersService;
import com.crking7.datn.utils.MomoEncoderUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/momo")
public class MomoPayController {

    private final OrdersRepository ordersRepository;

    private final OrdersService ordersService;

    public MomoPayController(OrdersRepository ordersRepository, OrdersService ordersService){
        this.ordersRepository = ordersRepository;
        this.ordersService = ordersService;
    }

    @GetMapping(value = "/test")
    public String test() {
        return "Test";
    }

    // tạo thanh toán, response trả về pay url
    @PostMapping(value = "/create-order")
    public Map<String, Object> createPayment(@RequestParam("orderId") Long orderId) throws JSONException, IOException, NoSuchAlgorithmException, InvalidKeyException {

        Orders orders = ordersRepository.findById(orderId).orElseThrow();
        String amount = String.valueOf((totalOrderAmount(orders) + orders.getShippingFee()));
        JSONObject json = new JSONObject();
        String partnerCode = MomoConfig.PARTNER_CODE;
        String accessKey = MomoConfig.ACCESS_KEY;
        String secretKey = MomoConfig.SECRET_KEY;
        String returnUrl = MomoConfig.REDIRECT_URL;
        String notifyUrl = MomoConfig.NOTIFY_URL;
        json.put("partnerCode", partnerCode);
        json.put("accessKey", accessKey);
        json.put("requestId", String.valueOf(System.currentTimeMillis()));
        json.put("amount", amount.toString());
        json.put("orderId", orderId.toString());
        json.put("orderInfo", "Thanh toan don hang " + orderId.toString());
        json.put("returnUrl", returnUrl);
        json.put("notifyUrl", notifyUrl);
        json.put("requestType", "captureMoMoWallet");

        String data = "partnerCode=" + partnerCode
                + "&accessKey=" + accessKey
                + "&requestId=" + json.get("requestId")
                + "&amount=" + amount.toString()
                + "&orderId=" + json.get("orderId")
                + "&orderInfo=" + json.get("orderInfo")
                + "&returnUrl=" + returnUrl
                + "&notifyUrl=" + notifyUrl
                + "&extraData=";

        String hashData = MomoEncoderUtils.signHmacSHA256(data, secretKey);
        json.put("signature", hashData);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(MomoConfig.CREATE_ORDER_URL);
        StringEntity stringEntity = new StringEntity(json.toString());
        post.setHeader("content-type", "application/json");
        post.setEntity(stringEntity);

        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }
        JSONObject result = new JSONObject(resultJsonStr.toString());
        Map<String, Object> kq = new HashMap<>();
        if (result.get("errorCode").toString().equalsIgnoreCase("0")) {
            kq.put("requestType", result.get("requestType"));
            kq.put("orderId", result.get("orderId"));
            kq.put("payUrl", result.get("payUrl"));
            kq.put("signature", result.get("signature"));
            kq.put("requestId", result.get("requestId"));
            kq.put("errorCode", result.get("errorCode"));
            kq.put("message", result.get("message"));
            kq.put("localMessage", result.get("localMessage"));
        } else {
            kq.put("requestType", result.get("requestType"));
            kq.put("orderId", result.get("orderId"));
            kq.put("signature", result.get("signature"));
            kq.put("requestId", result.get("requestId"));
            kq.put("errorCode", result.get("errorCode"));
            kq.put("message", result.get("message"));
            kq.put("localMessage", result.get("localMessage"));
        }
        return kq;
    }

    // truy vấn lại trạng thái thanh toán
    @PostMapping(value = "/transactionStatus")
    public Map<String, Object> transactionStatus(@RequestParam String requestId,
                                                 @RequestParam String orderId)
            throws InvalidKeyException, NoSuchAlgorithmException, ClientProtocolException, IOException, JSONException {
        JSONObject json = new JSONObject();
        String partnerCode = MomoConfig.PARTNER_CODE;
        String accessKey = MomoConfig.ACCESS_KEY;
        String secretKey = MomoConfig.SECRET_KEY;
        json.put("partnerCode", partnerCode);
        json.put("accessKey", accessKey);
        json.put("requestId", requestId);
        json.put("orderId", orderId);
        json.put("requestType", "transactionStatus");

        String data = "partnerCode=" + partnerCode + "&accessKey=" + accessKey + "&requestId=" + json.get("requestId")
                + "&orderId=" + json.get("orderId") + "&requestType=" + json.get("requestType");
        String hashData = MomoEncoderUtils.signHmacSHA256(data, secretKey);
        json.put("signature", hashData);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(MomoConfig.CREATE_ORDER_URL);
        StringEntity stringEntity = new StringEntity(json.toString());
        post.setHeader("content-type", "application/json");
        post.setEntity(stringEntity);

        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }
        JSONObject result = new JSONObject(resultJsonStr.toString());
        Map<String, Object> kq = new HashMap<>();
        kq.put("requestId", result.get("requestId"));
        kq.put("orderId", result.get("orderId"));
        kq.put("extraData", result.get("extraData"));
        kq.put("amount", Long.parseLong(result.get("amount").toString()));
        kq.put("transId", result.get("transId"));
        kq.put("payType", result.get("payType"));
        kq.put("errorCode", result.get("errorCode"));
        kq.put("message", result.get("message"));
        kq.put("localMessage", result.get("localMessage"));
        kq.put("requestType", result.get("requestType"));
        kq.put("signature", result.get("signature"));
        return kq;
    }
    private int totalOrderAmount(Orders orders) {
        int totalAmount = 0;
        for (OrderItem item : orders.getOrderItems()) {
            totalAmount += item.getQuantity() * item.getSellPrice();
        }
        return totalAmount;
    }
}
