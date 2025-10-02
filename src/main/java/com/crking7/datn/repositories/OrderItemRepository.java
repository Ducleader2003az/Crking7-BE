package com.crking7.datn.repositories;

import com.crking7.datn.models.Size;
import com.crking7.datn.models.OrderItem;
import com.crking7.datn.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByProductNameAndOrders(String productName, Orders checkOrders);
    long countByOrders(Orders orders);
    List<OrderItem> findByOrders(Orders orders);
}
