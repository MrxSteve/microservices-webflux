package com.arka.user_mservice.infra.adapters.out.r2dbc.adapter;

import com.arka.user_mservice.application.ports.out.AuthPersistencePort;
import com.arka.user_mservice.application.ports.out.UserPersistencePort;
import com.arka.user_mservice.domain.models.UserModel;
import com.arka.user_mservice.infra.adapters.out.r2dbc.mapper.UserEntityMapper;
import com.arka.user_mservice.infra.adapters.out.r2dbc.repository.UserR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserR2dbcAdapter implements
        UserPersistencePort,
        AuthPersistencePort {

    private final UserR2dbcRepository userR2dbcRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Mono<UserModel> save(UserModel user) {
        return userR2dbcRepository.save(userEntityMapper.toEntity(user))
                .map(userEntityMapper::toModel);
    }

    @Override
    public Mono<UserModel> findById(UUID userId) {
        return userR2dbcRepository.findById(userId)
                .map(userEntityMapper::toModel);
    }

    @Override
    public Mono<UserModel> findByUsername(String username) {
        return userR2dbcRepository.findByUsername(username)
                .map(userEntityMapper::toModel);
    }

    @Override
    public Mono<UserModel> findByEmail(String email) {
        return userR2dbcRepository.findByEmail(email)
                .map(userEntityMapper::toModel);
    }

    @Override
    public Mono<UserModel> findByUsernameOrEmail(String usernameOrEmail) {
        return userR2dbcRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .map(userEntityMapper::toModel);
    }

    @Override
    public Flux<UserModel> findAll() {
        return userR2dbcRepository.findAll()
                .map(userEntityMapper::toModel);
    }

    @Override
    public Mono<Void> deleteById(UUID userId) {
        return userR2dbcRepository.deleteById(userId);
    }

    @Override
    public Mono<Boolean> existsByUsername(String username) {
        return userR2dbcRepository.existsByUsername(username);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return userR2dbcRepository.existsByEmail(email);
    }

    // AuthPersistencePort
    @Override
    public Mono<UserModel> findUserByEmail(String email) {
        return userR2dbcRepository.findByEmail(email)
                .map(userEntityMapper::toModel);
    }

    @Override
    public Mono<UserModel> findByUserId(UUID userId) {
        return userR2dbcRepository.findById(userId)
                .map(userEntityMapper::toModel);
    }
}
