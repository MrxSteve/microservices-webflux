package com.arka.user_mservice.infra.adapters.in.rest.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class UserProfileRequest {
    @NotBlank(message = "Full name is required")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    private String fullName;

    @NotBlank(message = "Cellphone is required")
    private String cellphone;

    private String docType;

    @NotBlank(message = "Document number is required")
    private String docNumber;

    private String address;
    private String country;
    private String city;
}
