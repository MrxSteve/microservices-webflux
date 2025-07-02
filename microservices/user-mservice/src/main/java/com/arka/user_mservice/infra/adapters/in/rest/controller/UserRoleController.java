package com.arka.user_mservice.infra.adapters.in.rest.controller;

import com.arka.user_mservice.application.ports.in.IUserRoleUseCases;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.RoleResponse;
import com.arka.user_mservice.infra.adapters.in.rest.mapper.RoleDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "User Roles", description = "Operations to assign/remove roles to/from users")
@RestController
@RequestMapping("/api/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final IUserRoleUseCases iUserRoleUseCases;
    private final RoleDtoMapper roleDtoMapper;

    @Operation(summary = "Assign a role to a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role assigned to user successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/assign")
    public Mono<ResponseEntity<Void>> assignRoleToUser(
            @Parameter(description = "User ID", required = true) @RequestParam UUID userId,
            @Parameter(description = "Role ID", required = true) @RequestParam UUID roleId) {
        return iUserRoleUseCases.assignRole(userId, roleId)
                .thenReturn(ResponseEntity.ok().build());
    }

    @Operation(summary = "Remove a role from a user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Role removed from user successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @DeleteMapping("/remove")
    public Mono<ResponseEntity<Void>> removeRoleFromUser(
            @Parameter(description = "User ID", required = true) @RequestParam UUID userId,
            @Parameter(description = "Role ID", required = true) @RequestParam UUID roleId) {
        return iUserRoleUseCases.removeRole(userId, roleId)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Get all roles assigned to a user")
    @ApiResponse(responseCode = "200", description = "List of roles for the user")
    @GetMapping("/{userId}/roles")
    public Flux<RoleResponse> getRolesByUser(
            @Parameter(description = "User ID", required = true) @PathVariable UUID userId) {
        return iUserRoleUseCases.getRolesByUser(userId)
                .map(roleDtoMapper::toResponse);
    }
}
