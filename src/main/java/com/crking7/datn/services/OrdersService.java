package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.*;
import com.crking7.datn.web.dto.response.OrdersResponse;
import com.crking7.datn.web.dto.response.ProductResponse;
import org.springframework.data.util.Pair;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrdersService {
    OrdersResponse getOrderByType(long userId, int type);

    List<OrdersResponse> getOrders(long userId, int type);

    OrdersResponse getOrder(long orderId);

    List<OrdersResponse> getAllOrder(String keyword, int pageNo, int pageSize, String sortBy);

    Object addItemToCart(long userId, OrderItemRequest orderItemRequest);

    Object deleteItemFromCart(Long orderItemId);

    Object delete1Item(Long orderItemId);

    Object plus1Item(Long orderItemId);

    Object checkCreateOrder(Long orderId);

    Object createOrder(Long orderId, OrdersRequest ordersRequest);

    Object cancelOrder(CancelOrdersRequest cancelOrdersRequest);

    Object confirmOrder(UDOrdersRequest udOrdersRequest);

    Object shipOrder(UDOrdersRequest udOrdersRequest);

    Object successOrder(UDOrdersRequest udOrdersRequest);

    Object cancelOrder(UDOrdersRequest udOrdersRequest);

    Pair<List<OrdersResponse>, Integer> getOrder(String keyword, Integer status,Boolean isCheckout, String startDate, String endDate, int pageNo, int pageSize, String sortBy, boolean desc);

    String updateOrder(UDOrdersRequestAdmin udOrdersRequestAdmin);

    Long countOrders(Integer status);

    Long getTotalSoldProducts();

    double conversionRateType();

    long averageProcessingTime(int status);

    Long totalInCome();

    Long totalOrderNoProcess();

    List<Map<String, Object>> getOrderByMonth(int status);

}
