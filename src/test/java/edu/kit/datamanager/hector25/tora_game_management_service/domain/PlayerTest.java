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
// * Unit tests for Player domain class.
// * Tests all constructors, getters, and domain logic.
// */
//class PlayerTest {
//
//    private UUID testPlayerId;
//    private Player testPlayer;
//
//    @BeforeEach
//    void setUp() {
//        testPlayerId = UUID.randomUUID();
//        testPlayer = new Player("John", "Doe", "TEST");
//    }
//
//    // ==================== CONSTRUCTOR TESTS ====================
//
//    /**
//     * Tests the no-argument constructor.
//     * Verifies that a Player object is created with default values.
//     */
//    @Test
//    void testConstructor_WithFirstAndLastName() {
//        Player player = new Player("Jane", "Smith", "TEST");
//        assertNotNull(player);
//        assertEquals("Jane", player.getFirstName());
//        assertEquals("Smith", player.getLastName());
//        assertEquals("TEST", player.getUserName());
//    }
//
//    /**
//     * Tests the constructor with id, first name, and last name.
//     * Verifies that all attributes are set correctly.
//     */
//    @Test
//    void testConstructor_WithIdAndNames() {
//        Player player = new Player(testPlayerId, "John", "Doe", "TEST");
//        assertEquals(testPlayerId, player.getId());
//        assertEquals("John", player.getFirstName());
//        assertEquals("Doe", player.getLastName());
//        assertEquals("TEST", player.getUserName());
//    }
//
//    // ==================== GETTER/SETTER TESTS ====================
//
//    /**
//     * Tests getFirstName method.
//     * Verifies that the first name is retrieved correctly.
//     */
//    @Test
//    void testGetFirstName() {
//        assertEquals("John", testPlayer.getFirstName());
//    }
//
//    /**
//     * Tests setFirstName method.
//     * Verifies that the first name is set correctly.
//     */
//    @Test
//    void testSetFirstName() {
//        testPlayer.setFirstName("Jane");
//        assertEquals("Jane", testPlayer.getFirstName());
//    }
//
//    /**
//     * Tests getLastName method.
//     * Verifies that the last name is retrieved correctly.
//     */
//    @Test
//    void testGetLastName() {
//        assertEquals("Doe", testPlayer.getLastName());
//    }
//
//    /**
//     * Tests setLastName method.
//     * Verifies that the last name is set correctly.
//     */
//    @Test
//    void testSetLastName() {
//        testPlayer.setLastName("Smith");
//        assertEquals("Smith", testPlayer.getLastName());
//    }
//
//    /**
//     * Tests getUserName method.
//     * Verifies that the user name is retrieved correctly.
//     */
//    @Test
//    void testGetUserName() {
//        assertEquals("TEST", testPlayer.getUserName());
//    }
//
//    /**
//     * Tests setLastName method.
//     * Verifies that the last name is set correctly.
//     */
//    @Test
//    void testSetUserName() {
//        testPlayer.setLastName("Smith");
//        assertEquals("Smith", testPlayer.getUserName());
//    }
//
//    /**
//     * Tests getSessionId method.
//     * Verifies that the UUID identifier is retrieved correctly.
//     */
//    @Test
//    void testGetId() {
//        // ID is null after calling the constructor, as it is generated when stored in DB
//        Player unstoredPlayer = new Player("John", "Doe", "TEST");
//        assertNull(unstoredPlayer.getId());
//        // ID can be set via constructor
//        Player identifiedPlayer = new Player(testPlayerId, "John", "Doe", "TEST");
//        assertEquals(testPlayerId, identifiedPlayer.getId());
//    }
//
//    // ==================== EQUALITY TESTS ====================
//
//    /**
//     * Tests equality with the same player.
//     * Verifies that two players with the same attributes are equal.
//     */
//    @Test
//    void testEquals_SamePlayer() {
//        Player player1 = new Player(testPlayerId, "John", "Doe", "TEST");
//        Player player2 = new Player(testPlayerId, "John", "Doe", "TEST");
//        assertEquals(player1, player2);
//    }
//
//    /**
//     * Tests equality with different players.
//     * Verifies that players with different IDs are not equal.
//     */
//    @Test
//    void testEquals_DifferentPlayers() {
//        Player player1 = new Player(testPlayerId, "John", "Doe", "new ArrayList<>()");
//        Player player2 = new Player(UUID.randomUUID(), "Jane", "Smith", "new ArrayList<>()");
//        assertNotEquals(player1, player2);
//    }
//
//    /**
//     * Tests equality with different first names.
//     * Verifies that players with different first names are not equal.
//     */
//    @Test
//    void testEquals_DifferentFirstName() {
//        Player player1 = new Player(testPlayerId, "John", "Doe", "new ArrayList<>()");
//        Player player2 = new Player(testPlayerId, "Jane", "Doe", "new ArrayList<>()");
//        assertNotEquals(player1, player2);
//    }
//
//    /**
//     * Tests equality with different last names.
//     * Verifies that players with the same first name but different last names are not equal.
//     */
//    @Test
//    void testEquals_DifferentLastName() {
//        Player player1 = new Player(testPlayerId, "John", "Doe", "new ArrayList<>()");
//        Player player2 = new Player(testPlayerId, "John", "Smith", "new ArrayList<>()");
//        assertNotEquals(player1, player2);
//    }
//
//    /**
//     * Tests equality with different last names.
//     * Verifies that players with the same first name but different last names are not equal.
//     */
//    @Test
//    void testEquals_DifferentUserName() {
//        Player player1 = new Player(testPlayerId, "John", "Doe", "ArrayList<>()");
//        Player player2 = new Player(testPlayerId, "John", "Smith", "new ArrayList<>()");
//        assertNotEquals(player1, player2);
//    }
//
//    /**
//     * Tests equality with null.
//     * Verifies that a Player is not equal to null.
//     */
//    @Test
//    void testEquals_WithNull() {
//        assertNotEquals(null, testPlayer);
//    }
//
//    /**
//     * Tests equality with an object of a different class.
//     * Verifies that a Player is not equal to an object of another type.
//     */
//    @Test
//    void testEquals_WithDifferentClass() {
//        assertNotEquals("Not a player", testPlayer);
//    }
//
//
//    // ==================== EDGE CASES ====================
//
//    /**
//     * Tests creating a player with special characters in names.
//     * Verifies that names with accents and punctuation are handled correctly.
//     */
//    @Test
//    void testPlayer_WithSpecialCharactersInNames() {
//        Player player = new Player("José", "O'Neill", "@:)))§$%&/()=?");
//        assertEquals("José", player.getFirstName());
//        assertEquals("O'Neill", player.getLastName());
//        String fullName = player.getName();
//        assertEquals("José O'Neill", fullName);
//    }
//
//    /**
//     * Tests creating a player with very long names.
//     * Verifies that long strings are handled without issues.
//     */
//    @Test
//    void testPlayer_WithLongNames() {
//        String longFirstName = "A".repeat(100);
//        String longLastName = "B".repeat(100);
//        Player player = new Player(longFirstName, longLastName, longFirstName);
//        assertEquals(longFirstName, player.getFirstName());
//        assertEquals(longLastName, player.getLastName());
//    }
//
//    /**
//     * Tests that player IDs are unique.
//     * Verifies that different players have different UUID identifiers.
//     */
//    @Test
//    void testPlayer_IdIsUnique() {
//        UUID id1 = UUID.randomUUID();
//        UUID id2 = UUID.randomUUID();
//        Player player1 = new Player(id1, "John", "Doe", "new ArrayList<>()");
//        Player player2 = new Player(id2, "Jane", "Smith", "new ArrayList<>()");
//        assertNotEquals(player1.getId(), player2.getId());
//    }
//
//}

