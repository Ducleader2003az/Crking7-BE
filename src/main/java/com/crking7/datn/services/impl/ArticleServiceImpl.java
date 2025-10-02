package com.crking7.datn.services.impl;

import com.crking7.datn.config.Constants;
import com.crking7.datn.models.*;
import com.crking7.datn.repositories.ArticleImageRepository;
import com.crking7.datn.repositories.ArticleRepository;
import com.crking7.datn.repositories.CategoryRepository;
import com.crking7.datn.services.ArticleService;
import com.crking7.datn.web.dto.request.*;
import com.crking7.datn.web.dto.response.ArticleResponse;
import com.crking7.datn.mapper.ArticleMapper;
import com.crking7.datn.web.dto.response.OrdersResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              ArticleImageRepository articleImageRepository, CategoryRepository categoryRepository,
                              ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.articleImageRepository = articleImageRepository;
        this.categoryRepository = categoryRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public ArticleResponse getArticle(long articleId) {
        Article article = articleRepository.findByIdAndStatus(articleId, Constants.ACTIVE_STATUS);
        if (article != null) {
            return articleMapper.mapToResponse(article);
        }
        return null;
    }

    @Override
    public ArticleResponse getArticleAdmin(long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        return articleMapper.mapToResponse(article);
    }

    @Override
    public ArticleResponse getArticleByName(String title) {
        Article article = articleRepository.findByTitle(title);
        return articleMapper.mapToResponse(article);
    }

    @Override
    public Pair<List<ArticleResponse>, Integer> getArticles(int pageNo, int pageSize, String sortBy) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy).descending());
        Page<Article> articles = articleRepository.findAllByStatus(pageable, Constants.ACTIVE_STATUS);
        int total = (int) articles.getTotalElements();
        List<ArticleResponse> articleResponses = articles.getContent().stream()
                .map(articleMapper::mapToResponse)
                .toList();
        return Pair.of(articleResponses, total);
    }

    @Override
    public List<ArticleResponse> getRelatedArticles(long categoryId) {
        List<Article> articles = articleRepository.findRelatedArticle(categoryId, 5);
        return articles.stream()
                .map(articleMapper::mapToResponse)
                .toList();
    }

    @Override
    public Pair<List<ArticleResponse>, Integer> getAllArticles(String keyword,Integer status, int pageNo, int pageSize, String sortBy, boolean desc) {
        Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
        if (pageNo < 1) {
            pageNo = 1;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sortDirection, sortBy);
        Page<Article> articles = articleRepository.findAllByKeyword(keyword,status, pageable);
        int total = (int) articles.getTotalElements();
        List<ArticleResponse> articleResponses = articles.getContent().stream()
                .map(articleMapper::mapToResponse)
                .toList();
        return Pair.of(articleResponses, total);
    }

    @Override
    public List<ArticleResponse> getArticlesHome() {
        List<Article> articles = articleRepository.findByNewest();
        return articles.stream()
                .map(articleMapper::mapToResponse)
                .toList();
    }

    @Override
    public Pair<List<ArticleResponse>, Integer> getArticleByCategory(int pageNo, int pageSize, String sortBy, long categoryId) {
        Category category = categoryRepository.findByStatusAndIdAndType(Constants.ACTIVE_STATUS, categoryId, Constants.ARTICLE_TYPE);
        if (pageNo < 1) {
            pageNo = 1;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy).descending());
        Page<Article> articles = articleRepository.findAllByCategoryAndStatus(pageable, category, Constants.ACTIVE_STATUS);
        int total = (int) articles.getTotalElements();
        List<ArticleResponse> articleResponses = articles.getContent().stream()
                .map(articleMapper::mapToResponse)
                .toList();
        return Pair.of(articleResponses, total);
    }

    @Override
    @Transactional
    public ArticleResponse createArticle(ArticleRequest articleRequest) {
        Article article = articleRepository.findByTitle(articleRequest.getTitle());
        if (article == null) {
            Article articleNew = articleMapper.mapRequestToModel(articleRequest);
            Date date = new Date();
            articleNew.setCreatedDate(date);
            articleNew.setModifiedDate(date);
            articleNew.setStatus(1);
            Article articleResp = articleRepository.save(articleNew);

            return articleMapper.mapToResponse(articleResp);
        } else {
            return null;
        }
    }

    @Override
    public ArticleResponse updateArticle(long id, ArticleRequest articleRequest) {
        Article article = articleRepository.findById(id).orElseThrow();
        articleMapper.updateModel(article, articleRequest);
        Date currentDate = new Date();
        article.setModifiedDate(currentDate);
        articleRepository.save(article);

        return articleMapper.mapToResponse(article);
    }

    @Override
    public String hideArticle(long id) {
        Article article = articleRepository.findByIdAndStatus(id, Constants.ACTIVE_STATUS);
        if (article == null) {
            return null;
        }
        Date currentDate = new Date();
        article.setModifiedDate(currentDate);
        article.setStatus(0);
        articleRepository.save(article);
        return "Bài viết đã được ẩn";
    }

    @Override
    public String showArticle(long id) {
        Article article = articleRepository.findByIdAndStatus(id, Constants.INACTIVE_STATUS);
        if (article == null) {
            return null;
        }
        Date currentDate = new Date();
        article.setModifiedDate(currentDate);
        article.setStatus(1);
        articleRepository.save(article);
        return "Bài viết đã được hiện";
    }

    @Override
    public void delete(long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        if (article != null) {
            article.setCategory(null);
            articleRepository.save(article);
        }
        articleRepository.delete(article);
    }

    @Override
    public void deleteImage(long articleImageId) {
        ArticleImage articleImage = articleImageRepository.findById(articleImageId).orElseThrow();
        if (articleImage != null) {
            articleImage.setArticle(null);
            articleImageRepository.save(articleImage);
        }
        articleImageRepository.delete(articleImage);
    }
}
