package com.arka.user_mservice.infra.adapters.in.rest.mapper;

import com.arka.user_mservice.domain.models.RoleModel;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.RoleRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.RoleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleDtoMapper {
    RoleModel toModel(RoleRequest request);
    RoleResponse toResponse(RoleModel model);
}
