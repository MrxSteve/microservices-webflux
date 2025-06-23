package com.arka.user_mservice.infra.adapters.out.r2dbc.entities;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
@Table("user_roles")
public class UserRoleEntity {
    @Column("user_id")
    private UUID userId;

    @Column("role_id")
    private UUID roleId;
}
