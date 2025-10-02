package com.crking7.datn.services;

import com.crking7.datn.models.Size;
import com.crking7.datn.models.dtos.SizeDto;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.response.ArticleResponse;
import com.crking7.datn.web.dto.response.SizeResponse;

import java.util.List;

public interface SizeService {
    List<SizeDto> getAllValueSize();
}
