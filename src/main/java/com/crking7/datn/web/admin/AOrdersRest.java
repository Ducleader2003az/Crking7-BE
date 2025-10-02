package com.crking7.datn.web.admin;

import com.crking7.datn.config.Constants;
import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.services.InvoiceRedisService;
import com.crking7.datn.services.OrdersService;
import com.crking7.datn.services.impl.InvoiceRedisServiceImpl;
import com.crking7.datn.utils.PDFGenerator;
import com.crking7.datn.web.dto.request.*;
import com.crking7.datn.web.dto.response.InvoiceResponse;
import com.crking7.datn.web.dto.response.OrdersResponse;
import com.crking7.datn.web.dto.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/order")
@RequiredArgsConstructor
public class AOrdersRest {
    private final OrdersService ordersService;

    private final InvoiceRedisServiceImpl invoiceRedisService;

    private PDFGenerator pdfGenerator = new PDFGenerator();

    @PostMapping("/create/{id}")
    public ResponseEntity<?> createOrder(@PathVariable("id") Long orderId,
                                         @RequestBody OrdersRequest ordersRequest) {
        try {
            Object object = ordersService.createOrder(orderId, ordersRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đặt hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmOrder(
            @RequestBody UDOrdersRequest udOrdersRequest) {
        try {
            Object object = ordersService.confirmOrder(udOrdersRequest);
            if (object == "1") {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thành công", "Đơn hàng này đã được xác nhận"), HttpStatus.OK);
            }
            if (object == "2") {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thành công", "Bạn phải kiểm tra và xác nhận đơn hàng trước khi chuyển đi"), HttpStatus.OK);
            }
            if (object == "3") {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thành công", "Đơn hàng này đã được giao thành công"), HttpStatus.OK);
            }
            if (object == "4") {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thành công", "Đơn hàng này khách hàng đã từ chối nhận"), HttpStatus.OK);
            }
            if (object == "5") {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thành công", "Đơn hàng này đã được hủy bời khách hàng"), HttpStatus.OK);
            }
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/shipping")
    public ResponseEntity<?> shipOrder(
            @RequestBody UDOrdersRequest udOrdersRequest) {
        try {
            Object object = ordersService.shipOrder(udOrdersRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/success")
    public ResponseEntity<?> successOrder(
            @RequestBody UDOrdersRequest udOrdersRequest) {
        try {
            Object object = ordersService.successOrder(udOrdersRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/empcancel")
    public ResponseEntity<?> cancelOrder(
            @RequestBody UDOrdersRequest udOrdersRequest) {
        try {
            Object object = ordersService.cancelOrder(udOrdersRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", object), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateOrder(
            @RequestBody UDOrdersRequestAdmin udOrdersRequest) {
        try {
            String s = ordersService.updateOrder(udOrdersRequest);
            if (Objects.equals(s, "1")) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thành công", "Đơn hàng này khách hàng đã hủy"), HttpStatus.OK);
            }
            if (Objects.equals(s, "2")) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thành công", "Đơn hàng này đang được vận chuyển"), HttpStatus.OK);
            }
            if (Objects.equals(s, "3")) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thành công", "Đơn hàng này đã được giao thành công"), HttpStatus.OK);
            }
            if (Objects.equals(s, "4")) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thành công", "Đơn hàng này khách hàng đã từ chối nhận"), HttpStatus.OK);
            }
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/countOrders")
    public ResponseEntity<?> countOrders(@RequestParam(required = false) Integer status) {
        try {
            Long countOrders = ordersService.countOrders(status);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", countOrders), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/totalInCome")
    public ResponseEntity<?> totalInCome() {
        try {
            Long totalInCome = ordersService.totalInCome();
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", totalInCome), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }
    @GetMapping("/totalOrderNoProcess")
    public ResponseEntity<?> totalOrderNoProcess() {
        try {
            Long totalOrderNoProcess = ordersService.totalOrderNoProcess();
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", totalOrderNoProcess), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/totalSold")
    public ResponseEntity<?> getTotalSoldProducts() {
        try {
            Long t = ordersService.getTotalSoldProducts();
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", t), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getOrder(@RequestParam(required = false) String keyword,
                                      @RequestParam(value = "status", required = false) Integer status,
                                      @RequestParam(value = "isCheckout", required = false) Boolean isCheckout,
                                      @RequestParam(value = "startDate", required = false ) String startDate,
                                      @RequestParam(value = "endDate", required = false) String endDate,
                                      @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                      @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {

        try {
            Pair<List<OrdersResponse>, Integer> result  = ordersService.getOrder(keyword, status,isCheckout, startDate, endDate, pageNo, pageSize, sortBy, sortDirection.equals("desc"));
            List<OrdersResponse> ordersResponses = result.getFirst();
            int total = result.getSecond();
            if (!ordersResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(ordersResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/conversionRate")
    public ResponseEntity<?> conversionRate() {
        try {
            double d = ordersService.conversionRateType();
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", d), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/averageProcessingTime")
    public ResponseEntity<?> conversionRate(@RequestParam(name = "status") int status) {
        try {
            long l = ordersService.averageProcessingTime(status);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", l), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/allOrder")
    public ResponseEntity<?> getAllOrder(@RequestParam(required = false) String keyword,
                                         @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            List<OrdersResponse> ordersResponses = ordersService.getAllOrder(keyword, pageNo, pageSize, sortBy);
            if (ordersResponses != null) {
                int total = ordersResponses.size();
                List<Object> data = new ArrayList<>(ordersResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                int total = 0;
                return new ResponseEntity<>(ApiResponsePage.build(200, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable(name = "id") Long orderId) {
        try {
            OrdersResponse ordersResponse = ordersService.getOrder(orderId);
            if (ordersResponse != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", ordersResponse), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(ApiResponse.build(200, false, "Thất bại", "Có lỗi xảy ra"), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }
    @GetMapping("/getOrderByMonth")
    public ResponseEntity<?> getOrderByMonth(@RequestParam int status) {
        try {
            List<Map<String, Object>> order = ordersService.getOrderByMonth(status);
            if (order != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", order), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, false, "Thất bại", "Có lỗi xảy ra"), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/createInvoiceRedis")
    public ResponseEntity<?> createInvoiceRedis(@RequestBody AddInvoiceRequest addInvoiceRequest
    ){
        try {
            invoiceRedisService.addProductToInvoice(addInvoiceRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", addInvoiceRequest), HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Thêm hóa đơn không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/getAllInvoices")
    public ResponseEntity<?> getAllInvoiceRedis(){
        try {
            List<InvoiceResponse> invoices = invoiceRedisService.getAllInvoices();
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", invoices), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xảy ra lỗi trong khi lấy dữ liệu!  " + e.getMessage());
        }
    }

    @GetMapping("/searchInvoices")
    public ResponseEntity<?> searchInvoices(@RequestParam String keyword){
        try {
            List<InvoiceResponse> invoices = invoiceRedisService.searchInvoices(keyword);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", invoices), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xảy ra lỗi trong khi lấy dữ liệu!  " + e.getMessage());
        }
    }

    @PostMapping("/getInvoiceByCode")
    public ResponseEntity<?> getInvoiceByCode(@RequestParam String code){
        try {
            InvoiceResponse invoice = invoiceRedisService.getInvoiceByCode(code);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", invoice), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xảy ra lỗi trong khi lấy dữ liệu!  " + e.getMessage());
        }
    }

    @PostMapping("/updateInvoiceByCode")
    public ResponseEntity<?> updateInvoiceByCode(@RequestBody AddInvoiceRequest addInvoiceRequest){
        try {
            InvoiceResponse invoice = invoiceRedisService.updateInvoiceByCode(addInvoiceRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", invoice), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xảy ra lỗi trong khi lấy dữ liệu!  " + e.getMessage());
        }
    }


    @PostMapping("/printBill")
    public ResponseEntity<?> printBill(@RequestBody AddInvoiceRequest addInvoiceRequest){
        try {
            byte[] pdf = pdfGenerator.generateInvoicePdf(addInvoiceRequest);
            String pdfBase64 = Base64.getEncoder().encodeToString(pdf);

            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", pdfBase64), HttpStatus.OK);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xảy ra lỗi trong khi in hóa đơn " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteInvoiceByCode")
    public ResponseEntity<?> deleteInvoiceByCode(@RequestParam String code){
        try {
            invoiceRedisService.deleteInvoiceByCode(code);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", true), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xảy ra lỗi trong khi xóa hóa đơn " + e.getMessage());
        }
    }

    @PostMapping("/payInvoice")
    public ResponseEntity<?> payInvoice(@RequestBody AddInvoiceRequest addInvoiceRequest){
        try {
            invoiceRedisService.payInvoice(addInvoiceRequest);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", true), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xảy ra lỗi trong khi thanh toán hóa đơn " + e.getMessage());

        }
    }
}
