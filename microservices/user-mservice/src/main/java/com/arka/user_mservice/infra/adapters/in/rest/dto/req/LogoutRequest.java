package com.arka.user_mservice.infra.adapters.in.rest.dto.req;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class LogoutRequest {
    private String refreshToken;
    private String sessionId;
}
