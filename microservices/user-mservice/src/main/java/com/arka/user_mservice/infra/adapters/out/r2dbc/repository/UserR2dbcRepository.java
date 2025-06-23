package com.arka.user_mservice.infra.adapters.out.r2dbc.repository;

import com.arka.user_mservice.infra.adapters.out.r2dbc.entities.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserR2dbcRepository extends ReactiveCrudRepository<UserEntity, UUID> {
    Mono<UserEntity> findByUsername(String username);
    Mono<UserEntity> findByEmail(String email);
    Mono<UserEntity> findByUsernameOrEmail(String username, String email);
    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByEmail(String email);
}
