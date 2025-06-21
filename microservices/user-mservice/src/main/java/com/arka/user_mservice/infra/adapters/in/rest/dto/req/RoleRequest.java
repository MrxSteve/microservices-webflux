package com.arka.user_mservice.infra.adapters.in.rest.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class RoleRequest {
    @NotBlank(message = "Role name cannot be blank")
    @Size(min = 2, max = 50, message = "Role name must be between 2 and 50 characters")
    private String name;
}
