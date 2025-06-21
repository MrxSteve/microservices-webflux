package com.arka.user_mservice.infra.adapters.out.r2dbc.repository;

import com.arka.user_mservice.infra.adapters.out.r2dbc.entities.RoleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RoleR2dbcRepository extends ReactiveCrudRepository<RoleEntity, UUID> {
    Mono<RoleEntity> findByName(String name);
    Mono<Boolean> existsByName(String name);
}
