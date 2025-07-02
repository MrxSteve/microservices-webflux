package com.arka.user_mservice.application.ports.out;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TokenProviderPort {
    String generateAccessToken(UUID userId, String username, List<String> roles);
    List<String> getRolesFromToken(String token);
    String generateRefreshToken(UUID userId, String sessionId);
    String getSessionIdFromToken(String token);
    UUID getUserIdFromToken(String token);
    boolean validateToken(String token);
    Date getExpiration(String token);
}
