package com.arka.user_mservice.application.ports.in;

import com.arka.user_mservice.domain.models.AuthTokens;
import reactor.core.publisher.Mono;

public interface IRefreshTokenUseCase {
    Mono<AuthTokens> refreshToken(String refreshToken, String sessionId);
}
