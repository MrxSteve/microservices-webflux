package com.arka.user_mservice.infra.adapters.out.r2dbc.mapper;

import com.arka.user_mservice.domain.models.UserRoleModel;
import com.arka.user_mservice.infra.adapters.out.r2dbc.entities.UserRoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRoleEntityMapper {
    UserRoleEntity toEntity(UserRoleModel model);
    UserRoleModel toModel(UserRoleEntity entity);
}
