package com.crking7.datn.controller;

import com.crking7.datn.config.Constants;
import com.crking7.datn.config.VnpayConfig;
import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.models.OrderItem;
import com.crking7.datn.models.Orders;
import com.crking7.datn.repositories.OrdersRepository;
import com.crking7.datn.services.OrdersService;
import com.crking7.datn.utils.Utils;
import com.crking7.datn.web.dto.request.OrdersRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/vnpay")
public class VnPayController {

    private final OrdersRepository ordersRepository;

    private final OrdersService ordersService;

    public VnPayController(OrdersRepository ordersRepository, OrdersService ordersService) {
        this.ordersRepository = ordersRepository;
        this.ordersService = ordersService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> makePayment(
            @RequestParam(name = "orderId") Long orderId,
            @RequestParam(name = "bankCode", required = false) String bankCode,
            @RequestParam(name = "language", required = false, defaultValue = "vn") String language) throws IOException, UnsupportedEncodingException {

        Orders orders = ordersRepository.findById(orderId).orElseThrow();
        String orderType = "other";
        String codeOrders = Utils.getRandomNumber(8);
        orders.setCodeOrders(codeOrders);
        ordersRepository.save(orders);
        long vnp_Amount = (totalOrderAmount(orders) + orders.getShippingFee()) * 100L;
        String vnp_IpAddr = VnpayConfig.getIpAddress();

        String vnp_TmnCode = VnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnpayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnpayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", codeOrders);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + codeOrders);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Locale", language);
        vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;

        return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", paymentUrl), HttpStatus.OK);
    }

    @GetMapping("/result")
    public ResponseEntity<?> completePayment(
            @RequestParam(name = "vnp_OrderInfo") String vnp_OrderInfo,
            @RequestParam(name = "vnp_Amount") Integer vnp_Amount,
            @RequestParam(name = "vnp_BankCode", defaultValue = "") String vnp_BankCode,
            @RequestParam(name = "vnp_BankTranNo") String vnp_BankTranNo,
            @RequestParam(name = "vnp_CardType") String vnp_CardType,
            @RequestParam(name = "vnp_PayDate") String vnp_PayDate,
            @RequestParam(name = "vnp_ResponseCode") String vnp_ResponseCode,
            @RequestParam(name = "vnp_TransactionNo") String vnp_TransactionNo,
            @RequestParam(name = "vnp_TxnRef") String vnp_TxnRef
    ) throws ParseException {
        Orders orders = ordersRepository.findByCodeOrders(vnp_TxnRef);

        Map<String, String> response = new HashMap<>();

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date payDate = inputDateFormat.parse(vnp_PayDate);
        String formattedPayDate = outputDateFormat.format(payDate);

        response.put("vnp_OrderInfo", vnp_OrderInfo);
        response.put("vnp_Amount", vnp_Amount.toString());
        response.put("vnp_BankCode", vnp_BankCode);
        response.put("vnp_BankTranNo", vnp_BankTranNo);
        response.put("vnp_CardType", vnp_CardType);
        response.put("vnp_PayDate", formattedPayDate);
        response.put("vnp_ResponseCode", vnp_ResponseCode);
        response.put("vnp_TransactionNo", vnp_TransactionNo);
        response.put("vnp_TxnRef", vnp_TxnRef);
        // Cập nhật trạng thái isCheckout và hình thức thanh toán
        if ("00".equals(vnp_ResponseCode)) {
            // "00" là mã trả về cho thanh toán thành công
            orders.setPaymentMethod("vnPay");
            // Lưu thông tin vào cơ sở dữ liệu
            ordersRepository.save(orders);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", response), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ApiResponse.build(200, false, "Thành công", response), HttpStatus.OK);
        }
    }

    private int totalOrderAmount(Orders orders) {
        int totalAmount = 0;
        for (OrderItem item : orders.getOrderItems()) {
            totalAmount += item.getQuantity() * item.getSellPrice();
        }
        return totalAmount;
    }
}

