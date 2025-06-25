package com.arka.user_mservice.application.ports.out;

import com.arka.user_mservice.domain.models.UserModel;
import reactor.core.publisher.Mono;

public interface AuthPersistencePort {
    Mono<UserModel> findUserByEmail(String email);
}
