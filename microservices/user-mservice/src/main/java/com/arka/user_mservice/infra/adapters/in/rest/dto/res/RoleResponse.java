package com.arka.user_mservice.infra.adapters.in.rest.dto.res;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class RoleResponse {
    private UUID id;
    private String name;
}
