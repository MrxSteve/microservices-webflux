package com.arka.user_mservice.infra.adapters.in.rest.dto.res;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String sessionId;
}
