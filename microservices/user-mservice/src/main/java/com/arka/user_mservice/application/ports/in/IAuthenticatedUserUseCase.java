package com.arka.user_mservice.application.ports.in;

import com.arka.user_mservice.domain.models.UserModel;
import com.arka.user_mservice.domain.models.UserProfileModel;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IAuthenticatedUserUseCase {
    Mono<UserModel> getMyUser(UUID userId);
    Mono<UserModel> updateMyUser(UUID userId, UserModel user);
    Mono<UserProfileModel> updateMyProfile(UUID userId, UserProfileModel profile);
}
