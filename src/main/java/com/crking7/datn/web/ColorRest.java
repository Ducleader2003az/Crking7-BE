package com.crking7.datn.web;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.models.dtos.ColorDto;
import com.crking7.datn.models.dtos.SizeDto;
import com.crking7.datn.services.ColorService;
import com.crking7.datn.services.SizeService;
import com.crking7.datn.web.dto.response.ColorResponse;
import com.crking7.datn.web.dto.response.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/color")
public class ColorRest {
    private final ColorService colorService;

    public ColorRest(ColorService colorService) {
        this.colorService = colorService;
    }
    @GetMapping("")
    public ResponseEntity<?> getAllValueColor() {
        try {
            List<ColorDto> colors = colorService.getAllValueColor();
            if (colors != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", colors), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getColorByProduct(@PathVariable(name = "id") Long productId) {
        try {
            List<ColorResponse> colorResponses = colorService.getColorByProduct(productId);
            if (!colorResponses.isEmpty()) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy danh sách thành công", colorResponses), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(200, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
