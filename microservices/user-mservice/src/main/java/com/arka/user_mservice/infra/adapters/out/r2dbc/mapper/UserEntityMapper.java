package com.arka.user_mservice.infra.adapters.out.r2dbc.mapper;

import com.arka.user_mservice.domain.models.UserModel;
import com.arka.user_mservice.infra.adapters.out.r2dbc.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {
    UserModel toModel(UserEntity entity);
    UserEntity toEntity(UserModel model);
}
