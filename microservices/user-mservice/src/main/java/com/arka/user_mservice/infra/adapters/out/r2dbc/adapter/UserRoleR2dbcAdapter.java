package com.arka.user_mservice.infra.adapters.out.r2dbc.adapter;

import com.arka.user_mservice.application.ports.out.UserRolePersistencePort;
import com.arka.user_mservice.domain.models.RoleModel;
import com.arka.user_mservice.domain.models.UserRoleModel;
import com.arka.user_mservice.infra.adapters.out.r2dbc.entities.UserRoleEntity;
import com.arka.user_mservice.infra.adapters.out.r2dbc.mapper.RoleEntityMapper;
import com.arka.user_mservice.infra.adapters.out.r2dbc.mapper.UserRoleEntityMapper;
import com.arka.user_mservice.infra.adapters.out.r2dbc.repository.UserRoleR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRoleR2dbcAdapter implements UserRolePersistencePort {
    private final UserRoleR2dbcRepository userRoleR2dbcRepository;
    private final UserRoleEntityMapper userRoleEntityMapper;
    private final RoleEntityMapper roleEntityMapper;

    @Override
    public Mono<Void> assignRoleToUser(UUID userId, UUID roleId) {
        UserRoleEntity entity = new UserRoleEntity(userId, roleId);
        return userRoleR2dbcRepository.save(entity).then();
    }

    @Override
    public Mono<Void> removeRoleFromUser(UUID userId, UUID roleId) {
        return userRoleR2dbcRepository.deleteByUserIdAndRoleId(userId, roleId);
    }

    @Override
    public Flux<RoleModel> getRolesByUserId(UUID userId) {
        return userRoleR2dbcRepository.findRolesByUserId(userId)
                .map(roleEntityMapper::toModel);
    }
}
