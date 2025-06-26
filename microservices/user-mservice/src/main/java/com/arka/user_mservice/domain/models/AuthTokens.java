package com.arka.user_mservice.domain.models;

import lombok.*;

@AllArgsConstructor
@Getter @Setter @Builder
public class AuthTokens {
    private final String accessToken;
    private final String refreshToken;
    private final String sessionId;
}
