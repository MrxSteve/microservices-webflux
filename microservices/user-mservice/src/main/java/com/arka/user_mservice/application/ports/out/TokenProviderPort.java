package com.arka.user_mservice.application.ports.out;

import java.util.UUID;

public interface TokenProviderPort {
    String generateAccessToken(UUID userId, String username);
    String generateRefreshToken(UUID userId);
}
