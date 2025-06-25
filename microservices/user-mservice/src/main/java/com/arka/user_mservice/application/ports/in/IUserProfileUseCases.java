package com.arka.user_mservice.application.ports.in;

import com.arka.user_mservice.domain.models.UserProfileModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IUserProfileUseCases {
    Mono<UserProfileModel> create(UserProfileModel profile);
    Mono<UserProfileModel> update(UUID profileId, UserProfileModel profile);
    Mono<Void> deleteById(UUID profileId);
    Mono<UserProfileModel> findById(UUID profileId);
    Mono<UserProfileModel> findByUserId(UUID userId);
    Flux<UserProfileModel> findAll();
}
