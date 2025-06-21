package com.arka.user_mservice.domain.models;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class RoleModel {
    private UUID id;
    private String name;
}
