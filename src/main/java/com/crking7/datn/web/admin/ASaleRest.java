package com.crking7.datn.web.admin;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.models.Product;
import com.crking7.datn.services.ArticleService;
import com.crking7.datn.services.SaleService;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.request.BannerRequest;
import com.crking7.datn.web.dto.request.SaleRequest;
import com.crking7.datn.web.dto.response.*;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/sale")
public class ASaleRest {
    private final SaleService saleService;

    public ASaleRest(SaleService saleService) {
        this.saleService = saleService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getSale(@PathVariable(name = "id") Long id) {
        try {
            SaleResponse saleResponse = saleService.getSale(id);
            if (saleResponse != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", saleResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSale(@RequestBody SaleRequest saleRequest) {
        try {
            SaleResponse saleResponse = saleService.create(saleRequest);
            if (saleResponse != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", saleResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Integer isActive,
                                    @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                    @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {
        try {
            Pair<List<SaleResponse>, Integer> result = saleService.getAll(keyword, isActive, pageNo, pageSize, sortBy, sortDirection.equals("desc"));
            List<SaleResponse> saleResponses = result.getFirst();
            int total = result.getSecond();
            if (!saleResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(saleResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id,
                                    @RequestBody SaleRequest saleRequest) {
        try {
            SaleResponse sale = saleService.getSaleByName(saleRequest.getName());
            SaleResponse sale2 = saleService.getSale(id);
            if (sale != null) {
                if (sale2 != null && sale2.getId() == sale.getId()) {
                    SaleResponse saleResponse = saleService.update(id, saleRequest);
                    if (saleResponse != null) {
                        return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", saleResponse), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Cập nhật không thành công"), HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Tên danh mục đã tồn tại"), HttpStatus.OK);
                }
            } else {
                SaleResponse saleResponse = saleService.update(id, saleRequest);
                if (saleResponse != null) {
                    return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", saleResponse), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Cập nhật không thành công"), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        try {
            saleService.delete(id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", "Xóa khuyến mãi thành công"), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Xóa chương trình sale không thành công! Lỗi " + e.getMessage());
        }
    }

    @PostMapping("/add-product/{id}")
    public ResponseEntity<?> addProductsToSale(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "productIds") List<Long> productIds) {

        try {
            if (!productIds.isEmpty()) {
                String s = saleService.addProductsToSale(id, productIds);
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", s), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", "Hãy chọn ít nhất 1 sản phẩm"), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể thêm sản phẩm . Lỗi : " + e.getMessage());
        }
    }

    @PostMapping("/remove-product/{id}")
    public ResponseEntity<?> removeProductsFromSale(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "productIds") List<Long> productIds) {

        try {
            if (!productIds.isEmpty()) {
                String s = saleService.removeProductsFromSale(id, productIds);
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", s), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", "Hãy chọn ít nhất 1 sản phẩm"), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể xóa sản phẩm . Lỗi : " + e.getMessage());
        }
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hideSale(@PathVariable("id") Long id) {
        try {
            String s = saleService.hideSale(id);
            if (s == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", "Không tìm thấy danh mục"), HttpStatus.OK);
            }
            return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/show/{id}")
    public ResponseEntity<?> showSale(@PathVariable("id") Long id) {
        try {
            String s = saleService.showSale(id);
            if (s == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", "Không tìm thấy danh mục"), HttpStatus.OK);
            }
            return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
