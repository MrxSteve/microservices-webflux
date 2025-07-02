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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication", description = "Endpoints for login, logout, and token refresh")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthUseCase iAuthUseCase;
    private final AuthDtoMapper authDtoMapper;
    private final IRefreshTokenUseCase iRefreshTokenUseCase;
    private final JwtUtils jwtUtils;

    @Operation(summary = "User login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return iAuthUseCase.login(request.getEmail(), request.getPassword())
                .map(authDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Refresh JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token or session ID")
    })
    @PostMapping("/refresh")
    public Mono<ResponseEntity<AuthResponse>> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return iRefreshTokenUseCase.refreshToken(request.getRefreshToken(), request.getSessionId())
                .map(authDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @Operation(summary = "Logout from current session")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Logout successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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

    @Operation(summary = "Logout from all sessions")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Logout from all sessions successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
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
