package com.arka.user_mservice.infra.adapters.in.rest.controller;

import com.arka.user_mservice.application.ports.in.IAuthUseCase;
import com.arka.user_mservice.application.ports.in.IRefreshTokenUseCase;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.LoginRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.RefreshTokenRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.AuthResponse;
import com.arka.user_mservice.infra.adapters.in.rest.mapper.AuthDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthUseCase iAuthUseCase;
    private final AuthDtoMapper authDtoMapper;
    private final IRefreshTokenUseCase iRefreshTokenUseCase;

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return iAuthUseCase.login(request.getEmail(), request.getPassword())
                .map(authDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return iRefreshTokenUseCase.refreshToken(request.getRefreshToken(), request.getSessionId())
                .map(authDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

}