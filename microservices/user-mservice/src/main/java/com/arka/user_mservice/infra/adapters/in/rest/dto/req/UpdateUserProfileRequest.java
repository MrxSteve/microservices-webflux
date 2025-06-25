package com.arka.user_mservice.infra.adapters.in.rest.dto.req;

import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class UpdateUserProfileRequest {
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters")
    private String fullName;

    private String cellphone;
    private String docType;
    private String docNumber;

    private String address;
    private String country;
    private String city;
}
