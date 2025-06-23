package com.arka.user_mservice.infra.adapters.in.rest.controller;

import com.arka.user_mservice.application.ports.in.IUserRoleUseCases;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.RoleResponse;
import com.arka.user_mservice.infra.adapters.in.rest.mapper.RoleDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final IUserRoleUseCases iUserRoleUseCases;
    private final RoleDtoMapper roleDtoMapper;

    @PostMapping("/assign")
    public Mono<ResponseEntity<Void>> assignRoleToUser(@RequestParam UUID userId, @RequestParam UUID roleId) {
        return iUserRoleUseCases.assignRole(userId, roleId)
                .thenReturn(ResponseEntity.ok().build());
    }

    @DeleteMapping("/remove")
    public Mono<ResponseEntity<Void>> removeRoleFromUser(@RequestParam UUID userId, @RequestParam UUID roleId) {
        return iUserRoleUseCases.removeRole(userId, roleId)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @GetMapping("/{userId}/roles")
    public Flux<RoleResponse> getRolesByUser(@PathVariable UUID userId) {
        return iUserRoleUseCases.getRolesByUser(userId)
                .map(roleDtoMapper::toResponse);
    }
}