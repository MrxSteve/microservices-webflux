package com.arka.user_mservice.application.usecases;

import com.arka.user_mservice.application.ports.in.IRefreshTokenUseCase;
import com.arka.user_mservice.application.ports.out.*;
import com.arka.user_mservice.domain.models.AuthTokens;
import com.arka.user_mservice.domain.models.RoleModel;
import com.arka.user_mservice.domain.models.UserModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenUseCase {

    private final RefreshTokenStoragePort refreshTokenStoragePort;
    private final TokenProviderPort tokenProviderPort;
    private final AuthPersistencePort authPersistencePort;
    private final UserRolePersistencePort userRolePersistencePort;
    private final UserProfilePersistencePort userProfilePersistencePort;

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
                .flatMap(this::enrichUser)
                .flatMap(enrichedUser -> {
                    List<String> roles = enrichedUser.getRoles().stream()
                            .map(RoleModel::getName)
                            .toList();

                    String newAccessToken = tokenProviderPort.generateAccessToken(enrichedUser.getId(), enrichedUser.getUsername(), roles);

                    String newRefreshToken = tokenProviderPort.generateRefreshToken(enrichedUser.getId(), sessionId);

                    Duration expiration = Duration.between(
                            Instant.now(),
                            tokenProviderPort.getExpiration(newRefreshToken).toInstant()
                    );

                    return refreshTokenStoragePort.save(enrichedUser.getId(), sessionId, newRefreshToken, expiration)
                            .thenReturn(new AuthTokens(newAccessToken, newRefreshToken, sessionId));
                });
    }

    private Mono<UserModel> enrichUser(UserModel user) {
        return Mono.zip(
                userRolePersistencePort.getRolesByUserId(user.getId()).collectList(),
                userProfilePersistencePort.findByUserId(user.getId()).switchIfEmpty(Mono.justOrEmpty(null))
        ).map(tuple -> {
            user.setRoles(new HashSet<>(tuple.getT1()));
            user.setProfile(tuple.getT2());
            return user;
        });
    }
}

