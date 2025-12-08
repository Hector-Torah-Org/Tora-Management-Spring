/*
 * Copyright (c) 2025 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.kit.datamanager.hector25.tora_game_management_service.web.exceptionhandling;

import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Global exception handler for REST controllers.
 * Handles PlayerNotFoundException and other exceptions with appropriate HTTP responses.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    /**
     * Handles validation errors by returning HTTP 400 Bad Request.
     *
     * @param ex      the validation exception thrown
     * @param request the web request
     * @return a ResponseEntity with validation error details and HTTP 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        LOG.warn("Validation error: {}", ex.getMessage());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("message", "Input validation failed");
        body.put("errors", errors);
        body.put("path", extractPath(request));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Extracts and sanitizes the request path from WebRequest.
     *
     * @param request the web request
     * @return sanitized path
     */
    private String extractPath(WebRequest request) {
        String description = request.getDescription(false);
        if (Objects.nonNull(description) && description.startsWith("uri=")) {
            return sanitizeString(description.substring(4));
        }
        return "/";
    }

    /**
     * Sanitizes a string to prevent XSS attacks by removing potentially dangerous characters.
     * This prevents script injection through error messages.
     *
     * @param input the input string
     * @return sanitized string
     */
    private String sanitizeString(String input) {
        if (input == null) {
            return "";
        }
        return input
                .replaceAll("[<>\"'`]", "")
                .trim();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            WebRequest request) {
        LOG.warn("Malformed JSON request: {}", ex.getMessage());

        Map<String, Object> body = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON Request",
                "The request body is not readable or is malformed",
                extractPath(request)
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Creates a consistent error response map.
     *
     * @param status  HTTP status code
     * @param error   error type
     * @param message error message (will be sanitized)
     * @param path    request path (will be sanitized)
     * @return a map containing standardized error information
     */
    private Map<String, Object> createErrorResponse(int status, String error, String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("error", error);
        body.put("message", sanitizeString(message));
        body.put("path", sanitizeString(path));
        return body;
    }

    /**
     * Handles EntityNotFoundException (and subclasses) by returning HTTP 404 Not Found.
     * This handler covers both PlayerNotFoundException and GameNotFoundException.
     *
     * @param ex      the exception thrown
     * @param request the web request
     * @return a ResponseEntity with error details and HTTP 404 status
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex,
            WebRequest request) {
        LOG.warn("EntityNotFoundException: {}", ex.getMessage());

        Map<String, Object> body = createErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                extractPath(request)
        );

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles generic exceptions by returning HTTP 500 Internal Server Error.
     *
     * @param ex      the exception thrown
     * @param request the web request
     * @return a ResponseEntity with error details and HTTP 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(
            Exception ex,
            WebRequest request) {
        LOG.error("Unexpected exception", ex);

        Map<String, Object> body = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred",
                extractPath(request)
        );

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

