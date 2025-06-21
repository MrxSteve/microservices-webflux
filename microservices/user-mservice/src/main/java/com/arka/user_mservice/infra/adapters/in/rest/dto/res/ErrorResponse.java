package com.arka.user_mservice.infra.adapters.in.rest.dto.res;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;
}