package com.arka.user_mservice.application.ports.out;

import com.arka.user_mservice.domain.models.UserModel;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AuthPersistencePort {
    Mono<UserModel> findUserByEmail(String email);
    Mono<UserModel> findByUserId(UUID userId);
}
