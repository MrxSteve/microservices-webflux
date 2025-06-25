package com.arka.user_mservice.infra.adapters.in.rest.mapper;

import com.arka.user_mservice.domain.models.AuthTokens;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.AuthResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthDtoMapper {
    AuthResponse toResponse(AuthTokens authTokens);
}
