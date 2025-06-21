package com.arka.user_mservice.infra.adapters.in.rest.controller;

import com.arka.user_mservice.application.ports.in.IRoleUseCases;
import com.arka.user_mservice.infra.adapters.in.rest.dto.req.RoleRequest;
import com.arka.user_mservice.infra.adapters.in.rest.dto.res.RoleResponse;
import com.arka.user_mservice.infra.adapters.in.rest.mapper.RoleDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Roles", description = "CRUD operations for user roles")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleUseCases iRoleUseCases;
    private final RoleDtoMapper roleDtoMapper;

    @Operation(summary = "Create a new role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public Mono<ResponseEntity<RoleResponse>> create(@Valid @RequestBody RoleRequest request) {
        return iRoleUseCases.create(roleDtoMapper.toModel(request))
                .map(roleDtoMapper::toResponse)
                .map(res -> ResponseEntity.status(HttpStatus.CREATED).body(res));
    }

    @Operation(summary = "Find a role by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role found"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<RoleResponse>> getById(
            @Parameter(description = "Role ID", required = true) @PathVariable UUID id) {
        return iRoleUseCases.findById(id)
                .map(roleDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "List all roles")
    @ApiResponse(responseCode = "200", description = "List of roles")
    @GetMapping
    public Flux<RoleResponse> findAll() {
        return iRoleUseCases.findAll()
                .map(roleDtoMapper::toResponse);
    }

    @Operation(summary = "Delete a role by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(
            @Parameter(description = "Role ID", required = true) @PathVariable UUID id) {
        return iRoleUseCases.deleteById(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Operation(summary = "Find a role by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role found"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping("/name")
    public Mono<ResponseEntity<RoleResponse>> getByName(
            @Parameter(description = "Role name", required = true) @RequestParam String name) {
        return iRoleUseCases.findByName(name)
                .map(roleDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
