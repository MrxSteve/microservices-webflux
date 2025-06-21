package com.arka.user_mservice.application.ports.in;

import com.arka.user_mservice.domain.models.RoleModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IRoleUseCases {
    Mono<RoleModel> create(RoleModel role);
    Mono<RoleModel> findById(UUID roleId);
    Mono<RoleModel> findByName(String roleName);
    Mono<Void> deleteById(UUID roleId);
    Flux<RoleModel> findAll();
}
