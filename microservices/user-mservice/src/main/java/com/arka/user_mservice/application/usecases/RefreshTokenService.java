package com.arka.user_mservice.application.usecases;

import com.arka.user_mservice.application.ports.in.IRefreshTokenUseCase;
import com.arka.user_mservice.application.ports.out.AuthPersistencePort;
import com.arka.user_mservice.application.ports.out.RefreshTokenStoragePort;
import com.arka.user_mservice.application.ports.out.TokenProviderPort;
import com.arka.user_mservice.domain.models.AuthTokens;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenUseCase {

    private final RefreshTokenStoragePort refreshTokenStoragePort;
    private final TokenProviderPort tokenProviderPort;
    private final AuthPersistencePort authPersistencePort;

    @Override
    public Mono<AuthTokens> refreshToken(String refreshToken, String sessionId) {
        if (!tokenProviderPort.validateToken(refreshToken)) {
            return Mono.error(new RuntimeException("Invalid refresh token"));
        }

        UUID userId = tokenProviderPort.getUserIdFromToken(refreshToken);
        String sessionIdFromToken = tokenProviderPort.getSessionIdFromToken(refreshToken);

        if (!sessionId.equals(sessionIdFromToken)) {
            return Mono.error(new RuntimeException("Session ID mismatch"));
        }

        return refreshTokenStoragePort.get(userId, sessionId)
                .flatMap(storedToken -> {
                    if (!storedToken.equals(refreshToken)) {
                        return Mono.error(new RuntimeException("Refresh token mismatch"));
                    }
                    return authPersistencePort.findByUserId(userId);
                })
                .flatMap(user -> {
                    String newAccessToken = tokenProviderPort.generateAccessToken(user.getId(), user.getUsername());
                    String newRefreshToken = tokenProviderPort.generateRefreshToken(user.getId(), sessionId);

                    Duration expiration = Duration.between(
                            Instant.now(),
                            tokenProviderPort.getExpiration(newRefreshToken).toInstant()
                    );

                    return refreshTokenStoragePort.save(user.getId(), sessionId, newRefreshToken, expiration)
                            .thenReturn(new AuthTokens(newAccessToken, newRefreshToken, sessionId));
                });
    }
}

