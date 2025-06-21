package com.arka.user_mservice.infra.adapters.out.r2dbc.adapter;

import com.arka.user_mservice.application.ports.out.RolePersistencePort;
import com.arka.user_mservice.domain.models.RoleModel;
import com.arka.user_mservice.infra.adapters.out.r2dbc.mapper.RoleEntityMapper;
import com.arka.user_mservice.infra.adapters.out.r2dbc.repository.RoleR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoleR2dbcAdapter implements RolePersistencePort {
    private final RoleR2dbcRepository roleR2dbcRepository;
    private final RoleEntityMapper roleEntityMapper;

    @Override
    public Mono<RoleModel> save(RoleModel role) {
        return roleR2dbcRepository.save(roleEntityMapper.toEntity(role))
                .map(roleEntityMapper::toModel);
    }

    @Override
    public Mono<RoleModel> findById(UUID roleId) {
        return roleR2dbcRepository.findById(roleId)
                .map(roleEntityMapper::toModel);
    }

    @Override
    public Mono<Void> deleteById(UUID roleId) {
        return roleR2dbcRepository.deleteById(roleId);
    }

    @Override
    public Mono<RoleModel> findByName(String roleName) {
        return roleR2dbcRepository.findByName(roleName)
                .map(roleEntityMapper::toModel);
    }

    @Override
    public Mono<Boolean> existsByName(String roleName) {
        return roleR2dbcRepository.existsByName(roleName);
    }

    @Override
    public Flux<RoleModel> findAll() {
        return roleR2dbcRepository.findAll()
                .map(roleEntityMapper::toModel);
    }
}
