package com.arka.user_mservice.application.usecases;

import com.arka.user_mservice.application.ports.in.IUserProfileUseCases;
import com.arka.user_mservice.application.ports.out.UserPersistencePort;
import com.arka.user_mservice.application.ports.out.UserProfilePersistencePort;
import com.arka.user_mservice.domain.exceptions.ResourceAlreadyExistsException;
import com.arka.user_mservice.domain.exceptions.ResourceNotFoundException;
import com.arka.user_mservice.domain.models.UserProfileModel;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class UserProfileService implements IUserProfileUseCases {
    private final UserProfilePersistencePort userProfilePersistencePort;
    private final UserPersistencePort userPersistencePort;

    @Override
    public Mono<UserProfileModel> create(UserProfileModel profile) {
        return userPersistencePort.findById(profile.getUserId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User with ID " + profile.getUserId() + " not found")))
                .flatMap(user -> userProfilePersistencePort.findByUserId(profile.getUserId())
                        .flatMap(existing -> Mono.error(new ResourceAlreadyExistsException("User already has a profile")))
                        .switchIfEmpty(Mono.defer(() -> Mono.just(profile))))
                .cast(UserProfileModel.class)
                .flatMap(userProfilePersistencePort::save);
    }

    @Override
    public Mono<UserProfileModel> update(UUID profileId, UserProfileModel profile) {
        return userProfilePersistencePort.findById(profileId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Profile not found with ID: " + profileId)))
                .flatMap(existing -> {
                    if (profile.getFullName() != null && !profile.getFullName().equals(existing.getFullName())) {
                        existing.setFullName(profile.getFullName());
                    }
                    if (profile.getCellphone() != null && !profile.getCellphone().equals(existing.getCellphone())) {
                        existing.setCellphone(profile.getCellphone());
                    }
                    if (profile.getDocType() != null && !profile.getDocType().equals(existing.getDocType())) {
                        existing.setDocType(profile.getDocType());
                    }
                    if (profile.getDocNumber() != null && !profile.getDocNumber().equals(existing.getDocNumber())) {
                        existing.setDocNumber(profile.getDocNumber());
                    }
                    if (profile.getAddress() != null && !profile.getAddress().equals(existing.getAddress())) {
                        existing.setAddress(profile.getAddress());
                    }
                    if (profile.getCountry() != null && !profile.getCountry().equals(existing.getCountry())) {
                        existing.setCountry(profile.getCountry());
                    }
                    if (profile.getCity() != null && !profile.getCity().equals(existing.getCity())) {
                        existing.setCity(profile.getCity());
                    }
                    return userProfilePersistencePort.save(existing);
                });
    }

    @Override
    public Mono<Void> deleteById(UUID profileId) {
        return userProfilePersistencePort.findById(profileId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Profile not found with ID: " + profileId)))
                .flatMap(existing -> userProfilePersistencePort.deleteById(profileId));
    }

    @Override
    public Mono<UserProfileModel> findById(UUID profileId) {
        return userProfilePersistencePort.findById(profileId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Profile not found with ID: " + profileId)));
    }

    @Override
    public Mono<UserProfileModel> findByUserId(UUID userId) {
        return userProfilePersistencePort.findByUserId(userId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Profile not found for user with ID: " + userId)));
    }

    @Override
    public Flux<UserProfileModel> findAll() {
        return userProfilePersistencePort.findAll();
    }
}
