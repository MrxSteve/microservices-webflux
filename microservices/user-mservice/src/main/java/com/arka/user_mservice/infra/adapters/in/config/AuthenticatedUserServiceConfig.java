package com.arka.user_mservice.infra.adapters.in.config;

import com.arka.user_mservice.application.ports.in.IAuthenticatedUserUseCase;
import com.arka.user_mservice.application.ports.out.PasswordEncoderPort;
import com.arka.user_mservice.application.ports.out.UserPersistencePort;
import com.arka.user_mservice.application.ports.out.UserProfilePersistencePort;
import com.arka.user_mservice.application.ports.out.UserRolePersistencePort;
import com.arka.user_mservice.application.usecases.AuthenticatedUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticatedUserServiceConfig {
    @Bean
    public AuthenticatedUserService authenticatedUserService(
            UserPersistencePort userPersistencePort,
            UserProfilePersistencePort userProfilePersistencePort,
            UserRolePersistencePort userRolePersistencePort,
            PasswordEncoderPort passwordEncoderPort) {
        return new AuthenticatedUserService(
                userPersistencePort,
                userProfilePersistencePort,
                userRolePersistencePort,
                passwordEncoderPort);
    }

    @Bean
    public IAuthenticatedUserUseCase iAuthenticatedUserUseCase(AuthenticatedUserService authenticatedUserService) {
        return authenticatedUserService;
    }
}
