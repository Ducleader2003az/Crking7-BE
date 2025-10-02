package com.crking7.datn.services;

import com.crking7.datn.web.dto.request.CompanyRequest;
import com.crking7.datn.web.dto.response.CompanyResponse;

public interface CompanyService {
    CompanyResponse getCompany(long companyId);
    CompanyResponse createCompanyInfo(CompanyRequest companyRequest);
    CompanyResponse updateCompanyInfo(long companyId, CompanyRequest companyRequest);
}
