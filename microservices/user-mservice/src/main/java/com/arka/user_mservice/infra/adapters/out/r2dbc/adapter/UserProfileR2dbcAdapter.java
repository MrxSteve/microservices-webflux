package com.arka.user_mservice.infra.adapters.out.r2dbc.adapter;

import com.arka.user_mservice.application.ports.out.UserProfilePersistencePort;
import com.arka.user_mservice.domain.models.UserProfileModel;
import com.arka.user_mservice.infra.adapters.out.r2dbc.mapper.UserProfileEntityMapper;
import com.arka.user_mservice.infra.adapters.out.r2dbc.repository.UserProfileR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserProfileR2dbcAdapter implements UserProfilePersistencePort {
    private final UserProfileR2dbcRepository userProfileR2dbcRepository;
    private final UserProfileEntityMapper userProfileEntityMapper;

    @Override
    public Mono<UserProfileModel> save(UserProfileModel profile) {
        return userProfileR2dbcRepository.save(userProfileEntityMapper.toEntity(profile))
                .map(userProfileEntityMapper::toModel);
    }

    @Override
    public Mono<UserProfileModel> findById(UUID profileId) {
        return userProfileR2dbcRepository.findById(profileId)
                .map(userProfileEntityMapper::toModel);
    }

    @Override
    public Mono<UserProfileModel> findByUserId(UUID userId) {
        return userProfileR2dbcRepository.findByUserId(userId)
                .map(userProfileEntityMapper::toModel);
    }

    @Override
    public Flux<UserProfileModel> findAll() {
        return userProfileR2dbcRepository.findAll().map(userProfileEntityMapper::toModel);
    }

    @Override
    public Mono<Void> deleteById(UUID profileId) {
        return userProfileR2dbcRepository.deleteById(profileId);
    }
}
