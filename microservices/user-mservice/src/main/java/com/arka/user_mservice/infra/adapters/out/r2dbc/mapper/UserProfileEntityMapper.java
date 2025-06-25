package com.arka.user_mservice.infra.adapters.out.r2dbc.mapper;

import com.arka.user_mservice.domain.models.UserProfileModel;
import com.arka.user_mservice.infra.adapters.out.r2dbc.entities.UserProfileEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileEntityMapper {
    UserProfileModel toModel(UserProfileEntity entity);
    UserProfileEntity toEntity(UserProfileModel model);
}
