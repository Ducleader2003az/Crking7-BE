package com.crking7.datn.web;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.models.Product;
import com.crking7.datn.services.ProductService;
import com.crking7.datn.web.dto.response.CategoryResponse;
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
@RequestMapping("/api/product")
public class ProductRest {
    private final ProductService productService;

    public ProductRest(ProductService productService) {
        this.productService = productService;
    }

    /**
     * get products and pagination
     */
    @GetMapping("")
    public ResponseEntity<?> getProducts(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        try {
            Pair<List<ProductResponse>, Integer> result = productService.getProducts(pageNo, pageSize, sortBy);
            List<ProductResponse> productResponses = result.getFirst();
            int total = result.getSecond();
            if (!productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponsePage.builder(200, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/allProduct")
    public ResponseEntity<?> getALLProducts(@RequestParam(value = "valueSize", required = false) List<String> valueSize,
                                            @RequestParam(value = "valueColor", required = false) List<String> valueColor,
                                            @RequestParam(value = "minPrice", required = false) Integer minPrice,
                                            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
                                            @RequestParam(value = "categoryId", required = false) Long categoryId,
                                            @RequestParam(value = "saleId", required = false) Long saleId,
                                            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {

        try {
            Pair<List<ProductResponse>, Integer> result = productService.getALLProducts(valueSize, valueColor, minPrice, maxPrice, categoryId, saleId, pageNo, pageSize, sortBy, sortDirection.equals("desc"));
            List<ProductResponse> productResponses = result.getFirst();
            int total = result.getSecond();
            if (!productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    /**
     * get products by categoryId and pagination
     */
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable("id") Long categoryId,
                                                   @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                   @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            Pair<List<ProductResponse>, Integer> result = productService.getProductsByCategory(categoryId, pageNo, pageSize, sortBy);
            List<ProductResponse> productResponses = result.getFirst();
            int total = result.getSecond();
            if (!productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponsePage.builder(200, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/search")
    public ResponseEntity<?> getProductByName(@RequestParam(value = "keyword") String keyword,
                                              @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                              @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {

        try {
            Pair<List<ProductResponse>, Integer> result = productService.getProductsByKeyword(keyword, pageNo, pageSize, sortBy);
            List<ProductResponse> productResponses = result.getFirst();
            int total = result.getSecond();
            if (!productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponsePage.builder(200, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/searchProduct")
//    public ResponseEntity<?> getProductByColorSizePriceCategory(@Param("valueSize") String valueSize,
//                                                                @Param("valueColor") String valueColor,
//                                                                @Param("minPrice") int minPrice,
//                                                                @Param("maxPrice") int maxPrice,
//                                                                @Param("categoryId") long categoryId,
//                                                                @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
//                                                                @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
//                                                                @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {
//
//        try {
//            List<ProductResponse> productResponses = productService.getProductByColorSizePriceCategory(valueSize, valueColor, minPrice, maxPrice, categoryId, pageNo, pageSize, sortBy);
//            return ResponseEntity.ok(Objects.requireNonNullElse(productResponses, "Không có sản phẩm nào!"));
//        } catch (Exception e) {
//            return new ResponseEntity<>("Lỗi! :" + e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long productId) {
        try {
            ProductResponse productResponse = productService.getProduct(productId);
            if (productResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getProductByName(@RequestParam(value = "name") String name) {
        try {
            ProductResponse productResponse = productService.getProductByName(name);
            if (productResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", productResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/size/{id}")
    private ResponseEntity<?> getProductBySize(@PathVariable("id") long sizeId) {
        try {
            ProductResponse productResponse = productService.getProductBySize(sizeId);
            if (productResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy dữ liệu thành công", productResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/size")
    public ResponseEntity<?> getProductByValueSize(@RequestParam String valueSize,
                                                   @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                   @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            Pair<List<ProductResponse>, Integer> result = productService.getProductsByValueSize(valueSize, pageNo, pageSize, sortBy);
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

    @GetMapping("/search/color")
    public ResponseEntity<?> getProductByValueColor(@RequestParam String valueColor,
                                                    @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                    @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            Pair<List<ProductResponse>, Integer> result = productService.getProductsByValueColor(valueColor, pageNo, pageSize, sortBy);
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

    @GetMapping("/search/price")
    public ResponseEntity<?> getProductByPrice(@RequestParam int minPrice,
                                               @RequestParam int maxPrice,
                                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                               @RequestParam(value = "sortBy", defaultValue = "name") String sortBy) {

        try {
            Pair<List<ProductResponse>, Integer> result = productService.getProductsByPrice(minPrice, maxPrice, pageNo, pageSize, sortBy);
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

    @GetMapping("/searchPro")
    public ResponseEntity<?> searchProduct(@Param("valueSize") String valueSize,
                                           @Param("valueColor") String valueColor,
                                           @Param("minPrice") Integer minPrice,
                                           @Param("maxPrice") Integer maxPrice,
                                           @Param("categoryId") long categoryId,
                                           @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                           @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            Pair<List<ProductResponse>, Integer> result = productService.searchProduct(valueSize, valueColor, minPrice, maxPrice, categoryId, pageNo, pageSize, sortBy);
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

    @GetMapping("/sale/{saleId}")
    private ResponseEntity<?> getProductBySale(@PathVariable("saleId") Long saleId,
                                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                               @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        try {
            Pair<List<ProductResponse>, Integer> result = productService.getProductBySaleId(saleId, pageNo, pageSize, sortBy);
            List<ProductResponse> productResponses = result.getFirst();
            int total = result.getSecond();
            if (!productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/relatedProducts/{id}")
    public ResponseEntity<?> getRelatedProducts(@PathVariable("id") Long categoryId) {

        try {
            List<ProductResponse> productResponses = productService.getRelatedProducts(categoryId, 10);
            if (productResponses != null && !productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy dữ liệu thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, false, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/randomProducts")
    public ResponseEntity<?> getRandomProducts() {
        try {
            List<ProductResponse> productResponses = productService.getRandomProducts();
            if (productResponses != null && !productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy dữ liệu thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, false, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bestSeller")
    public ResponseEntity<?> getOrder(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {

        try {
            Pair<List<ProductResponse>, Integer> result = productService.getBestSellerProducts(pageNo, pageSize, sortBy);
            List<ProductResponse> productResponses = result.getFirst();
            int total = result.getSecond();
            if (!productResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(productResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/all/category/{id}")
    public ResponseEntity<?> getProducts(@PathVariable("id") Long categoryId) {
        try {
            List<ProductResponse> products = productService.getProductsByCategoryId(categoryId);
            if (!products.isEmpty()) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy danh sách thành công", products), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/sale")
    public ResponseEntity<?> getProductBySale() {
        try {
            List<ProductResponse> products = productService.getProductBySale();
            if (!products.isEmpty()) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy danh sách thành công", products), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
