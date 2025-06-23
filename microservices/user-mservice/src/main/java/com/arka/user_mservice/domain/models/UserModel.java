package com.arka.user_mservice.domain.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class UserModel {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private boolean enabled;
    private LocalDateTime createdAt;

    private UserProfileModel profile;
    private Set<RoleModel> roles;
}
