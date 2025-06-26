package com.arka.user_mservice.application.ports.in;

import com.arka.user_mservice.domain.models.AuthTokens;
import reactor.core.publisher.Mono;

public interface IAuthUseCase {
    Mono<AuthTokens> login(String email, String password);
    Mono<Void> logout(String refreshToken, String sessionId);
    Mono<Void> logoutAll(String accessToken);
}
