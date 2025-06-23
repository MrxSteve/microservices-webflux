package com.arka.user_mservice.application.ports.out;

import com.arka.user_mservice.domain.models.UserProfileModel;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserProfilePersistencePort {
    Mono<UserProfileModel> findByUserId(UUID userId);
}
