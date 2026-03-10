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
//package edu.kit.datamanager.hector25.tora_game_management_service.domain;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for Game domain class.
// * Tests all constructors, getters, setters, and domain logic.
// */
//class GameTest {
//
//    private UUID testSessionId;
//    private UUID testPlayerId;
//    private Session testSession;
//    private Player testPlayer;
//    private String testGameName;
//
//    @BeforeEach
//    void setUp() {
//        testSessionId = UUID.randomUUID();
//        testPlayerId = UUID.randomUUID();
//        testGameName = "Test Game";
//        testPlayer = new Player(testPlayerId, "John", "Doe", "JoDo");
//        testSession = new Session(testSessionId, testPlayer);
//    }
//
//    // ==================== CONSTRUCTOR TESTS ====================
//
//    /**
//     * Tests the no-argument constructor.
//     * Verifies that a Game object is created with default values.
//     */
//    @Test
//    void testConstructor_NoArgs() {
//        Session session = new Session();
//        assertNotNull(session);
//        assertNull(session.getSessionId());
//        assertNotNull(session.getPlayer());
//    }
//
//    /**
//     * Tests the constructor with id and name parameters.
//     * Verifies that a Game object is created with the specified id and name.
//     */
//    @Test
//    void testConstructor_WithIdAndPlayers() {
//        List<Player> players = new ArrayList<>();
//        players.add(testPlayer);
//        Session game = new Session(testSessionId, testPlayer);
//
//        assertEquals(testSessionId, game.getSessionId());
//        assertEquals(players, game.getPlayer());
//    }
//
//    // ==================== GETTER/SETTER TESTS ====================
//
//    /**
//     * Tests the getter and setter methods for Game class.
//     * Verifies that the properties can be set and retrieved correctly.
//     */
//    @Test
//    void testGetId() {
//        assertEquals(testSessionId, testSession.getSessionId());
//    }
//
//    /**
//     * Tests getting the players list from the Game.
//     * Verifies that the list is initialized correctly.
//     */
//    @Test
//    void testGetPlayers() {
//        assertNotNull(testSession.getPlayer());
//    }
//
//
//    // ==================== EQUALS TESTS ====================
//
//    /**
//     * Tests equality of the same Game object.
//     * Verifies that a Game is equal to itself.
//     */
//    @Test
//    void testEquals_SameGame() {
//        assertEquals(testSession, testSession);
//    }
//
//    /**
//     * Tests equality of two Game objects with the same properties.
//     * Verifies that two Games with identical id, name, and players are equal.
//     */
//    @Test
//    void testEquals_EqualGames() {
//        Session game1 = new Session(testSessionId, testPlayer);
//        Session game2 = new Session(testSessionId, testPlayer);
//        assertEquals(game1, game2);
//    }
//
//    /**
//     * Tests inequality of two Game objects with different IDs.
//     * Verifies that Games with different ids are not equal.
//     */
//    @Test
//    void testEquals_DifferentId() {
//        Session game1 = new Session(testSessionId, testPlayer);
//        Session game2 = new Session(UUID.randomUUID(), testPlayer);
//        assertNotEquals(game1, game2);
//    }
//
//    /**
//     * Tests inequality of two Game objects with different players.
//     * Verifies that Games with different players lists are not equal.
//     */
//    @Test
//    void testEquals_DifferentPlayers() {
//        List<Player> players1 = new ArrayList<>();
//        Player testPlayer2 = new Player(" ", " ", "test");
//
//
//        Session game1 = new Session(testSessionId, testPlayer);
//        Session game2 = new Session(testSessionId, testPlayer2);
//        assertNotEquals(game1, game2);
//    }
//
//    /**
//     * Tests equality with null.
//     * Verifies that a Game is not equal to null.
//     */
//    @Test
//    void testEquals_WithNull() {
//        assertNotEquals(null, testSession);
//    }
//
//    /**
//     * Tests equality with an object of a different class.
//     * Verifies that a Game is not equal to an object of another type.
//     */
//    @Test
//    void testEquals_WithDifferentClass() {
//        assertNotEquals("NotAGame", testSession);
//    }
//
//
//    // ==================== TOSTRING TESTS ====================
//
//    /**
//     * Tests the toString method with an empty players list.
//     * Verifies that the string representation contains game information.
//     */
//    @Test
//    void testToString_EmptyPlayers() {
//        String result = testSession.toString();
//        assertNotNull(result);
//        assertTrue(result.contains("Session{"));
//        assertTrue(result.contains("id=" + testSessionId));
//    }
//
//    /**
//     * Tests the toString method with players in the list.
//     * Verifies that the string representation includes game and player information.
//     */
//    @Test
//    void testToString_WithPlayers() {
//        String result = testSession.toString();
//
//        assertNotNull(result);
//        assertTrue(result.contains("Session{"));
//        assertTrue(result.contains("id=" + testSessionId));
//        assertTrue(result.contains("player="));
//    }
//
//}

