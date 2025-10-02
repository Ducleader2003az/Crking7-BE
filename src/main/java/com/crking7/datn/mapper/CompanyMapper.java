package com.crking7.datn.mapper;

import com.crking7.datn.models.Company;
import com.crking7.datn.web.dto.request.CompanyRequest;
import com.crking7.datn.web.dto.response.CompanyResponse;
import org.mapstruct.*;

@Mapper(uses = SocialMediaMapper.class)
public interface CompanyMapper {

    @Mapping(target = "socials", source = "socialMedias")
    CompanyResponse mapToResponse(Company company);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "socialMedias", source = "socialMedias")
    Company mapToModel(CompanyRequest companyRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel(@MappingTarget Company company, CompanyRequest companyRequest);
}
