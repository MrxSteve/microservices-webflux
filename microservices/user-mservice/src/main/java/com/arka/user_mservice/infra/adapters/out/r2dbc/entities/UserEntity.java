package com.arka.user_mservice.infra.adapters.out.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
@Table("users")
public class UserEntity {
    @Id
    private UUID id;

    private String username;

    private String email;

    private String password;

    private Boolean enabled;

    @Column("created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

}
