package com.arka.user_mservice.application.usecases;

import com.arka.user_mservice.application.ports.in.IUserRoleUseCases;
import com.arka.user_mservice.application.ports.out.RolePersistencePort;
import com.arka.user_mservice.application.ports.out.UserPersistencePort;
import com.arka.user_mservice.application.ports.out.UserRolePersistencePort;
import com.arka.user_mservice.domain.exceptions.ResourceNotFoundException;
import com.arka.user_mservice.domain.models.RoleModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class UserRoleService implements IUserRoleUseCases {
    private final UserRolePersistencePort userRolePersistencePort;
    private final UserPersistencePort userPersistencePort;
    private final RolePersistencePort rolePersistencePort;

    @Override
    public Mono<Void> assignRole(UUID userId, UUID roleId) {
        return verifyUserAndRoleExistence(userId, roleId)
                .then(userRolePersistencePort.assignRoleToUser(userId, roleId)
                        .onErrorResume(e -> Mono.error(new ResourceNotFoundException("Failed to assign role: " + e.getMessage()))));
    }

    @Override
    public Mono<Void> removeRole(UUID userId, UUID roleId) {
        return verifyUserAndRoleExistence(userId, roleId)
                .then(userRolePersistencePort.removeRoleFromUser(userId, roleId)
                        .onErrorResume(e -> Mono.error(new ResourceNotFoundException("Failed to remove role: " + e.getMessage()))));
    }

    @Override
    public Flux<RoleModel> getRolesByUser(UUID userId) {
        return userPersistencePort.findById(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User with ID " + userId + " not found")))
                .flatMapMany(user -> userRolePersistencePort.getRolesByUserId(userId)
                        .switchIfEmpty(Flux.error(new ResourceNotFoundException("No roles found for user with ID " + userId))));
    }

    private Mono<Void> verifyUserAndRoleExistence(UUID userId, UUID roleId) {
        return userPersistencePort.findById(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User with ID " + userId + " not found")))
                .then(rolePersistencePort.findById(roleId)
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Role with ID " + roleId + " not found"))))
                .then();
    }
}
