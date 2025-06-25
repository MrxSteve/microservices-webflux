package com.arka.user_mservice.infra.adapters.in.rest.mapper;

import com.arka.user_mservice.domain.models.UserProfileModel;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.UpdateUserProfileRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.UserProfileRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.UserProfileResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserProfileDtoMapper {
    UserProfileModel toModel(UserProfileRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModelFromRequest(UpdateUserProfileRequest request, @MappingTarget UserProfileModel model);

    UserProfileResponse toResponse(UserProfileModel model);
}
