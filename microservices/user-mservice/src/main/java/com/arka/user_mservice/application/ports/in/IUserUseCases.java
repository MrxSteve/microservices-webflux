package com.arka.user_mservice.application.ports.in;

import com.arka.user_mservice.domain.models.UserModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IUserUseCases {
    Mono<UserModel> register(UserModel user);
    Mono<UserModel> findById(UUID userId);
    Mono<UserModel> findByUsernameOrEmail(String usernameOrEmail);
    Flux<UserModel> findAll();
    Mono<Void> deleteById(UUID userId);
    Mono<UserModel> update(UUID userId, UserModel user);

    Mono<UserModel> enable(UUID userId);
    Mono<UserModel> disable(UUID userId);
}
