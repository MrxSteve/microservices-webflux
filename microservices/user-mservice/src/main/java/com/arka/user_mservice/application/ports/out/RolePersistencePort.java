package com.arka.user_mservice.application.ports.out;

import com.arka.user_mservice.domain.models.RoleModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface RolePersistencePort {
    Mono<RoleModel> save(RoleModel role);
    Mono<RoleModel> findById(UUID roleId);
    Mono<Void> deleteById(UUID roleId);
    Mono<RoleModel> findByName(String roleName);
    Mono<Boolean> existsByName(String roleName);
    Flux<RoleModel> findAll();
}
