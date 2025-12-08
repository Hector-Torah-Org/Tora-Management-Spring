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

package edu.kit.datamanager.hector25.tora_game_management_service.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameNotFoundException.
 * Tests exception creation and behavior.
 */
class GameNotFoundExceptionTest {

    // ==================== CONSTRUCTOR TESTS ====================

    @Test
    void testConstructorWithMessage() {
        String message = "Game not found";
        GameNotFoundException exception = new GameNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Game not found";
        Throwable cause = new RuntimeException("Database error");
        GameNotFoundException exception = new GameNotFoundException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    // ==================== EXCEPTION BEHAVIOR TESTS ====================

    @Test
    void testExceptionIsRuntimeException() {
        GameNotFoundException exception = new GameNotFoundException("Test");
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void testExceptionCanBeCaught() {
        assertThrows(GameNotFoundException.class, () -> {
            throw new GameNotFoundException("Test message");
        });
    }

    @Test
    void testExceptionCanBeCaughtAsRuntimeException() {
        assertThrows(RuntimeException.class, () -> {
            throw new GameNotFoundException("Test message");
        });
    }

    @Test
    void testExceptionCanBeCaughtAsException() {
        assertThrows(Exception.class, () -> {
            throw new GameNotFoundException("Test message");
        });
    }

    // ==================== MESSAGE TESTS ====================

    @Test
    void testMessageWithUUID() {
        String uuid = "550e8400-e29b-41d4-a716-446655440000";
        String message = "Game with id " + uuid + " not found";
        GameNotFoundException exception = new GameNotFoundException(message);

        assertTrue(exception.getMessage().contains(uuid));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testEmptyMessage() {
        GameNotFoundException exception = new GameNotFoundException("");
        assertEquals("", exception.getMessage());
    }

    @Test
    void testLongMessage() {
        String longMessage = "A".repeat(1000);
        GameNotFoundException exception = new GameNotFoundException(longMessage);
        assertEquals(longMessage, exception.getMessage());
    }

    @Test
    void testNullMessage() {
        GameNotFoundException exception = new GameNotFoundException(null);
        assertNull(exception.getMessage());
    }

    // ==================== CAUSE TESTS ====================

    @Test
    void testWithDatabaseException() {
        RuntimeException cause = new RuntimeException("Connection timeout");
        GameNotFoundException exception = new GameNotFoundException("Game not found", cause);

        assertNotNull(exception.getCause());
        assertEquals("Connection timeout", exception.getCause().getMessage());
    }

    @Test
    void testWithNullPointerException() {
        NullPointerException cause = new NullPointerException("Null reference");
        GameNotFoundException exception = new GameNotFoundException("Game not found", cause);

        assertNotNull(exception.getCause());
        assertInstanceOf(NullPointerException.class, exception.getCause());
    }

    // ==================== INHERITANCE TESTS ====================

    @Test
    void testExtendsEntityNotFoundException() {
        GameNotFoundException exception = new GameNotFoundException("Test");
        assertInstanceOf(EntityNotFoundException.class, exception);
    }

    @Test
    void testStackTrace() {
        GameNotFoundException exception = new GameNotFoundException("Test message");
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    // ==================== PRACTICAL USE CASE TESTS ====================

    @Test
    void testThrowInServiceLayer() {
        String gameId = "550e8400-e29b-41d4-a716-446655440000";

        Exception exception = assertThrows(GameNotFoundException.class, () -> {
            throw new GameNotFoundException("Game with id " + gameId + " not found");
        });

        assertTrue(exception.getMessage().contains(gameId));
    }

    @Test
    void testExceptionMessageFormat() {
        String gameId = "550e8400-e29b-41d4-a716-446655440000";
        GameNotFoundException exception = new GameNotFoundException("Game with id " + gameId + " not found");

        assertEquals("Game with id " + gameId + " not found", exception.getMessage());
    }
}

