package com.arka.user_mservice.infra.adapters.out.r2dbc.repository;

import com.arka.user_mservice.infra.adapters.out.r2dbc.entities.RoleEntity;
import com.arka.user_mservice.infra.adapters.out.r2dbc.entities.UserRoleEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRoleR2dbcRepository extends ReactiveCrudRepository<UserRoleEntity, Void> {
    Mono<Void> deleteByUserIdAndRoleId(UUID userId, UUID roleId);

    @Query("SELECT r.* FROM roles r " +
            "INNER JOIN user_roles ur ON ur.role_id = r.id " +
            "WHERE ur.user_id = :userId")
    Flux<RoleEntity> findRolesByUserId(UUID userId);
}
