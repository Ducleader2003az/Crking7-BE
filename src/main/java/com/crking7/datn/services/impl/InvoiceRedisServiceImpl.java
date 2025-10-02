package com.crking7.datn.services.impl;

import com.crking7.datn.config.Constants;
import com.crking7.datn.models.OrderItem;
import com.crking7.datn.models.Orders;
import com.crking7.datn.models.Product;
import com.crking7.datn.repositories.OrderItemRepository;
import com.crking7.datn.repositories.OrdersRepository;
import com.crking7.datn.services.InvoiceRedisService;
import com.crking7.datn.utils.Utils;
import com.crking7.datn.web.dto.request.AddInvoiceRequest;
import com.crking7.datn.web.dto.request.OrderItemRequest;
import com.crking7.datn.web.dto.request.OrdersRequest;
import com.crking7.datn.web.dto.response.InvoiceResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InvoiceRedisServiceImpl extends BaseRedisServiceImpl implements InvoiceRedisService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private Jedis jedis = new Jedis();

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${redis.prefixInvoice}")
    private String prefixKey;

    @Value("${redis.expire}")
    private int expire;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    public InvoiceRedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    public void addProductToInvoice(AddInvoiceRequest addInvoiceRequest) {
        String invoiceCode  = prefixKey + addInvoiceRequest.getCode();
        String invoiceJson;
        try {
            invoiceJson = objectMapper.writeValueAsString(addInvoiceRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        this.set(invoiceCode, invoiceJson);
        this.setTimeToLive(invoiceCode, expire);
    }

    @Override
    public void updateProductFromInvoice(String invoiceCode, List<Product> productList) {

    }

    @Override
    public void deleteProductFromInvoice(String invoiceCode, List<Product> productList) {

    }

    @Override
    public Map<String, Integer> getProductFromInvoice(String invoiceCode) {
        return Map.of();
    }

    @Override
    public List<InvoiceResponse> getAllInvoices() {
        Set<String> keys = this.getFieldPrefixes(prefixKey);
        List<InvoiceResponse> invoiceResponses = new ArrayList<>();

        for (String key : keys){
            Object field = this.get(key);
            InvoiceResponse data = null;
//            int ttl =(int) jedis.ttl(key);
            try {
                data = objectMapper.readValue(String.valueOf(field), InvoiceResponse.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            data.setCode(key.substring(prefixKey.length()));
//            data.setExpireTime(ttl);

            invoiceResponses.add(data);
        }

        return invoiceResponses;
    }

    public InvoiceResponse getInvoiceByCode(String code) {
        Object invoice = this.get(prefixKey + code);
//        int ttl =(int) jedis.ttl(prefixKey + code);

        try {
                InvoiceResponse data = objectMapper.readValue(String.valueOf(invoice), InvoiceResponse.class);
//                data.setExpireTime(ttl);
                data.setCode(code);
                return data;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
    }

    public InvoiceResponse updateInvoiceByCode(AddInvoiceRequest addInvoiceRequest) {
        try {
            String invoiceCode  = prefixKey + addInvoiceRequest.getCode();
            String invoiceJson;
            InvoiceResponse invoice = InvoiceResponse.builder()
                    .code(addInvoiceRequest.getCode())
                    .fullName(addInvoiceRequest.getFullName())
                    .phone(addInvoiceRequest.getPhone())
                    .addressDetail(addInvoiceRequest.getAddressDetail())
                    .wards(addInvoiceRequest.getWards())
                    .province(addInvoiceRequest.getProvince())
                    .items(addInvoiceRequest.getItems())
                    .district(addInvoiceRequest.getDistrict())
                    .build();
            try {
                invoiceJson = objectMapper.writeValueAsString(addInvoiceRequest);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            int ttl =(int) jedis.ttl(invoiceCode);
            this.set(invoiceCode, invoiceJson);
            if (ttl > 0) {
                jedis.expire(invoiceCode, ttl);
            }
            return invoice;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteInvoiceByCode(String code) throws Exception {
        try {
            this.delete(prefixKey +  code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void payInvoice(AddInvoiceRequest addInvoiceRequest){
        try {
            Orders order = Orders.builder()
                    .fullName(addInvoiceRequest.getFullName())
                    .phone(addInvoiceRequest.getPhone())
                    .isCheckout(true)
                    .note(addInvoiceRequest.getNote())
                    .codeOrders(addInvoiceRequest.getCode())
                    .paymentMethod(Constants.PAY_AT_STORE)
                    .status(Constants.DELIVERED_STATUS)
                    .createDate(new Date())
                    .orderDate(new Date())
                    .type(Constants.ORDERS_TYPE)
                    .build();

            Orders orderInserted =  ordersRepository.save(order);

            List<OrderItem> orderItems = addInvoiceRequest.getItems().stream()
                    .map(o ->{
                        OrderItem orderItem = OrderItem.builder()
                                .productName(o.getProductName())
                                .valueColor(o.getValueColor())
                                .valueSize(o.getValueSize())
                                .quantity(o.getQuantity())
                                .sellPrice(o.getSellPrice())
                                .orders(orderInserted)
                                .build();
                        return orderItem;
                    })
                    .toList();

            for (OrderItem orderItem : orderItems){
                orderItemRepository.save(orderItem);
            }

            this.delete(prefixKey +  addInvoiceRequest.getCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<InvoiceResponse> searchInvoices(String keyword) {
        Set<String> keys = this.getFieldPrefixes(prefixKey);
        List<InvoiceResponse> invoiceResponses = new ArrayList<>();

        for (String key : keys){
            Object field = this.get(key);
            InvoiceResponse data = null;
//            int ttl =(int) jedis.ttl(key);
            try {
                data = objectMapper.readValue(String.valueOf(field), InvoiceResponse.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            data.setCode(key.substring(prefixKey.length()));
//            data.setExpireTime(ttl);

            if (data.getCode().contains(keyword) || data.getFullName().contains(keyword) || data.getPhone().contains(keyword)){
                invoiceResponses.add(data);
            }
        }

        return invoiceResponses;
    }


}
