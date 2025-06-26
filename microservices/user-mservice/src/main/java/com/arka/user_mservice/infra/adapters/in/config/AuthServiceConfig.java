package com.arka.user_mservice.infra.adapters.in.config;

import com.arka.user_mservice.application.ports.in.IAuthUseCase;
import com.arka.user_mservice.application.ports.out.AuthPersistencePort;
import com.arka.user_mservice.application.ports.out.PasswordEncoderPort;
import com.arka.user_mservice.application.ports.out.RefreshTokenStoragePort;
import com.arka.user_mservice.application.ports.out.TokenProviderPort;
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
            RefreshTokenStoragePort refreshTokenStoragePort) {
        return new AuthService(authPersistencePort, passwordEncoderPort,
                tokenProviderPort, refreshTokenStoragePort);
    }

    @Bean
    public IAuthUseCase iAuthUseCase(AuthService authService) {
        return authService;
    }
}
