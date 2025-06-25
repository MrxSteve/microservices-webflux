package com.arka.user_mservice.application.usecases;

import com.arka.user_mservice.application.ports.in.IAuthUseCase;
import com.arka.user_mservice.application.ports.out.AuthPersistencePort;
import com.arka.user_mservice.application.ports.out.PasswordEncoderPort;
import com.arka.user_mservice.application.ports.out.TokenProviderPort;
import com.arka.user_mservice.domain.models.AuthTokens;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthService implements IAuthUseCase {
    private final AuthPersistencePort authPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    @Override
    public Mono<AuthTokens> login(String email, String password) {
        return authPersistencePort.findUserByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with email: " + email)))
                .flatMap(user -> {
                    if (!passwordEncoderPort.matches(password, user.getPassword())) {
                        return Mono.error(new RuntimeException("Invalid credentials"));
                    }

                    String accessToken = tokenProviderPort.generateAccessToken(user.getId(), user.getUsername());
                    String refreshToken = tokenProviderPort.generateRefreshToken(user.getId());

                    return Mono.just(new AuthTokens(accessToken, refreshToken));
                });
    }
}
