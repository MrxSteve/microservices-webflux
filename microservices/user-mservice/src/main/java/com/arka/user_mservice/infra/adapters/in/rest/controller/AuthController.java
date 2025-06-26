package com.arka.user_mservice.infra.adapters.in.rest.controller;

import com.arka.user_mservice.application.ports.in.IAuthUseCase;
import com.arka.user_mservice.application.ports.in.IRefreshTokenUseCase;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.LoginRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.LogoutRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.RefreshTokenRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.AuthResponse;
import com.arka.user_mservice.infra.adapters.in.rest.mapper.AuthDtoMapper;
import com.arka.user_mservice.infra.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthUseCase iAuthUseCase;
    private final AuthDtoMapper authDtoMapper;
    private final IRefreshTokenUseCase iRefreshTokenUseCase;
    private final JwtUtils jwtUtils;

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

    @DeleteMapping("/logout")
    public Mono<ResponseEntity<Object>> logout(@RequestBody @Valid LogoutRequest request) {
        return iAuthUseCase.logout(request.getRefreshToken(), request.getSessionId())
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(e -> {
                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.badRequest().build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                });
    }

    @DeleteMapping("/logout/all")
    public Mono<ResponseEntity<Object>> logoutAll(ServerHttpRequest request) {
        String token = jwtUtils.extractTokenFromHeader(request.getHeaders());

        return iAuthUseCase.logoutAll(token)
                .thenReturn(ResponseEntity.noContent().build())
                .onErrorResume(e -> {
                    if (e instanceof IllegalArgumentException) {
                        return Mono.just(ResponseEntity.badRequest().build());
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                });
    }
}