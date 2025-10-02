package com.crking7.datn.services.impl;

import com.crking7.datn.mapper.ColorMapper;
import com.crking7.datn.models.Color;
import com.crking7.datn.models.Product;
import com.crking7.datn.models.dtos.ColorDto;
import com.crking7.datn.repositories.ColorRepository;
import com.crking7.datn.repositories.ProductRepository;
import com.crking7.datn.services.ColorService;
import com.crking7.datn.web.dto.response.ColorResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;

    private final ProductRepository productRepository;
    private final ColorMapper colorMapper;

    public ColorServiceImpl(ColorRepository colorRepository,
                            ColorMapper colorMapper,
                            ProductRepository productRepository) {
        this.colorRepository = colorRepository;
        this.colorMapper = colorMapper;
        this.productRepository = productRepository;
    }

    @Override
    public List<ColorDto> getAllValueColor() {
        List<String> distinctColorValues = colorRepository.findDistinctColorValues();
        return distinctColorValues.stream()
                .map(ColorDto::new)
                .collect(Collectors.toList());
    }
    @Override
    public List<ColorResponse> getColorByProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow();
        List<Color> colors = colorRepository.findByProduct(product);
        return colors.stream()
                .map(colorMapper::mapModelToResponse)
                .toList();
    }
}
