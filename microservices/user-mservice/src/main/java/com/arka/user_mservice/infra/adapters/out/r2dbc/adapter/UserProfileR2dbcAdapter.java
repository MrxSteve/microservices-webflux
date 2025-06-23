package com.arka.user_mservice.infra.adapters.out.r2dbc.adapter;

import com.arka.user_mservice.application.ports.out.UserProfilePersistencePort;
import com.arka.user_mservice.domain.models.UserProfileModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserProfileR2dbcAdapter implements UserProfilePersistencePort {
    @Override
    public Mono<UserProfileModel> findByUserId(UUID userId) {
        return null;
    }
}
