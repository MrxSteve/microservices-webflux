package com.arka.user_mservice.infra.adapters.in.config;

import com.arka.user_mservice.application.ports.in.IRoleUseCases;
import com.arka.user_mservice.application.ports.out.RolePersistencePort;
import com.arka.user_mservice.application.usecases.RoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleServiceConfig {
    @Bean
    public RoleService roleService(RolePersistencePort rolePersistencePort) {
        return new RoleService(rolePersistencePort);
    }

    @Bean
    public IRoleUseCases iRoleUseCases(RoleService roleService) {
        return roleService;
    }
}
