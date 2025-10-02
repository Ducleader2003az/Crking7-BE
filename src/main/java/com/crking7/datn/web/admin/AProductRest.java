package com.crking7.datn.web.admin;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.models.Product;
import com.crking7.datn.services.ProductService;
import com.crking7.datn.web.dto.request.ProductRequest;
import com.crking7.datn.web.dto.request.ProductUDRequest;
import com.crking7.datn.web.dto.response.OrdersResponse;
import com.crking7.datn.web.dto.response.ProductResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin/product")
public class AProductRest {
    private final ProductService productService;

    public AProductRest(ProductService productService) {
        this.productService = productService;
    }

    /**
     * create product
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductAdmin(@PathVariable("id") Long productId) {
        try {
            ProductResponse productResponse = productService.getProductAdmin(productId);
            if (productResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        try {
            ProductResponse product = productService.getProductByName(productRequest.getName());
            if (product == null) {
                ProductResponse productResponse = productService.createProduct(productRequest);
                if (productResponse != null) {
                    return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Tạo mới không thành công"), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Tên sản phẩm đã tồn tại"), HttpStatus.OK);
            }
        } catch (Exception e) {
            String errorMessage = "Không thể tạo mới sản phẩm, lỗi: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductUDRequest productRequest) {
        try {
            ProductResponse product = productService.getProductByName(productRequest.getName());
            ProductResponse product2 = productService.getProductAdmin(productRequest.getId());
            if (product != null) {
                // Nếu sản phẩm đã tồn tại theo tên
                if (product2 != null && product2.getId() == product.getId()) {
                    // Sản phẩm2 chính là sản phẩm cần cập nhật, không cần cảnh báo
                    ProductResponse productResponse = productService.updateProduct(productRequest);
                    if (productResponse != null) {
                        return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", productResponse), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Cập nhật không thành công"), HttpStatus.OK);
                    }
                } else {
                    // Tên sản phẩm đã tồn tại và không phải là sản phẩm cần cập nhật
                    return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Tên sản phẩm đã tồn tại"), HttpStatus.OK);
                }
            } else {
                // Sản phẩm không tồn tại theo tên, tiến hành cập nhật
                ProductResponse productResponse = productService.updateProduct(productRequest);
                if (productResponse != null) {
                    return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", productResponse), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Cập nhật không thành công"), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            String errorMessage = "Không thể cập nhật sản phẩm, lỗi: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hideProduct(@PathVariable("id") long id) {
        try {
            ProductResponse productResponse = productService.hideProduct(id);
            if (productResponse != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Ẩn sản phẩm không thành công"), HttpStatus.OK);
            }
        } catch (Exception e) {
            String errorMessage = "Không thể ẩn sản phẩm, lỗi: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/show/{id}")
    public ResponseEntity<?> showProduct(@PathVariable("id") long id) {
        try {
            ProductResponse productResponse = productService.showProduct(id);
            if (productResponse != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Hiện sản phẩm không thành công"), HttpStatus.OK);
            }
        } catch (Exception e) {
            String errorMessage = "Không thể hiện sản phẩm, lỗi: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable(name = "id") long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", "Xóa sản phẩm thành công"), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa sản phẩm không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/quantity")
    public ResponseEntity<?> getProductByQuantity(@RequestParam(value = "isActive", defaultValue = "true") boolean isActive,
                                                  @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                  @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                  @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            Pair<List<ProductResponse>, Integer> result = productService.getProductByQuantity(isActive, pageNo, pageSize, sortBy);
            List<ProductResponse> productResponses = result.getFirst();
            int total = result.getSecond();
            if (!productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllProducts(@RequestParam(required = false) String keyword,
                                            @RequestParam(value = "status", required = false) Integer status,
                                            @RequestParam(value = "minPrice", required = false) Integer minPrice,
                                            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
                                            @RequestParam(value = "categoryId", required = false) Long categoryId,
                                            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {

        try {
            Pair<List<ProductResponse>, Integer> result = productService.getALLProductsAdmin(keyword, status, minPrice, maxPrice, categoryId, pageNo, pageSize, sortBy, sortDirection.equals("desc"));
            List<ProductResponse> productResponses = result.getFirst();
            int total = result.getSecond();
            if (!productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponsePage.builder(500, false, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/productSale")
    public ResponseEntity<?> getProductBySaleAdmin(@RequestParam(required = false) String keyword,
                                                   @RequestParam(value = "saleId") Long saleId) {
        try {
            List<ProductResponse> productResponse = productService.getProductBySaleAdmin(keyword, saleId);
            if (productResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/productNoSale")

    public ResponseEntity<?> getProductNoSale(@RequestParam(required = false) String keyword,
                                              @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            Pair<List<ProductResponse>, Integer> result = productService.getProductNoSale(keyword, pageNo, pageSize, sortBy);
            List<ProductResponse> productResponses = result.getFirst();
            int total = result.getSecond();
            if (!productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponsePage.builder(500, false, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/totalProduct")
    public ResponseEntity<?> totalProduct() {
        try {
            Long totalProduct = productService.totalProduct();
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", totalProduct), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hủy đơn hàng không thành công! Lỗi " + e.getMessage());
        }
    }
}
