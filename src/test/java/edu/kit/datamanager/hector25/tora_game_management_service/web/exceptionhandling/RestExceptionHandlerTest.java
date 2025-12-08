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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for RestExceptionHandler.
 * Tests exception handling and error response formatting.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // ==================== PLAYER NOT FOUND EXCEPTION TESTS ====================

    @Test
    void testHandlePlayerNotFoundException_Returns404() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/players/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void testHandlePlayerNotFoundException_IncludesErrorMessage() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/players/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    void testHandlePlayerNotFoundException_IncludesTimestamp() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/players/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void testHandlePlayerNotFoundException_IncludesPath() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/players/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").exists())
                .andExpect(jsonPath("$.path", containsString("/players")));
    }

    // ==================== VALIDATION EXCEPTION TESTS ====================

    @Test
    void testHandleValidationException_Returns400() throws Exception {
        String invalidJson = "{\"firstName\":\"\",\"lastName\":\"Doe\"}";

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/players")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testHandleValidationException_IncludesErrorsList() throws Exception {
        String invalidJson = "{\"firstName\":\"\",\"lastName\":\"\"}";

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/players")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))));
    }

    @Test
    void testHandleValidationException_ErrorContainsFieldName() throws Exception {
        String invalidJson = "{\"firstName\":\"\",\"lastName\":\"Doe\"}";

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/players")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(containsString("firstName"))));
    }

    // ==================== ERROR RESPONSE FORMAT TESTS ====================

    @Test
    void testErrorResponse_HasRequiredFields() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/players/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").exists());
    }

    @Test
    void testErrorResponse_StatusMatchesHttpCode() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/players/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // ==================== XSS PREVENTION TESTS ====================

    @Test
    void testErrorResponse_SanitizesMessage() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/players/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.message", not(containsString("<"))))
                .andExpect(jsonPath("$.message", not(containsString(">"))));
    }

    @Test
    void testErrorResponse_SanitizesPath() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/players/{id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path", not(containsString("<"))))
                .andExpect(jsonPath("$.path", not(containsString(">"))));
    }

    // ==================== GLOBAL EXCEPTION HANDLER TESTS ====================

    @Test
    void testHandleGlobalException_DirectCall() {
        RestExceptionHandler handler = new RestExceptionHandler();
        Exception exception = new Exception("Test exception");
        org.springframework.web.context.request.WebRequest request =
                new org.springframework.web.context.request.ServletWebRequest(
                        new org.springframework.mock.web.MockHttpServletRequest()
                );

        org.springframework.http.ResponseEntity<Object> response = handler.handleGlobalException(exception, request);

        assertEquals(500, response.getStatusCode().value());
        assertNotNull(response.getBody());
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> body = (java.util.Map<String, Object>) response.getBody();
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("An unexpected error occurred", body.get("message"));
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    void testSanitizeString_WithNullInput() {
        RestExceptionHandler handler = new RestExceptionHandler();
        // Test through the handler's sanitization by using reflection or testing indirectly
        // Since sanitizeString is private, we test it through the public methods
        Exception exception = new Exception((String) null);
        org.springframework.web.context.request.WebRequest request =
                new org.springframework.web.context.request.ServletWebRequest(
                        new org.springframework.mock.web.MockHttpServletRequest()
                );

        org.springframework.http.ResponseEntity<Object> response = handler.handleGlobalException(exception, request);
        assertNotNull(response.getBody());
    }

    @Test
    void testExtractPath_WithoutUriPrefix() {
        RestExceptionHandler handler = new RestExceptionHandler();
        org.springframework.mock.web.MockHttpServletRequest servletRequest =
                new org.springframework.mock.web.MockHttpServletRequest();
        servletRequest.setRequestURI("/test-path");

        org.springframework.web.context.request.WebRequest request =
                new org.springframework.web.context.request.ServletWebRequest(servletRequest);

        // Test indirectly through handleGlobalException
        Exception exception = new Exception("Test");
        org.springframework.http.ResponseEntity<Object> response = handler.handleGlobalException(exception, request);

        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> body = (java.util.Map<String, Object>) response.getBody();
        assertNotNull(body.get("path"));
    }
}

