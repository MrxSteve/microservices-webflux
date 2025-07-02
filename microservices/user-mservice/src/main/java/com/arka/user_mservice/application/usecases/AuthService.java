package com.arka.user_mservice.application.usecases;

import com.arka.user_mservice.application.ports.in.IAuthUseCase;
import com.arka.user_mservice.application.ports.out.*;
import com.arka.user_mservice.domain.exceptions.ResourceNotFoundException;
import com.arka.user_mservice.domain.models.AuthTokens;
import com.arka.user_mservice.domain.models.RoleModel;
import com.arka.user_mservice.domain.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthService implements IAuthUseCase {
    private final AuthPersistencePort authPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenStoragePort refreshTokenStoragePort;
    private final UserRolePersistencePort userRolePersistencePort;
    private final UserProfilePersistencePort userProfilePersistencePort;

    @Override
    public Mono<AuthTokens> login(String email, String password) {
        return authPersistencePort.findUserByEmail(email)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")))
                .flatMap(user -> {
                    if (!passwordEncoderPort.matches(password, user.getPassword())) {
                        return Mono.error(new RuntimeException("Invalid credentials"));
                    }

                    return enrichUser(user).flatMap(enrichedUser -> {
                        String sessionId = UUID.randomUUID().toString();

                        List<String> roles = enrichedUser.getRoles().stream()
                                .map(RoleModel::getName)
                                .toList();

                        String accessToken = tokenProviderPort.generateAccessToken(enrichedUser.getId(), enrichedUser.getUsername(), roles);

                        String refreshToken = tokenProviderPort.generateRefreshToken(enrichedUser.getId(), sessionId);

                        Duration expiration = Duration.between(
                                Instant.now(),
                                tokenProviderPort.getExpiration(refreshToken).toInstant()
                        );

                        return refreshTokenStoragePort.save(enrichedUser.getId(), sessionId, refreshToken, expiration)
                                .thenReturn(new AuthTokens(accessToken, refreshToken, sessionId));
                    });
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
