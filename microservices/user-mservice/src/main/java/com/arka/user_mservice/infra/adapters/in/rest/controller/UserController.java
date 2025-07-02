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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserUseCases iUserUseCases;
    private final UserDtoMapper userDtoMapper;

    @PostMapping("/register")
    public Mono<ResponseEntity<UserResponse>> register(@Valid @RequestBody UserRequest request) {
        UserModel user = userDtoMapper.toModel(request);
        return iUserUseCases.register(user)
                .map(userDtoMapper::toResponse)
                .map(res -> ResponseEntity.status(HttpStatus.CREATED).body(res));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findById(@PathVariable UUID id) {
        return iUserUseCases.findById(id)
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Flux<UserResponse> findAll() {
        return iUserUseCases.findAll()
                .map(userDtoMapper::toResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public Mono<ResponseEntity<UserResponse>> findByUsernameOrEmail(@RequestParam String value) {
        return iUserUseCases.findByUsernameOrEmail(value)
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        UserModel userToUpdate = userDtoMapper.toModel(request);
        return iUserUseCases.update(id, userToUpdate)
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
        return iUserUseCases.deleteById(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/enable")
    public Mono<ResponseEntity<UserResponse>> enable(@PathVariable UUID id) {
        return iUserUseCases.enable(id)
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/disable")
    public Mono<ResponseEntity<UserResponse>> disable(@PathVariable UUID id) {
        return iUserUseCases.disable(id)
                .map(userDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }
}

