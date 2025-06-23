package com.arka.user_mservice.infra.adapters.in.rest.mapper;

import com.arka.user_mservice.domain.models.UserModel;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.UpdateUserRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.UserRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserModel toModel(UpdateUserRequest request);
    UserModel toModel(UserRequest request);
    UserResponse toResponse(UserModel model);
}
