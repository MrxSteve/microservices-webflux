package com.arka.user_mservice.application.usecases;

import com.arka.user_mservice.application.ports.in.IRoleUseCases;
import com.arka.user_mservice.application.ports.out.RolePersistencePort;
import com.arka.user_mservice.domain.exceptions.ResourceAlreadyExistsException;
import com.arka.user_mservice.domain.exceptions.ResourceNotFoundException;
import com.arka.user_mservice.domain.models.RoleModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class RoleService implements IRoleUseCases {
    private final RolePersistencePort rolePersistencePort;

    @Override
    public Mono<RoleModel> create(RoleModel role) {
        return rolePersistencePort.existsByName(role.getName())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new ResourceAlreadyExistsException("Role already exists with name: " + role.getName()));
                    }
                    return rolePersistencePort.save(role);
                });
    }

    @Override
    public Mono<RoleModel> findById(UUID roleId) {
        return rolePersistencePort.findById(roleId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Role not found with id: " + roleId)));
    }

    @Override
    public Mono<RoleModel> findByName(String roleName) {
        return rolePersistencePort.findByName(roleName)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Role not found with name: " + roleName)));
    }

    @Override
    public Mono<Void> deleteById(UUID roleId) {
        return rolePersistencePort.findById(roleId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Role not found with id: " + roleId)))
                .flatMap(existing -> rolePersistencePort.deleteById(roleId));
    }

    @Override
    public Flux<RoleModel> findAll() {
        return rolePersistencePort.findAll();
    }
}
