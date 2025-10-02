package com.crking7.datn.web.admin;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.helper.ApiResponsePage;
import com.crking7.datn.services.ArticleService;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.request.ArticleUDRequest;
import com.crking7.datn.web.dto.request.CategoryRequest;
import com.crking7.datn.web.dto.response.ArticleResponse;
import com.crking7.datn.web.dto.response.CategoryResponse;
import com.crking7.datn.web.dto.response.OrdersResponse;
import com.crking7.datn.web.dto.response.ProductResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/article")
public class AArticleRest {
    private final ArticleService articleService;

    public AArticleRest(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public ResponseEntity<?> getArticles(@RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) Integer status,
                                         @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
                                         @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection
        ) {
        try {
            Pair<List<ArticleResponse>, Integer> result  = articleService.getAllArticles(keyword,status, pageNo, pageSize, sortBy, sortDirection.equals("desc"));
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
    @PostMapping("/create")
    public ResponseEntity<?> createArticle(@RequestBody ArticleRequest articleRequest) {
        try {
            ArticleResponse articleResponse = articleService.createArticle(articleRequest);
            if (articleResponse != null) {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", articleResponse), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", "Tên bài viết đã tồn tại"), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getArticleAdmin(@PathVariable("id") Long articleId) {
        try {
            ArticleResponse articleResponse = articleService.getArticleAdmin(articleId);
            if (articleResponse == null) {
                return new ResponseEntity<>(ApiResponse.build(201, false, "thành công", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", articleResponse), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ApiResponse.build(404, true, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable("id") long id,
                                           @RequestBody ArticleRequest articleRequest) {
        try {
            ArticleResponse article = articleService.getArticleByName(articleRequest.getTitle());
            ArticleResponse article2 = articleService.getArticleAdmin(id);
            if (article != null) {
                if (article2 != null && article2.getId() == article.getId()) {
                    ArticleResponse articleResponse = articleService.updateArticle(id, articleRequest);
                    if (articleResponse != null) {
                        return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", articleResponse), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Cập nhật không thành công"), HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Tên danh mục đã tồn tại"), HttpStatus.OK);
                }
            } else {
                ArticleResponse articleResponse = articleService.updateArticle(id, articleRequest);

                if (articleResponse != null) {
                    return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", articleResponse), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(ApiResponse.build(201, false, "Thất bại", "Cập nhật không thành công"), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hideArticle(@PathVariable("id") long id) {
        try {
            String s = articleService.hideArticle(id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/show/{id}")
    public ResponseEntity<?> showArticle(@PathVariable("id") long id) {
        try {
            String s = articleService.showArticle(id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", s), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable(name = "id") long id) {
        try {
            articleService.delete(id);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", "Xóa bài viết thành công!"), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa bài viết không thành công! Lỗi " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteImage/{id}")
    public ResponseEntity<?> deleteArticleImage(@PathVariable(name = "id") long articleImageId) {
        try {
            articleService.deleteImage(articleImageId);
            return new ResponseEntity<>(ApiResponse.build(200, true, "Thành công", "Xóa ảnh thành công!"), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa ảnh không thành công! Lỗi " + e.getMessage());
        }
    }
}
