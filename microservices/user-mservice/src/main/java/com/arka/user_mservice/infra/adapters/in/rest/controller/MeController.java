package com.arka.user_mservice.infra.adapters.in.rest.controller;

import com.arka.user_mservice.application.ports.in.IAuthenticatedUserUseCase;
import com.arka.user_mservice.domain.models.UserProfileModel;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.UpdateUserProfileRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.UpdateUserRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.UserProfileResponse;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.UserResponse;
import com.arka.user_mservice.infra.adapters.in.rest.mapper.UserDtoMapper;
import com.arka.user_mservice.infra.adapters.in.rest.mapper.UserProfileDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {

    private final IAuthenticatedUserUseCase iAuthenticatedUserUseCase;
    private final UserDtoMapper userDtoMapper;
    private final UserProfileDtoMapper userProfileDtoMapper;

    private UUID extractUserId(Authentication authentication) {
        return UUID.fromString(authentication.getName());
    }

    @GetMapping
    public Mono<ResponseEntity<UserResponse>> getMe(Mono<Authentication> auth) {
        return auth
                .map(this::extractUserId)
                .flatMap(iAuthenticatedUserUseCase::getMyUser)
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping
    public Mono<ResponseEntity<UserResponse>> updateMe(
            Mono<Authentication> auth,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        return auth
                .map(this::extractUserId)
                .flatMap(userId -> iAuthenticatedUserUseCase.updateMyUser(userId, userDtoMapper.toModel(request)))
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/profile")
    public Mono<ResponseEntity<UserProfileResponse>> updateProfile(
            Mono<Authentication> auth,
            @RequestBody @Valid UpdateUserProfileRequest request
    ) {
        return auth
                .map(this::extractUserId)
                .flatMap(userId -> iAuthenticatedUserUseCase.getMyUser(userId)
                        .flatMap(user -> {
                            UserProfileModel profileModel = user.getProfile();
                            userProfileDtoMapper.updateModelFromRequest(request, profileModel);
                            return iAuthenticatedUserUseCase.updateMyProfile(userId, profileModel);
                        }))
                .map(userProfileDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }
}

