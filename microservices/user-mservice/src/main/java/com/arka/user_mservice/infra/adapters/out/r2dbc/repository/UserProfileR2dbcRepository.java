package com.arka.user_mservice.infra.adapters.out.r2dbc.repository;

import com.arka.user_mservice.infra.adapters.out.r2dbc.entities.UserProfileEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserProfileR2dbcRepository extends ReactiveCrudRepository<UserProfileEntity, UUID> {
    Mono<UserProfileEntity> findByUserId(UUID userId);
}
