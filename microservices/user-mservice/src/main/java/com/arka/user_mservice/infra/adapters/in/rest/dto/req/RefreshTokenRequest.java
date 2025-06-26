package com.arka.user_mservice.infra.adapters.in.rest.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

    @NotBlank(message = "Session ID is required")
    private String sessionId;
}
