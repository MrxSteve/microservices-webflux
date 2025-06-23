package com.arka.user_mservice.application.ports.in;

import com.arka.user_mservice.domain.models.RoleModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IUserRoleUseCases {
    Mono<Void> assignRole(UUID userId, UUID roleId);
    Mono<Void> removeRole(UUID userId, UUID roleId);
    Flux<RoleModel> getRolesByUser(UUID userId);
}
