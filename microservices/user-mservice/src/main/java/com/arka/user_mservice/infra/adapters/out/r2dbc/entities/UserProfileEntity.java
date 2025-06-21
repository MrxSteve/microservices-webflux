package com.arka.user_mservice.infra.adapters.out.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
@Table("user_profiles")
public class UserProfileEntity {
    @Id
    private UUID id;

    @Column("user_id")
    private UUID userId;

    @Column("full_name")
    private String fullName;

    private String cellphone;

    @Column("doc_type")
    private String docType;

    @Column("doc_number")
    private String docNumber;

    private String address;
    private String country;
    private String city;
}
