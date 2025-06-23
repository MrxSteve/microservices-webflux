package com.arka.user_mservice.application.ports.out;

import com.arka.user_mservice.domain.models.RoleModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRolePersistencePort {
    Mono<Void> assignRoleToUser(UUID userId, UUID roleId);
    Mono<Void> removeRoleFromUser(UUID userId, UUID roleId);
    Flux<RoleModel> getRolesByUserId(UUID userId);
}
