package com.arka.user_mservice.infra.adapters.in.config;

import com.arka.user_mservice.application.ports.in.IUserUseCases;
import com.arka.user_mservice.application.ports.out.RolePersistencePort;
import com.arka.user_mservice.application.ports.out.UserPersistencePort;
import com.arka.user_mservice.application.ports.out.UserProfilePersistencePort;
import com.arka.user_mservice.application.ports.out.UserRolePersistencePort;
import com.arka.user_mservice.application.usecases.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfig {
    @Bean
    public UserService userService(
            UserPersistencePort userPersistencePort,
            RolePersistencePort rolePersistencePort,
            UserRolePersistencePort userRolePersistencePort,
            UserProfilePersistencePort userProfilePersistencePort
    ) {
        return new UserService(userPersistencePort, rolePersistencePort, userRolePersistencePort, userProfilePersistencePort);
    }

    @Bean
    public IUserUseCases iUserUseCases(UserService userService) {
        return userService;
    }
}
