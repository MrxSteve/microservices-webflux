package com.arka.user_mservice.infra.adapters.in.rest.dto.res;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class UserResponse {
    private UUID id;
    private String username;
    private String email;
    private boolean enabled;
    private LocalDateTime createdAt;

    private UserProfileResponse profile;
    private List<RoleResponse> roles;
}
