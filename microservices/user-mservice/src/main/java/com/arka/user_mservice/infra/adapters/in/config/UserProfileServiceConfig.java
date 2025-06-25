package com.arka.user_mservice.infra.adapters.in.config;

import com.arka.user_mservice.application.ports.in.IUserProfileUseCases;
import com.arka.user_mservice.application.ports.out.UserPersistencePort;
import com.arka.user_mservice.application.ports.out.UserProfilePersistencePort;
import com.arka.user_mservice.application.usecases.UserProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserProfileServiceConfig {
    @Bean
    public UserProfileService userProfileService(
            UserProfilePersistencePort userProfilePersistencePort,
            UserPersistencePort userPersistencePort) {
        return new UserProfileService(userProfilePersistencePort, userPersistencePort);
    }

    @Bean
    public IUserProfileUseCases iUserProfileUseCases(UserProfileService userProfileService) {
        return userProfileService;
    }
}
