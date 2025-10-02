package com.crking7.datn.services;

import com.crking7.datn.models.Product;
import com.crking7.datn.web.dto.request.AddInvoiceRequest;
import com.crking7.datn.web.dto.request.OrderItemRequest;
import com.crking7.datn.web.dto.request.OrdersRequest;
import com.crking7.datn.web.dto.response.InvoiceResponse;

import java.util.List;
import java.util.Map;

public interface InvoiceRedisService extends BaseRedisService {
    void addProductToInvoice(AddInvoiceRequest addInvoiceRequest);

    void updateProductFromInvoice(String invoiceCode, List<Product> productList);

    void deleteProductFromInvoice(String invoiceCode, List<Product> productList);

    Map<String, Integer> getProductFromInvoice(String invoiceCode);

    List<InvoiceResponse> getAllInvoices();
}
