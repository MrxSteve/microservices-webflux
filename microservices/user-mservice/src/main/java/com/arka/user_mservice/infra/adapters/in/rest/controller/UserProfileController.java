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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "User Profiles", description = "CRUD operations for user profiles")
@RestController
@RequestMapping("/api/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {

    private final IUserProfileUseCases iUserProfileUseCases;
    private final UserProfileDtoMapper userProfileDtoMapper;

    @Operation(summary = "Create a profile for a user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/{userId}")
    public Mono<ResponseEntity<UserProfileResponse>> create(
            @Parameter(description = "User ID", required = true) @PathVariable UUID userId,
            @Valid @RequestBody UserProfileRequest request) {
        UserProfileModel model = userProfileDtoMapper.toModel(request);
        model.setUserId(userId);
        return iUserProfileUseCases.create(model)
                .map(userProfileDtoMapper::toResponse)
                .map(res -> ResponseEntity.created(URI.create("/api/user-profiles/" + res.getId())).body(res));
    }

    @Operation(summary = "Update an existing profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @PatchMapping("/{profileId}")
    public Mono<ResponseEntity<UserProfileResponse>> update(
            @Parameter(description = "Profile ID", required = true) @PathVariable UUID profileId,
            @Valid @RequestBody UpdateUserProfileRequest request) {
        return iUserProfileUseCases.findById(profileId)
                .flatMap(existing -> {
                    userProfileDtoMapper.updateModelFromRequest(request, existing);
                    return iUserProfileUseCases.update(profileId, existing);
                })
                .map(userProfileDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Get all user profiles")
    @ApiResponse(responseCode = "200", description = "List of profiles")
    @GetMapping
    public Flux<UserProfileResponse> findAll() {
        return iUserProfileUseCases.findAll()
                .map(userProfileDtoMapper::toResponse);
    }

    @Operation(summary = "Find a profile by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping("/{profileId}")
    public Mono<ResponseEntity<UserProfileResponse>> findById(
            @Parameter(description = "Profile ID", required = true) @PathVariable UUID profileId) {
        return iUserProfileUseCases.findById(profileId)
                .map(userProfileDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Find a profile by user ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping("/by-user/{userId}")
    public Mono<ResponseEntity<UserProfileResponse>> findByUserId(
            @Parameter(description = "User ID", required = true) @PathVariable UUID userId) {
        return iUserProfileUseCases.findByUserId(userId)
                .map(userProfileDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Delete a profile by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Profile deleted"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @DeleteMapping("/{profileId}")
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "Profile ID", required = true) @PathVariable UUID profileId) {
        return iUserProfileUseCases.deleteById(profileId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}

