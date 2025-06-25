package com.arka.user_mservice.infra.adapters.in.rest.controller;

import com.arka.user_mservice.application.ports.in.IAuthUseCase;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.LoginRequest;
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

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return iAuthUseCase.login(request.getEmail(), request.getPassword())
                .map(authDtoMapper::toResponse)
                .map(ResponseEntity::ok);
    }
}