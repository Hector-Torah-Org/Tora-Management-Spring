///*
// * Copyright (c) 2025 Karlsruhe Institute of Technology.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package edu.kit.datamanager.hector25.tora_game_management_service.exceptions;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for EntityNotFoundException.
// * Tests the base exception class for entity not found scenarios.
// */
//class EntityNotFoundExceptionTest {
//
//    // ==================== CONSTRUCTOR TESTS ====================
//
//    /**
//     * Tests the constructor with a message parameter.
//     * Verifies that the exception is created with the correct message and no cause.
//     */
//    @Test
//    void testConstructorWithMessage() {
//        String message = "Entity not found";
//        EntityNotFoundException exception = new EntityNotFoundException(message);
//
//        assertNotNull(exception);
//        assertEquals(message, exception.getMessage());
//        assertNull(exception.getCause());
//    }
//
//    /**
//     * Tests the constructor with message and cause parameters.
//     * Verifies that both message and cause are properly set.
//     */
//    @Test
//    void testConstructorWithMessageAndCause() {
//        String message = "Entity not found";
//        Throwable cause = new RuntimeException("Database error");
//        EntityNotFoundException exception = new EntityNotFoundException(message, cause);
//
//        assertNotNull(exception);
//        assertEquals(message, exception.getMessage());
//        assertEquals(cause, exception.getCause());
//    }
//
//    // ==================== EXCEPTION BEHAVIOR TESTS ====================
//
//    @Test
//    void testExceptionIsRuntimeException() {
//        EntityNotFoundException exception = new EntityNotFoundException("Test");
//        assertInstanceOf(RuntimeException.class, exception);
//    }
//
//    @Test
//    void testExceptionCanBeCaught() {
//        assertThrows(EntityNotFoundException.class, () -> {
//            throw new EntityNotFoundException("Test message");
//        });
//    }
//
//    @Test
//    void testExceptionCanBeCaughtAsRuntimeException() {
//        assertThrows(RuntimeException.class, () -> {
//            throw new EntityNotFoundException("Test message");
//        });
//    }
//
//    // ==================== MESSAGE TESTS ====================
//
//    @Test
//    void testMessageWithUUID() {
//        String uuid = "550e8400-e29b-41d4-a716-446655440000";
//        String message = "Entity with id " + uuid + " not found";
//        EntityNotFoundException exception = new EntityNotFoundException(message);
//
//        assertTrue(exception.getMessage().contains(uuid));
//        assertTrue(exception.getMessage().contains("not found"));
//    }
//
//    @Test
//    void testEmptyMessage() {
//        EntityNotFoundException exception = new EntityNotFoundException("");
//        assertEquals("", exception.getMessage());
//    }
//
//    @Test
//    void testNullMessage() {
//        EntityNotFoundException exception = new EntityNotFoundException(null);
//        assertNull(exception.getMessage());
//    }
//
//    // ==================== CAUSE TESTS ====================
//
//    @Test
//    void testWithDatabaseException() {
//        RuntimeException cause = new RuntimeException("Connection timeout");
//        EntityNotFoundException exception = new EntityNotFoundException("Entity not found", cause);
//
//        assertNotNull(exception.getCause());
//        assertEquals("Connection timeout", exception.getCause().getMessage());
//    }
//
//    // ==================== SUBCLASS TESTS ====================
//
//    /**
//     * Tests that PlayerNotFoundException inherits from EntityNotFoundException.
//     * Verifies the exception inheritance hierarchy.
//     */
//    @Test
//    void testPlayerNotFoundExceptionInheritsFromEntityNotFoundException() {
//        PlayerNotFoundException exception = new PlayerNotFoundException("Player not found");
//        assertInstanceOf(EntityNotFoundException.class, exception);
//    }
//
//    /**
//     * Tests that GameNotFoundException inherits from EntityNotFoundException.
//     * Verifies the exception inheritance hierarchy.
//     */
//    @Test
//    void testGameNotFoundExceptionInheritsFromEntityNotFoundException() {
//        GameNotFoundException exception = new GameNotFoundException("Game not found");
//        assertInstanceOf(EntityNotFoundException.class, exception);
//    }
//
//    @Test
//    void testCanCatchAllEntityNotFoundExceptionsPolymorphically() {
//        // Test that PlayerNotFoundException can be caught as EntityNotFoundException
//        assertThrows(EntityNotFoundException.class, () -> {
//            throw new PlayerNotFoundException("Player not found");
//        });
//
//        // Test that GameNotFoundException can be caught as EntityNotFoundException
//        assertThrows(EntityNotFoundException.class, () -> {
//            throw new GameNotFoundException("Game not found");
//        });
//    }
//
//    // ==================== STACK TRACE TESTS ====================
//
//    @Test
//    void testStackTrace() {
//        EntityNotFoundException exception = new EntityNotFoundException("Test message");
//        assertNotNull(exception.getStackTrace());
//        assertTrue(exception.getStackTrace().length > 0);
//    }
//
//    // ==================== PRACTICAL USE CASE TESTS ====================
//    @Test
//    void testThrowInGenericMethod() {
//        String entityId = "550e8400-e29b-41d4-a716-446655440000";
//
//        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
//            throw new EntityNotFoundException("Entity with id " + entityId + " not found");
//        });
//
//        assertTrue(exception.getMessage().contains(entityId));
//    }
//}
//
//