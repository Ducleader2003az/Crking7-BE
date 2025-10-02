package com.crking7.datn.mapper;

import com.crking7.datn.models.Role;
import com.crking7.datn.web.dto.response.RoleResponse;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    RoleResponse mapModelToResponse(Role role);
}
