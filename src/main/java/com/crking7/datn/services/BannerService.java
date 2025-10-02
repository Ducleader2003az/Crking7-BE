package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.BannerRequest;
import com.crking7.datn.web.dto.response.BannerResponse;
import com.crking7.datn.web.dto.response.OrdersResponse;
import org.springframework.data.util.Pair;

import java.util.List;

public interface BannerService {
    /**
     * get number of banners
     */
    List<BannerResponse> getNumberOfBanners(int number);

    List<BannerResponse> getNumberOfBannersByCategory(long categoryId, int number);

    BannerResponse createBanner(BannerRequest bannerRequest);

    BannerResponse getBannerById(long id);

    BannerResponse getBannerByName(String name);

    Pair<List<BannerResponse>, Integer> getBanners(int pageNo, int pageSize, String sortBy);

    Pair<List<BannerResponse>, Integer> getAllBanners(String keyword,Integer status, int pageNo, int pageSize, String sortBy, boolean desc);

    void deleteBanner(long id);

    BannerResponse updateBanner(long id, BannerRequest bannerRequest);

    String hideBanner(long id);

    String showBanner(long id);
}
