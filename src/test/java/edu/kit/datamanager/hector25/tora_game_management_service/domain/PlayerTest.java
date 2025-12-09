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

package edu.kit.datamanager.hector25.tora_game_management_service.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Player domain class.
 * Tests all constructors, getters, and domain logic.
 */
class PlayerTest {

    private UUID testPlayerId;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        testPlayerId = UUID.randomUUID();
        testPlayer = new Player("John", "Doe");
    }

    // ==================== CONSTRUCTOR TESTS ====================

    /**
     * Tests the no-argument constructor.
     * Verifies that a Player object is created with default values.
     */
    @Test
    void testConstructor_WithFirstAndLastName() {
        Player player = new Player("Jane", "Smith");
        assertNotNull(player);
        assertEquals("Jane", player.getFirstName());
        assertEquals("Smith", player.getLastName());
    }

    /**
     * Tests the constructor with id, first name, and last name.
     * Verifies that all attributes are set correctly.
     */
    @Test
    void testConstructor_WithIdAndNames() {
        Player player = new Player(testPlayerId, "John", "Doe");
        assertEquals(testPlayerId, player.getId());
        assertEquals("John", player.getFirstName());
        assertEquals("Doe", player.getLastName());
    }

    /**
     * Tests the constructor with id, first name, last name, and games list.
     * Verifies that all attributes are set correctly.
     */
    @Test
    void testConstructor_WithIdNamesAndGames() {
        List<Game> games = new ArrayList<>();
        Player player = new Player(testPlayerId, "John", "Doe", games);
        assertEquals(testPlayerId, player.getId());
        assertEquals("John", player.getFirstName());
        assertEquals("Doe", player.getLastName());
        assertEquals(games, player.getGames());
    }

    // ==================== GETTER/SETTER TESTS ====================

    /**
     * Tests getFirstName method.
     * Verifies that the first name is retrieved correctly.
     */
    @Test
    void testGetFirstName() {
        assertEquals("John", testPlayer.getFirstName());
    }

    /**
     * Tests setFirstName method.
     * Verifies that the first name is set correctly.
     */
    @Test
    void testSetFirstName() {
        testPlayer.setFirstName("Jane");
        assertEquals("Jane", testPlayer.getFirstName());
    }

    /**
     * Tests getLastName method.
     * Verifies that the last name is retrieved correctly.
     */
    @Test
    void testGetLastName() {
        assertEquals("Doe", testPlayer.getLastName());
    }

    /**
     * Tests setLastName method.
     * Verifies that the last name is set correctly.
     */
    @Test
    void testSetLastName() {
        testPlayer.setLastName("Smith");
        assertEquals("Smith", testPlayer.getLastName());
    }

    /**
     * Tests getId method.
     * Verifies that the UUID identifier is retrieved correctly.
     */
    @Test
    void testGetId() {
        // ID is null after calling the constructor, as it is generated when stored in DB
        Player unstoredPlayer = new Player("John", "Doe");
        assertNull(unstoredPlayer.getId());
        // ID can be set via constructor
        Player identifiedPlayer = new Player(testPlayerId, "John", "Doe");
        assertEquals(testPlayerId, identifiedPlayer.getId());
    }

    /**
     * Tests getGames method.
     * Verifies that the games list is retrieved correctly.
     */
    @Test
    void testGetGames() {
        assertNotNull(testPlayer.getGames());
        assertTrue(testPlayer.getGames().isEmpty());
    }

    /**
     * Tests setGames method.
     * Verifies that the games list is set correctly.
     */
    @Test
    void testSetGames() {
        List<Game> games = new ArrayList<>();
        testPlayer.setGames(games);
        assertEquals(games, testPlayer.getGames());
    }

    // ==================== BUSINESS LOGIC TESTS ====================

    /**
     * Tests getName method.
     * Verifies that it combines first and last names correctly.
     */
    @Test
    void testGetName_CombinesFirstAndLastName() {
        String fullName = testPlayer.getName();
        assertEquals("John Doe", fullName);
    }

    /**
     * Tests getName with different first and last names.
     * Verifies that the method correctly combines them.
     */
    @Test
    void testGetName_WithDifferentNames() {
        Player player = new Player("Jane", "Smith");
        String fullName = player.getName();
        assertEquals("Jane Smith", fullName);
    }

    /**
     * Tests getName with single character first and last names.
     * Verifies that the method correctly combines them.
     */
    @Test
    void testGetName_WithSingleCharNames() {
        Player player = new Player("A", "B");
        String fullName = player.getName();
        assertEquals("A B", fullName);
    }

    // ==================== EQUALITY TESTS ====================

    /**
     * Tests equality with the same player.
     * Verifies that two players with the same attributes are equal.
     */
    @Test
    void testEquals_SamePlayer() {
        Player player1 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        assertEquals(player1, player2);
    }

    /**
     * Tests equality with different players.
     * Verifies that players with different IDs are not equal.
     */
    @Test
    void testEquals_DifferentPlayers() {
        Player player1 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(UUID.randomUUID(), "Jane", "Smith", new ArrayList<>());
        assertNotEquals(player1, player2);
    }

    /**
     * Tests equality with different first names.
     * Verifies that players with different first names are not equal.
     */
    @Test
    void testEquals_DifferentFirstName() {
        Player player1 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(testPlayerId, "Jane", "Doe", new ArrayList<>());
        assertNotEquals(player1, player2);
    }

    /**
     * Tests equality with different last names.
     * Verifies that players with the same first name but different last names are not equal.
     */
    @Test
    void testEquals_DifferentLastName() {
        Player player1 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(testPlayerId, "John", "Smith", new ArrayList<>());
        assertNotEquals(player1, player2);
    }

    /**
     * Tests equality with null.
     * Verifies that a Player is not equal to null.
     */
    @Test
    void testEquals_WithNull() {
        assertNotEquals(null, testPlayer);
    }

    /**
     * Tests equality with an object of a different class.
     * Verifies that a Player is not equal to an object of another type.
     */
    @Test
    void testEquals_WithDifferentClass() {
        assertNotEquals("Not a player", testPlayer);
    }

    // ==================== HASHCODE TESTS ====================

    /**
     * Tests that hashCode is the same for equal players.
     * Verifies that two players with the same attributes have the same hash code.
     */
    @Test
    void testHashCode_SamePlayers() {
        Player player1 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        assertEquals(player1.hashCode(), player2.hashCode());
    }

    /**
     * Tests that hashCode is consistent for the same player instance.
     * Verifies that multiple calls to hashCode return the same value.
     */
    @Test
    void testHashCode_ConsistentForSamePlayer() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        int hash1 = player.hashCode();
        int hash2 = player.hashCode();
        assertEquals(hash1, hash2);
    }

    // ==================== TOSTRING TESTS ====================

    /**
     * Tests the toString method includes player information.
     * Verifies that the string representation contains the player's names.
     */
    @Test
    void testToString_ContainsPlayerInfo() {
        String result = testPlayer.toString();
        assertNotNull(result);
        assertTrue(result.contains("Player"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }

    /**
     * Tests the toString method includes the player's ID.
     * Verifies that the UUID is present in the string representation.
     */
    @Test
    void testToString_ContainsId() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        String result = player.toString();
        assertTrue(result.contains(testPlayerId.toString()));
    }

    // ==================== EDGE CASES ====================

    /**
     * Tests creating a player with empty games list.
     * Verifies that the games list is initialized correctly.
     */
    @Test
    void testPlayer_WithEmptyGames() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        assertNotNull(player.getGames());
        assertTrue(player.getGames().isEmpty());
    }

    /**
     * Tests creating a player with special characters in names.
     * Verifies that names with accents and punctuation are handled correctly.
     */
    @Test
    void testPlayer_WithSpecialCharactersInNames() {
        Player player = new Player("José", "O'Neill");
        assertEquals("José", player.getFirstName());
        assertEquals("O'Neill", player.getLastName());
        String fullName = player.getName();
        assertEquals("José O'Neill", fullName);
    }

    /**
     * Tests creating a player with very long names.
     * Verifies that long strings are handled without issues.
     */
    @Test
    void testPlayer_WithLongNames() {
        String longFirstName = "A".repeat(100);
        String longLastName = "B".repeat(100);
        Player player = new Player(longFirstName, longLastName);
        assertEquals(longFirstName, player.getFirstName());
        assertEquals(longLastName, player.getLastName());
    }

    /**
     * Tests that player IDs are unique.
     * Verifies that different players have different UUID identifiers.
     */
    @Test
    void testPlayer_IdIsUnique() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Player player1 = new Player(id1, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(id2, "Jane", "Smith", new ArrayList<>());
        assertNotEquals(player1.getId(), player2.getId());
    }

    // ==================== ADD/REMOVE GAME TESTS ====================

    /**
     * Tests adding a game to a player's game list.
     * Verifies that the game is added successfully and the list size increases.
     */
    @Test
    void testAddGame() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        UUID gameId = UUID.randomUUID();
        Game game = new Game(gameId, "Test Game", new ArrayList<>());

        player.addGame(game);

        assertEquals(1, player.getGames().size());
        assertTrue(player.getGames().contains(game));
    }

    /**
     * Tests adding multiple games to a player's game list.
     * Verifies that multiple games can be added and all are retained.
     */
    @Test
    void testAddMultipleGames() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        UUID gameId1 = UUID.randomUUID();
        UUID gameId2 = UUID.randomUUID();
        Game game1 = new Game(gameId1, "Game 1", new ArrayList<>());
        Game game2 = new Game(gameId2, "Game 2", new ArrayList<>());

        player.addGame(game1);
        player.addGame(game2);

        assertEquals(2, player.getGames().size());
        assertTrue(player.getGames().contains(game1));
        assertTrue(player.getGames().contains(game2));
    }

    /**
     * Tests removing a game from a player's game list.
     * Verifies that the game is removed successfully and the list is empty.
     */
    @Test
    void testRemoveGame() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        UUID gameId = UUID.randomUUID();
        Game game = new Game(gameId, "Test Game", new ArrayList<>());

        player.addGame(game);
        assertEquals(1, player.getGames().size());

        player.removeGame(game);
        assertEquals(0, player.getGames().size());
        assertFalse(player.getGames().contains(game));
    }

    /**
     * Tests removing a game that is not in the player's game list.
     * Verifies that the list remains unchanged when trying to remove a non-existent game.
     */
    @Test
    void testRemoveGame_NotInList() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        UUID gameId1 = UUID.randomUUID();
        UUID gameId2 = UUID.randomUUID();
        Game game1 = new Game(gameId1, "Game 1", new ArrayList<>());
        Game game2 = new Game(gameId2, "Game 2", new ArrayList<>());

        player.addGame(game1);
        player.removeGame(game2);

        assertEquals(1, player.getGames().size());
        assertTrue(player.getGames().contains(game1));
    }

}

