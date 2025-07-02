package com.arka.user_mservice.infra.init;

import com.arka.user_mservice.application.ports.out.*;
import com.arka.user_mservice.domain.models.RoleModel;
import com.arka.user_mservice.domain.models.UserModel;
import com.arka.user_mservice.domain.models.UserProfileModel;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RolePersistencePort rolePersistencePort;
    private final UserPersistencePort userPersistencePort;
    private final UserRolePersistencePort userRolePersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final UserProfilePersistencePort userProfilePersistencePort;

    @Value("${admin.password}")
    private String adminPassword;

    @PostConstruct
    public void initData() {
        initializeRoles()
                .then(initializeAdminUser())
                .subscribe();
    }

    private Mono<Void> initializeRoles() {
        return Flux.just("ROLE_ADMIN", "ROLE_CLIENTE")
                .flatMap(roleName ->
                        rolePersistencePort.findByName(roleName)
                                .switchIfEmpty(
                                        rolePersistencePort.save(new RoleModel(null, roleName))
                                                .doOnNext(r -> log.info("Creating role: {}", r.getName()))
                                )
                ).then();
    }

    private Mono<Void> initializeAdminUser() {
        String defaultAdminEmail = "admin@admin.com";

        return userPersistencePort.findByEmail(defaultAdminEmail)
                .switchIfEmpty(
                        Mono.defer(() -> {
                            log.info("Creating default admin user with email: {}", defaultAdminEmail);

                            UserModel admin = new UserModel();
                            admin.setUsername("admin");
                            admin.setEmail(defaultAdminEmail);
                            admin.setPassword(passwordEncoderPort.encode(adminPassword));
                            admin.setEnabled(true);
                            admin.setCreatedAt(LocalDateTime.now());

                            return userPersistencePort.save(admin)
                                    .flatMap(savedAdmin ->
                                            rolePersistencePort.findByName("ROLE_ADMIN")
                                                    .flatMap(adminRole ->
                                                            userRolePersistencePort.assignRoleToUser(savedAdmin.getId(), adminRole.getId())
                                                    )
                                                    .then(Mono.defer(() -> userProfilePersistencePort.save(
                                                            UserProfileModel.builder()
                                                                    .userId(savedAdmin.getId())
                                                                    .fullName("Administrador")
                                                                    .cellphone("")
                                                                    .docType("")
                                                                    .docNumber("")
                                                                    .address("")
                                                                    .country("")
                                                                    .city("")
                                                                    .build()
                                                    )))
                                                    .thenReturn(savedAdmin)
                                    );
                        })
                )
                .then();
    }
}

