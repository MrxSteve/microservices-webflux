package com.arka.user_mservice.infra.adapters.in.config;

import com.arka.user_mservice.application.ports.in.IAuthUseCase;
import com.arka.user_mservice.application.ports.out.*;
import com.arka.user_mservice.application.usecases.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthServiceConfig {
    @Bean
    public AuthService authService(
            AuthPersistencePort authPersistencePort,
            PasswordEncoderPort passwordEncoderPort,
            TokenProviderPort tokenProviderPort,
            RefreshTokenStoragePort refreshTokenStoragePort,
            UserRolePersistencePort userRolePersistencePort,
            UserProfilePersistencePort userProfilePersistencePort) {
        return new AuthService(authPersistencePort, passwordEncoderPort,
                tokenProviderPort, refreshTokenStoragePort,
                userRolePersistencePort, userProfilePersistencePort);
    }

    @Bean
    public IAuthUseCase iAuthUseCase(AuthService authService) {
        return authService;
    }
}
