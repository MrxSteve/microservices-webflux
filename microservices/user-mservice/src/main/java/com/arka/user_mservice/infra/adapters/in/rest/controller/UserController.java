package com.arka.user_mservice.infra.adapters.in.rest.controller;

import com.arka.user_mservice.application.ports.in.IUserUseCases;
import com.arka.user_mservice.domain.models.UserModel;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.UpdateUserRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.UserRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.UserResponse;
import com.arka.user_mservice.infra.adapters.in.rest.mapper.UserDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Users", description = "CRUD operations for users")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserUseCases iUserUseCases;
    private final UserDtoMapper userDtoMapper;

    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    @PostMapping("/register")
    public Mono<ResponseEntity<UserResponse>> register(@Valid @RequestBody UserRequest request) {
        UserModel user = userDtoMapper.toModel(request);
        return iUserUseCases.register(user)
                .map(userDtoMapper::toResponse)
                .map(res -> ResponseEntity.status(HttpStatus.CREATED).body(res));
    }

    @Operation(summary = "Find a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable UUID id) {
        return iUserUseCases.findById(id)
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List all users")
    @ApiResponse(responseCode = "200", description = "List of users")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Flux<UserResponse> findAll() {
        return iUserUseCases.findAll()
                .map(userDtoMapper::toResponse);
    }

    @Operation(summary = "Find user by username or email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public Mono<ResponseEntity<UserResponse>> findByUsernameOrEmail(@RequestParam String value) {
        return iUserUseCases.findByUsernameOrEmail(value)
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        UserModel userToUpdate = userDtoMapper.toModel(request);
        return iUserUseCases.update(id, userToUpdate)
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Delete a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
        return iUserUseCases.deleteById(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Enable a user by ID")
    @ApiResponse(responseCode = "200", description = "User enabled")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/enable")
    public Mono<ResponseEntity<UserResponse>> enable(@PathVariable UUID id) {
        return iUserUseCases.enable(id)
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Disable a user by ID")
    @ApiResponse(responseCode = "200", description = "User disabled")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/disable")
    public Mono<ResponseEntity<UserResponse>> disable(@PathVariable UUID id) {
        return iUserUseCases.disable(id)
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }
}


