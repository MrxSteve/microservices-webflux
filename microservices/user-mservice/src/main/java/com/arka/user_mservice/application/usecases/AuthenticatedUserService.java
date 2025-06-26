package com.arka.user_mservice.application.usecases;

import com.arka.user_mservice.application.ports.in.IAuthenticatedUserUseCase;
import com.arka.user_mservice.application.ports.out.PasswordEncoderPort;
import com.arka.user_mservice.application.ports.out.UserPersistencePort;
import com.arka.user_mservice.application.ports.out.UserProfilePersistencePort;
import com.arka.user_mservice.application.ports.out.UserRolePersistencePort;
import com.arka.user_mservice.domain.exceptions.ResourceNotFoundException;
import com.arka.user_mservice.domain.models.UserModel;
import com.arka.user_mservice.domain.models.UserProfileModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.UUID;

@RequiredArgsConstructor
public class AuthenticatedUserService implements IAuthenticatedUserUseCase {
    private final UserPersistencePort userPersistencePort;
    private final UserProfilePersistencePort userProfilePersistencePort;
    private final UserRolePersistencePort userRolePersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public Mono<UserModel> getMyUser(UUID userId) {
        return userPersistencePort.findById(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")))
                .flatMap(user -> Mono.zip(
                        userRolePersistencePort.getRolesByUserId(userId).collectList(),
                        userProfilePersistencePort.findByUserId(userId).switchIfEmpty(Mono.justOrEmpty(null))
                ).map(tuple -> {
                    user.setRoles(new HashSet<>(tuple.getT1()));
                    user.setProfile(tuple.getT2());
                    return user;
                }));
    }

    @Override
    public Mono<UserModel> updateMyUser(UUID userId, UserModel updated) {
        return userPersistencePort.findById(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")))
                .flatMap(existing -> {
                    if (updated.getUsername() != null) existing.setUsername(updated.getUsername());
                    if (updated.getEmail() != null) existing.setEmail(updated.getEmail());
                    if (updated.getPassword() != null) {
                        existing.setPassword(passwordEncoderPort.encode(updated.getPassword()));
                    }
                    if (updated.getCreatedAt() != null) existing.setCreatedAt(updated.getCreatedAt());
                    existing.setEnabled(true);
                    return userPersistencePort.save(existing);
                });
    }

    @Override
    public Mono<UserProfileModel> updateMyProfile(UUID userId, UserProfileModel profile) {
        return userProfilePersistencePort.findByUserId(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Profile not found")))
                .flatMap(existing -> {
                    existing.setFullName(profile.getFullName());
                    existing.setCellphone(profile.getCellphone());
                    existing.setAddress(profile.getAddress());
                    existing.setCity(profile.getCity());
                    existing.setCountry(profile.getCountry());
                    existing.setDocNumber(profile.getDocNumber());
                    existing.setDocType(profile.getDocType());
                    return userProfilePersistencePort.save(existing);
                });
    }
}

