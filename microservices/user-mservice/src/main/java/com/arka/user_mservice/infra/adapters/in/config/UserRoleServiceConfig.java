package com.arka.user_mservice.infra.adapters.in.config;

import com.arka.user_mservice.application.ports.in.IUserRoleUseCases;
import com.arka.user_mservice.application.ports.out.RolePersistencePort;
import com.arka.user_mservice.application.ports.out.UserPersistencePort;
import com.arka.user_mservice.application.ports.out.UserRolePersistencePort;
import com.arka.user_mservice.application.usecases.UserRoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRoleServiceConfig {
    @Bean
    public UserRoleService userRoleService(
            UserRolePersistencePort userRolePersistencePort,
            UserPersistencePort userPersistencePort,
            RolePersistencePort rolePersistencePort) {
        return new UserRoleService(userRolePersistencePort, userPersistencePort, rolePersistencePort);
    }

    @Bean
    public IUserRoleUseCases iUserRoleUseCases(UserRoleService userRoleService) {
        return userRoleService;
    }
}
