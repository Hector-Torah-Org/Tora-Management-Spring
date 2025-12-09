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
 * Unit tests for PlayerNotFoundException.
 * Tests exception creation and behavior.
 */
class PlayerNotFoundExceptionTest {

    // ==================== CONSTRUCTOR TESTS ====================

    /**
     * Tests the constructor with a message parameter.
     * Verifies that the exception is created with the correct message and no cause.
     */
    @Test
    void testConstructorWithMessage() {
        String message = "Player not found";
        PlayerNotFoundException exception = new PlayerNotFoundException(message);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    /**
     * Tests the constructor with message and cause parameters.
     * Verifies that both message and cause are properly set.
     */
    @Test
    void testConstructorWithMessageAndCause() {
        String message = "Player not found";
        Throwable cause = new RuntimeException("Database error");
        PlayerNotFoundException exception = new PlayerNotFoundException(message, cause);

        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    // ==================== EXCEPTION BEHAVIOR TESTS ====================

    /**
     * Tests that PlayerNotFoundException is a RuntimeException.
     * Verifies the exception hierarchy.
     */
    @Test
    void testExceptionIsRuntimeException() {
        PlayerNotFoundException exception = new PlayerNotFoundException("Test");
        assertInstanceOf(RuntimeException.class, exception);
    }

    /**
     * Tests that the exception can be caught as PlayerNotFoundException.
     * Verifies proper exception throwing behavior.
     */
    @Test
    void testExceptionCanBeCaught() {
        assertThrows(PlayerNotFoundException.class, () -> {
            throw new PlayerNotFoundException("Test message");
        });
    }

    /**
     * Tests that the exception can be caught as RuntimeException.
     * Verifies it can be handled as a more general exception type.
     */
    @Test
    void testExceptionCanBeCaughtAsRuntimeException() {
        assertThrows(RuntimeException.class, () -> {
            throw new PlayerNotFoundException("Test message");
        });
    }

    /**
     * Tests that the exception can be caught as Exception.
     * Verifies it can be handled as the base exception type.
     */
    @Test
    void testExceptionCanBeCaughtAsException() {
        assertThrows(Exception.class, () -> {
            throw new PlayerNotFoundException("Test message");
        });
    }

    // ==================== MESSAGE TESTS ====================

    /**
     * Tests exception message containing a UUID.
     * Verifies that UUIDs are properly included in error messages.
     */
    @Test
    void testMessageWithUUID() {
        String uuid = "550e8400-e29b-41d4-a716-446655440000";
        String message = "Player with id " + uuid + " not found";
        PlayerNotFoundException exception = new PlayerNotFoundException(message);

        assertTrue(exception.getMessage().contains(uuid));
        assertTrue(exception.getMessage().contains("not found"));
    }

    /**
     * Tests exception with an empty message.
     * Verifies that empty strings are handled correctly.
     */
    @Test
    void testEmptyMessage() {
        PlayerNotFoundException exception = new PlayerNotFoundException("");
        assertEquals("", exception.getMessage());
    }

    /**
     * Tests exception with a very long message.
     * Verifies that long error messages are handled correctly.
     */
    @Test
    void testLongMessage() {
        String longMessage = "A".repeat(1000);
        PlayerNotFoundException exception = new PlayerNotFoundException(longMessage);
        assertEquals(longMessage, exception.getMessage());
    }

    @Test
    void testSpecialCharactersInMessage() {
        String message = "Player with id 123!@#$%^&*() not found";
        PlayerNotFoundException exception = new PlayerNotFoundException(message);
        assertEquals(message, exception.getMessage());
    }

    // ==================== CAUSE TESTS ====================

    @Test
    void testExceptionWithCause() {
        RuntimeException cause = new RuntimeException("Original error");
        PlayerNotFoundException exception = new PlayerNotFoundException("Player not found", cause);

        assertSame(cause, exception.getCause());
        assertEquals("Original error", exception.getCause().getMessage());
    }

    @Test
    void testExceptionWithNullCause() {
        PlayerNotFoundException exception = new PlayerNotFoundException("Player not found", null);
        assertNull(exception.getCause());
    }

    @Test
    void testExceptionWithChainedCauses() {
        RuntimeException rootCause = new RuntimeException("Root cause");
        RuntimeException middleCause = new RuntimeException("Middle cause", rootCause);
        PlayerNotFoundException exception = new PlayerNotFoundException("Player not found", middleCause);

        assertEquals(middleCause, exception.getCause());
        assertEquals(rootCause, exception.getCause().getCause());
    }

    // ==================== STACKTRACE TESTS ====================

    @Test
    void testStackTraceAvailable() {
        PlayerNotFoundException exception = new PlayerNotFoundException("Test");
        StackTraceElement[] stackTrace = exception.getStackTrace();
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    @Test
    void testExceptionToString() {
        String message = "Player not found";
        PlayerNotFoundException exception = new PlayerNotFoundException(message);
        String result = exception.toString();

        assertNotNull(result);
        assertTrue(result.contains("PlayerNotFoundException"));
        assertTrue(result.contains(message));
    }

    // ==================== MULTIPLE EXCEPTIONS ====================

    @Test
    void testMultipleInstancesAreIndependent() {
        PlayerNotFoundException exception1 = new PlayerNotFoundException("Error 1");
        PlayerNotFoundException exception2 = new PlayerNotFoundException("Error 2");

        assertEquals("Error 1", exception1.getMessage());
        assertEquals("Error 2", exception2.getMessage());
        assertNotEquals(exception1.getMessage(), exception2.getMessage());
    }

    @Test
    void testExceptionInTryCatch() {
        try {
            throw new PlayerNotFoundException("Test error");
        } catch (PlayerNotFoundException e) {
            assertEquals("Test error", e.getMessage());
        }
    }

    // ==================== EXCEPTION PROPAGATION ====================

    @Test
    void testExceptionPropagationThroughThrows() {
        assertThrows(PlayerNotFoundException.class, () -> methodThatThrowsException());
    }

    private void methodThatThrowsException() throws PlayerNotFoundException {
        throw new PlayerNotFoundException("Exception from method");
    }

    @Test
    void testExceptionMessagePreservedPropagation() {
        String originalMessage = "Original message";
        PlayerNotFoundException exception = assertThrows(PlayerNotFoundException.class, () -> {
            throw new PlayerNotFoundException(originalMessage);
        });

        assertEquals(originalMessage, exception.getMessage());
    }

    // ==================== SERIALIZATION TESTS ====================

    @Test
    void testExceptionIsSerializable() {
        PlayerNotFoundException exception = new PlayerNotFoundException("Test message");
        assertNotNull(exception);
        // Note: Full serialization testing would require ObjectOutputStream/ObjectInputStream
    }
}

