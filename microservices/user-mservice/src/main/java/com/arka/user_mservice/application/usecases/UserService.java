package com.arka.user_mservice.application.usecases;

import com.arka.user_mservice.application.ports.in.IUserUseCases;
import com.arka.user_mservice.application.ports.out.*;
import com.arka.user_mservice.domain.exceptions.ResourceAlreadyExistsException;
import com.arka.user_mservice.domain.exceptions.ResourceNotFoundException;
import com.arka.user_mservice.domain.models.UserModel;
import com.arka.user_mservice.domain.models.UserProfileModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@RequiredArgsConstructor
public class UserService implements IUserUseCases {

    private final UserPersistencePort userPersistencePort;
    private final RolePersistencePort rolePersistencePort;
    private final UserRolePersistencePort userRolePersistencePort;
    private final UserProfilePersistencePort userProfilePersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public Mono<UserModel> register(UserModel user) {
        return userPersistencePort.existsByUsername(user.getUsername())
                .flatMap(usernameExists -> {
                    if (usernameExists) {
                        return Mono.error(new ResourceAlreadyExistsException("Username is already in use: " + user.getUsername()));
                    }
                    return userPersistencePort.existsByEmail(user.getEmail());
                })
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.error(new ResourceAlreadyExistsException("Email is already in use: " + user.getEmail()));
                    }

                    user.setPassword(passwordEncoderPort.encode(user.getPassword()));
                    user.setEnabled(true);
                    user.setCreatedAt(LocalDateTime.now());

                    return rolePersistencePort.findByName("CLIENTE")
                            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Default role CLIENTE not found")))
                            .flatMap(defaultRole ->
                                    userPersistencePort.save(user)
                                            .flatMap(savedUser ->
                                                    userRolePersistencePort.assignRoleToUser(savedUser.getId(), defaultRole.getId())
                                                            .then(
                                                                    userProfilePersistencePort.save(UserProfileModel.builder()
                                                                            .userId(savedUser.getId())
                                                                            .fullName("")
                                                                            .cellphone("")
                                                                            .docType("")
                                                                            .docNumber("")
                                                                            .address("")
                                                                            .country("")
                                                                            .city("")
                                                                            .build())
                                                            )
                                                            .thenReturn(savedUser)
                                            )
                            );
                })
                .flatMap(this::enrichUser);
    }

    @Override
    public Mono<UserModel> findById(UUID userId) {
        return userPersistencePort.findById(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with ID: " + userId)))
                .flatMap(this::enrichUser);
    }

    @Override
    public Mono<UserModel> findByUsernameOrEmail(String usernameOrEmail) {
        return userPersistencePort.findByUsernameOrEmail(usernameOrEmail)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with username or email: " + usernameOrEmail)))
                .flatMap(this::enrichUser);
    }

    @Override
    public Flux<UserModel> findAll() {
        return userPersistencePort.findAll()
                .flatMap(this::enrichUser);
    }

    @Override
    public Mono<Void> deleteById(UUID userId) {
        return userPersistencePort.findById(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with ID: " + userId)))
                .flatMap(existing -> userPersistencePort.deleteById(userId));
    }

    @Override
    public Mono<UserModel> update(UUID userId, UserModel user) {
        return userPersistencePort.findById(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with ID: " + userId)))
                .flatMap(existing -> {
                    if (user.getUsername() != null && !user.getUsername().equals(existing.getUsername())) {
                        return userPersistencePort.existsByUsername(user.getUsername())
                                .flatMap(usernameExists -> {
                                    if (usernameExists) {
                                        return Mono.error(new ResourceAlreadyExistsException("Username is already in use: " + user.getUsername()));
                                    }
                                    return Mono.just(existing);
                                });
                    }
                    return Mono.just(existing);
                })
                .flatMap(existing -> {
                    if (user.getEmail() != null && !user.getEmail().equals(existing.getEmail())) {
                        return userPersistencePort.existsByEmail(user.getEmail())
                                .flatMap(emailExists -> {
                                    if (emailExists) {
                                        return Mono.error(new ResourceAlreadyExistsException("Email is already in use: " + user.getEmail()));
                                    }
                                    return Mono.just(existing);
                                });
                    }
                    return Mono.just(existing);
                })
                .flatMap(existing -> {
                    if (user.getUsername() != null) {
                        existing.setUsername(user.getUsername());
                    }
                    if (user.getEmail() != null) {
                        existing.setEmail(user.getEmail());
                    }
                    if (user.getPassword() != null) {
                        existing.setPassword(passwordEncoderPort.encode(user.getPassword()));
                    }
                    if (user.getCreatedAt() != null) {
                        existing.setCreatedAt(user.getCreatedAt());
                    }
                    existing.setEnabled(user.isEnabled());
                    return userPersistencePort.save(existing);
                })
                .flatMap(this::enrichUser);
    }

    @Override
    public Mono<UserModel> enable(UUID userId) {
        return userPersistencePort.findById(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with ID: " + userId)))
                .flatMap(user -> {
                    user.setEnabled(true);
                    return userPersistencePort.save(user);
                })
                .flatMap(this::enrichUser);
    }

    @Override
    public Mono<UserModel> disable(UUID userId) {
        return userPersistencePort.findById(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found with ID: " + userId)))
                .flatMap(user -> {
                    user.setEnabled(false);
                    return userPersistencePort.save(user);
                })
                .flatMap(this::enrichUser);
    }

    private Mono<UserModel> enrichUser(UserModel user) {
        return Mono.zip(
                userRolePersistencePort.getRolesByUserId(user.getId()).collectList(),
                userProfilePersistencePort.findByUserId(user.getId()).switchIfEmpty(Mono.justOrEmpty(null))
        ).map(tuple -> {
            user.setRoles(new HashSet<>(tuple.getT1()));
            user.setProfile(tuple.getT2());
            return user;
        });
    }
}
