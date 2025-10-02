package com.crking7.datn.web.admin;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.models.Banner;
import com.crking7.datn.web.dto.request.BannerRequest;
import com.crking7.datn.web.dto.response.BannerResponse;
import com.crking7.datn.services.BannerService;
import com.crking7.datn.web.dto.response.CategoryResponse;
import com.crking7.datn.web.dto.response.OrdersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/admin/banner")
public class ABannerRest {
    private final BannerService bannerService;

    @Autowired
    public ABannerRest(BannerService bannerService) {
        this.bannerService = bannerService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> createBanner(@RequestBody BannerRequest bannerRequest) {
        try {
            BannerResponse bannerResponse = bannerService.createBanner(bannerRequest);
            if (bannerResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", "Tên banner đã tồn tại"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", bannerResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBannerById(@PathVariable(name = "id") long id) {
        try {
            BannerResponse bannerResponse = bannerService.getBannerById(id);
            if (bannerResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", "Banner không tồn tại"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", bannerResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getBanners(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                        @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        try {
            Pair<List<BannerResponse>, Integer> result = bannerService.getBanners(pageNo, pageSize, sortBy);
            List<BannerResponse> bannerResponses = result.getFirst();
            int total = result.getSecond();
            if (!bannerResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(bannerResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/allBanner")
    public ResponseEntity<?> getAllBanners(@RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) Integer status,
                                           @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                           @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                           @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {
        try {
            Pair<List<BannerResponse>, Integer> result = bannerService.getAllBanners(keyword,status, pageNo, pageSize, sortBy,sortDirection.equals("desc"));
            List<BannerResponse> bannerResponses = result.getFirst();
            int total = result.getSecond();
            if (!bannerResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(bannerResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBanner(@PathVariable("id") long id,
                                          @RequestBody BannerRequest bannerRequest) {
        try {
            BannerResponse banner = bannerService.getBannerByName(bannerRequest.getName());
            BannerResponse banner2 = bannerService.getBannerById(id);
            if (banner != null) {
                if (banner2 != null && banner2.getId() == banner.getId()) {
                    BannerResponse bannerResponse = bannerService.updateBanner(id, bannerRequest);
                    if (bannerResponse != null) {
                        return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", bannerResponse), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Cập nhật không thành công"), HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Tên danh mục đã tồn tại"), HttpStatus.OK);
                }
            } else {
                BannerResponse bannerResponse = bannerService.updateBanner(id, bannerRequest);
                if (bannerResponse != null) {
                    return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", bannerResponse), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Cập nhật không thành công"), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/hideBanner/{id}")
    public ResponseEntity<?> hideCategory(@PathVariable("id") long id) {
        try {
            String s = bannerService.hideBanner(id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/showBanner/{id}")
    public ResponseEntity<?> showBanner(@PathVariable("id") long id) {
        try {
            String s = bannerService.showBanner(id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBanner(@PathVariable(name = "id") long id) {
        try {
            bannerService.deleteBanner(id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", "Xóa banne thành công"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
