package com.arka.user_mservice.infra.adapters.in.config;

import com.arka.user_mservice.application.ports.in.IRefreshTokenUseCase;
import com.arka.user_mservice.application.ports.out.AuthPersistencePort;
import com.arka.user_mservice.application.ports.out.RefreshTokenStoragePort;
import com.arka.user_mservice.application.ports.out.TokenProviderPort;
import com.arka.user_mservice.application.usecases.RefreshTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RefreshTokenServiceConfig {
    @Bean
    public RefreshTokenService refreshTokenService(
            RefreshTokenStoragePort refreshTokenStoragePort,
            TokenProviderPort tokenProviderPort,
            AuthPersistencePort authPersistencePort) {
        return new RefreshTokenService(refreshTokenStoragePort, tokenProviderPort, authPersistencePort);
    }

    @Bean
    public IRefreshTokenUseCase iRefreshTokenUseCase(RefreshTokenService refreshTokenService) {
        return refreshTokenService;
    }
}
