package com.arka.user_mservice.application.ports.out;

import com.arka.user_mservice.domain.models.UserModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserPersistencePort {
    Mono<UserModel> save(UserModel user);
    Mono<UserModel> findById(UUID userId);
    Mono<UserModel> findByUsername(String username);
    Mono<UserModel> findByEmail(String email);
    Mono<UserModel> findByUsernameOrEmail(String usernameOrEmail);

    Flux<UserModel> findAll();
    Mono<Void> deleteById(UUID userId);

    Mono<Boolean> existsByUsername(String username);
    Mono<Boolean> existsByEmail(String email);
}
