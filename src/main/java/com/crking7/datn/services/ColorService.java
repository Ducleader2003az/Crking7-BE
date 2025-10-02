package com.crking7.datn.services;

import com.crking7.datn.models.dtos.ColorDto;
import com.crking7.datn.web.dto.response.ColorResponse;

import java.util.List;

public interface ColorService {
    List<ColorDto> getAllValueColor();

    List<ColorResponse> getColorByProduct(Long productId);
}
