package com.arka.user_mservice.infra.adapters.in.rest.controller;

import com.arka.user_mservice.application.ports.in.IUserProfileUseCases;
import com.arka.user_mservice.domain.models.UserProfileModel;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.UpdateUserProfileRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.UserProfileRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.UserProfileResponse;
import com.arka.user_mservice.infra.adapters.in.rest.mapper.UserProfileDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final IUserProfileUseCases iUserProfileUseCases;
    private final UserProfileDtoMapper userProfileDtoMapper;

    @PostMapping("/{userId}")
    public Mono<ResponseEntity<UserProfileResponse>> create(
            @PathVariable UUID userId,
            @Valid @RequestBody UserProfileRequest request
    ) {
        UserProfileModel model = userProfileDtoMapper.toModel(request);
        model.setUserId(userId);
        return iUserProfileUseCases.create(model)
                .map(userProfileDtoMapper::toResponse)
                .map(res -> ResponseEntity.created(URI.create("/api/user-profiles/" + res.getId())).body(res));
    }

    @PatchMapping("/{profileId}")
    public Mono<ResponseEntity<UserProfileResponse>> update(
            @PathVariable UUID profileId,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        return iUserProfileUseCases.findById(profileId)
                .flatMap(existing -> {
                    userProfileDtoMapper.updateModelFromRequest(request, existing);
                    return iUserProfileUseCases.update(profileId, existing);
                })
                .map(userProfileDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Flux<UserProfileResponse> findAll() {
        return iUserProfileUseCases.findAll()
                .map(userProfileDtoMapper::toResponse);
    }

    @GetMapping("/{profileId}")
    public Mono<ResponseEntity<UserProfileResponse>> findById(@PathVariable UUID profileId) {
        return iUserProfileUseCases.findById(profileId)
                .map(userProfileDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/by-user/{userId}")
    public Mono<ResponseEntity<UserProfileResponse>> findByUserId(@PathVariable UUID userId) {
        return iUserProfileUseCases.findByUserId(userId)
                .map(userProfileDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{profileId}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable UUID profileId) {
        return iUserProfileUseCases.deleteById(profileId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
