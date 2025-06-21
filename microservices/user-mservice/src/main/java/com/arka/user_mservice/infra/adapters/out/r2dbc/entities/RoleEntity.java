package com.arka.user_mservice.infra.adapters.out.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
@Table("roles")
public class RoleEntity {
    @Id
    private UUID id;

    private String name;
}
