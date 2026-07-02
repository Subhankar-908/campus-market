package com.ceog.campus_marketplace.Exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ApiErrorResponse {
    private int status;
    private String error;
    private String message;
    private Map<String, String> fieldErrors;   // only populated for validation errors
    private LocalDateTime timestamp;
}