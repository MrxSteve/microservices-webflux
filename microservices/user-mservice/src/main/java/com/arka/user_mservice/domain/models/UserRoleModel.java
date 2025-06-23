package com.arka.user_mservice.domain.models;

import lombok.*;

import java.util.UUID;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class UserRoleModel {
    private UUID userId;
    private UUID roleId;
}

