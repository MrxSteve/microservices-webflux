package com.arka.user_mservice.application.usecases;

import com.arka.user_mservice.application.ports.in.IAuthUseCase;
import com.arka.user_mservice.application.ports.out.AuthPersistencePort;
import com.arka.user_mservice.application.ports.out.PasswordEncoderPort;
import com.arka.user_mservice.application.ports.out.RefreshTokenStoragePort;
import com.arka.user_mservice.application.ports.out.TokenProviderPort;
import com.arka.user_mservice.domain.exceptions.ResourceNotFoundException;
import com.arka.user_mservice.domain.models.AuthTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthService implements IAuthUseCase {
    private final AuthPersistencePort authPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenStoragePort refreshTokenStoragePort;

    @Override
    public Mono<AuthTokens> login(String email, String password) {
        return authPersistencePort.findUserByEmail(email)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")))
                .flatMap(user -> {
                    if (!passwordEncoderPort.matches(password, user.getPassword())) {
                        return Mono.error(new RuntimeException("Invalid credentials"));
                    }

                    String sessionId = UUID.randomUUID().toString();
                    String accessToken = tokenProviderPort.generateAccessToken(user.getId(), user.getUsername());
                    String refreshToken = tokenProviderPort.generateRefreshToken(user.getId(), sessionId);

                    Duration expiration = Duration.between(
                            Instant.now(),
                            tokenProviderPort.getExpiration(refreshToken).toInstant()
                    );

                    return refreshTokenStoragePort.save(user.getId(), sessionId, refreshToken, expiration)
                            .thenReturn(new AuthTokens(accessToken, refreshToken, sessionId));
                });
    }

    @Override
    public Mono<Void> logout(String refreshToken, String sessionId) {
        if (!StringUtils.hasText(refreshToken) || !StringUtils.hasText(sessionId)) {
            return Mono.error(new IllegalArgumentException("Invalid refresh token or session ID"));
        }

        UUID userId = tokenProviderPort.getUserIdFromToken(refreshToken);
        return refreshTokenStoragePort.delete(userId, sessionId);
    }

    @Override
    public Mono<Void> logoutAll(String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            return Mono.error(new IllegalArgumentException("Missing access token"));
        }

        UUID userId = tokenProviderPort.getUserIdFromToken(accessToken);
        return refreshTokenStoragePort.deleteAllByUserId(userId);
    }
}
