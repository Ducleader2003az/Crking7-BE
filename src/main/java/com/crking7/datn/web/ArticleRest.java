package com.crking7.datn.web;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.web.dto.response.ArticleResponse;
import com.crking7.datn.services.ArticleService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/article")
public class ArticleRest {
    private final ArticleService articleService;

    public ArticleRest(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public ResponseEntity<?> getArticles(@RequestParam(value = "pageNo", defaultValue = "1")int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20")int pageSize,
                                         @RequestParam(value = "sortBy",defaultValue = "id")String sortBy){
        try {
            Pair<List<ArticleResponse>, Integer> result  = articleService.getArticles(pageNo, pageSize, sortBy);
            List<ArticleResponse> articleResponses = result.getFirst();
            int total = result.getSecond();
            if (!articleResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(articleResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "Lấy danh sách thành công", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/home")
    public ResponseEntity<?> getArticleAdmin() {
        try {
            List<ArticleResponse> articleResponses = articleService.getArticlesHome();
            if (articleResponses == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", articleResponses), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/relatedArticle")
    public ResponseEntity<?> getRelatedArticles(@RequestParam(value = "categoryId") Long categoryId) {
        try {
            List<ArticleResponse> articleResponses = articleService.getRelatedArticles(categoryId);
            if (articleResponses == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", articleResponses), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<?> getArticlesByCategory(@PathVariable("id")long categoryId,
                                                   @RequestParam(value = "pageNo", defaultValue = "1")int pageNo,
                                                   @RequestParam(value = "pageSize", defaultValue = "20")int pageSize,
                                                   @RequestParam(value = "sortBy",defaultValue = "id")String sortBy){
        try {
            Pair<List<ArticleResponse>, Integer> result  = articleService.getArticleByCategory(pageNo, pageSize, sortBy, categoryId);
            List<ArticleResponse> articleResponses = result.getFirst();
            int total = result.getSecond();
            if (!articleResponses.isEmpty()) {
                List<Object> data = new ArrayList<>(articleResponses);
                return new ResponseEntity<>(ApiResponsePage.build(200, true, pageNo, pageSize, total, "Lấy danh sách thành công", data), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponsePage.build(201, false, pageNo, pageSize, total, "Lấy danh sách thành công", null), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticle(@PathVariable("id") long articleId){
        try{
            ArticleResponse articleResponse = articleService.getArticle(articleId);
            if (articleResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", articleResponse), HttpStatus.OK);
            }
        }catch (Exception e) {
            return new ResponseEntity<>("Lỗi!", HttpStatus.BAD_REQUEST);
        }
    }
}
