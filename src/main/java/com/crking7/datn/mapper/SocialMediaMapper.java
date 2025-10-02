package com.crking7.datn.mapper;

import com.crking7.datn.models.SocialMedia;
import com.crking7.datn.models.dtos.SocialMediaDto;
import org.mapstruct.Mapper;

@Mapper
public interface SocialMediaMapper {
    SocialMediaDto mapToDto(SocialMedia socialMedia);
    SocialMedia mapToModel(SocialMediaDto socialMediaDto);

}
