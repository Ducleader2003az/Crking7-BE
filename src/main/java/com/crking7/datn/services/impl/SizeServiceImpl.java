package com.crking7.datn.services.impl;

import com.crking7.datn.config.Constants;
import com.crking7.datn.mapper.ArticleMapper;
import com.crking7.datn.mapper.SizeMapper;
import com.crking7.datn.models.Article;
import com.crking7.datn.models.ArticleImage;
import com.crking7.datn.models.Category;
import com.crking7.datn.models.Size;
import com.crking7.datn.models.dtos.SizeDto;
import com.crking7.datn.repositories.ArticleImageRepository;
import com.crking7.datn.repositories.ArticleRepository;
import com.crking7.datn.repositories.CategoryRepository;
import com.crking7.datn.repositories.SizeRepository;
import com.crking7.datn.services.ArticleService;
import com.crking7.datn.services.SizeService;
import com.crking7.datn.web.dto.request.ArticleImageRequest;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.response.ArticleResponse;
import com.crking7.datn.web.dto.response.SizeResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;
    private final SizeMapper sizeMapper;

    public SizeServiceImpl(SizeRepository sizeRepository,
                           SizeMapper sizeMapper) {
        this.sizeRepository = sizeRepository;
        this.sizeMapper = sizeMapper;
    }

    @Override
    public List<SizeDto> getAllValueSize() {
        List<String> distinctSizeValues = sizeRepository.findDistinctSizeValues();
        return distinctSizeValues.stream()
                .map(SizeDto::new)
                .collect(Collectors.toList());
    }
}
