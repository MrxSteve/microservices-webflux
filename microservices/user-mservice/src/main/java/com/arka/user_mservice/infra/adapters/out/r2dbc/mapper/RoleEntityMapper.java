package com.arka.user_mservice.infra.adapters.out.r2dbc.mapper;

import com.arka.user_mservice.domain.models.RoleModel;
import com.arka.user_mservice.infra.adapters.out.r2dbc.entities.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleEntityMapper {
    RoleEntity toEntity(RoleModel model);
    RoleModel toModel(RoleEntity entity);
}
