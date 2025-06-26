package com.arka.user_mservice.application.ports.out;

import java.util.Date;
import java.util.UUID;

public interface TokenProviderPort {
    String generateAccessToken(UUID userId, String username);
    String generateRefreshToken(UUID userId, String sessionId);
    String getSessionIdFromToken(String token);
    UUID getUserIdFromToken(String token);
    boolean validateToken(String token);
    Date getExpiration(String token);
}
