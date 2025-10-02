package com.crking7.datn.web;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.services.CategoryService;
import com.crking7.datn.web.dto.response.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryRest {
    private final CategoryService categoryService;

    @Autowired
    public CategoryRest(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<?> getCategories(@RequestParam(value = "pageNo", defaultValue = "1")int pageNo,
                                                         @RequestParam(value = "pageSize", defaultValue = "20")int pageSize,
                                                         @RequestParam(value = "sortBy",defaultValue = "id")String sortBy){
        try {
            Pair<List<CategoryResponse>, Integer> result  = categoryService.getCategories(pageNo, pageSize, sortBy);
            List<CategoryResponse> categoryResponses = result.getFirst();
            int total = result.getSecond();
            if (!categoryResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(categoryResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "thất bại", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi " + e.getMessage());
        }
    }
    @GetMapping("/type/{type}")
    public ResponseEntity<?> getCategoriesByType(@PathVariable("type") int type){
        try {
            List<CategoryResponse> categoryResponses = categoryService.getCategoriesByType(type);
            if (categoryResponses != null && !categoryResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(categoryResponses);
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy dữ liệu thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            }
        }catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/parent")
    public ResponseEntity<?> getCategoriesByParentCategory(@RequestParam(name = "parentId") Long parentId){
        try {
            List<CategoryResponse> categoryResponses = categoryService.getCategoriesByParentCategory(parentId);
            if (categoryResponses != null && !categoryResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(categoryResponses);
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy dữ liệu thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            }
        }catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/parentCategory")
    public ResponseEntity<?> getParentCategory() {
        try {
            List<CategoryResponse> categoryResponse = categoryService.getParentCategory();
            if (categoryResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Lấy dữ liệu thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy dữ liệu thành công", categoryResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable(name = "id") long id){
        try {
            CategoryResponse categoryResponse = categoryService.getCategoryById(id);
            if(categoryResponse == null){
                return new ResponseEntity<>(ApiResponse.build(201, false, "thất bại", null), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(ApiResponse.build(200, true, "Lấy dữ liệu thành công", categoryResponse), HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
