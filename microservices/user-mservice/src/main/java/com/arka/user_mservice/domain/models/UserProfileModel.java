package com.arka.user_mservice.domain.models;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class UserProfileModel {
    private UUID id;
    private UUID userId;
    private String fullName;
    private String cellphone;
    private String docType;
    private String docNumber;
    private String address;
    private String country;
    private String city;
}
