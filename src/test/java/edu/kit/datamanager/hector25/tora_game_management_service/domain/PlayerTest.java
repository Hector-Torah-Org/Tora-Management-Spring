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

    @Test
    void testConstructor_WithFirstAndLastName() {
        Player player = new Player("Jane", "Smith");
        assertNotNull(player);
        assertEquals("Jane", player.getFirstName());
        assertEquals("Smith", player.getLastName());
        // ID is null for non-persisted entities
        assertNull(player.getId());
    }

    @Test
    void testConstructor_WithIdAndNames() {
        Player player = new Player(testPlayerId, "John", "Doe");
        assertEquals(testPlayerId, player.getId());
        assertEquals("John", player.getFirstName());
        assertEquals("Doe", player.getLastName());
    }

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

    @Test
    void testGetFirstName() {
        assertEquals("John", testPlayer.getFirstName());
    }

    @Test
    void testSetFirstName() {
        testPlayer.setFirstName("Jane");
        assertEquals("Jane", testPlayer.getFirstName());
    }

    @Test
    void testGetLastName() {
        assertEquals("Doe", testPlayer.getLastName());
    }

    @Test
    void testSetLastName() {
        testPlayer.setLastName("Smith");
        assertEquals("Smith", testPlayer.getLastName());
    }

    @Test
    void testGetId() {
        // ID is null for non-persisted entities
        assertNull(testPlayer.getId());
    }

    @Test
    void testGetGames() {
        assertNotNull(testPlayer.getGames());
        assertTrue(testPlayer.getGames().isEmpty());
    }

    @Test
    void testSetGames() {
        List<Game> games = new ArrayList<>();
        testPlayer.setGames(games);
        assertEquals(games, testPlayer.getGames());
    }

    // ==================== BUSINESS LOGIC TESTS ====================

    @Test
    void testGetName_CombinesFirstAndLastName() {
        String fullName = testPlayer.getName();
        assertEquals("John Doe", fullName);
    }

    @Test
    void testGetName_WithDifferentNames() {
        Player player = new Player("Jane", "Smith");
        String fullName = player.getName();
        assertEquals("Jane Smith", fullName);
    }

    @Test
    void testGetName_WithSingleCharNames() {
        Player player = new Player("A", "B");
        String fullName = player.getName();
        assertEquals("A B", fullName);
    }

    // ==================== EQUALITY TESTS ====================

    @Test
    void testEquals_SamePlayer() {
        Player player1 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        assertEquals(player1, player2);
    }

    @Test
    void testEquals_DifferentPlayers() {
        Player player1 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(UUID.randomUUID(), "Jane", "Smith", new ArrayList<>());
        assertNotEquals(player1, player2);
    }

    @Test
    void testEquals_DifferentFirstName() {
        Player player1 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(testPlayerId, "Jane", "Doe", new ArrayList<>());
        assertNotEquals(player1, player2);
    }

    @Test
    void testEquals_DifferentLastName() {
        Player player1 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(testPlayerId, "John", "Smith", new ArrayList<>());
        assertNotEquals(player1, player2);
    }

    @Test
    void testEquals_WithNull() {
        assertNotEquals(null, testPlayer);
    }

    @Test
    void testEquals_WithDifferentClass() {
        assertNotEquals("Not a player", testPlayer);
    }

    // ==================== HASHCODE TESTS ====================

    @Test
    void testHashCode_SamePlayers() {
        Player player1 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        assertEquals(player1.hashCode(), player2.hashCode());
    }

    @Test
    void testHashCode_ConsistentForSamePlayer() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        int hash1 = player.hashCode();
        int hash2 = player.hashCode();
        assertEquals(hash1, hash2);
    }

    // ==================== TOSTRING TESTS ====================

    @Test
    void testToString_ContainsPlayerInfo() {
        String result = testPlayer.toString();
        assertNotNull(result);
        assertTrue(result.contains("Player"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }

    @Test
    void testToString_ContainsId() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        String result = player.toString();
        assertTrue(result.contains(testPlayerId.toString()));
    }

    // ==================== EDGE CASES ====================

    @Test
    void testPlayer_WithEmptyGames() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        assertNotNull(player.getGames());
        assertTrue(player.getGames().isEmpty());
    }

    @Test
    void testPlayer_WithSpecialCharactersInNames() {
        Player player = new Player("José", "O'Neill");
        assertEquals("José", player.getFirstName());
        assertEquals("O'Neill", player.getLastName());
        String fullName = player.getName();
        assertEquals("José O'Neill", fullName);
    }

    @Test
    void testPlayer_WithLongNames() {
        String longFirstName = "A".repeat(100);
        String longLastName = "B".repeat(100);
        Player player = new Player(longFirstName, longLastName);
        assertEquals(longFirstName, player.getFirstName());
        assertEquals(longLastName, player.getLastName());
    }

    @Test
    void testPlayer_IdIsUnique() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Player player1 = new Player(id1, "John", "Doe", new ArrayList<>());
        Player player2 = new Player(id2, "Jane", "Smith", new ArrayList<>());
        assertNotEquals(player1.getId(), player2.getId());
    }

    // ==================== ADD/REMOVE GAME TESTS ====================

    @Test
    void testAddGame() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        UUID gameId = UUID.randomUUID();
        Game game = new Game(gameId, new ArrayList<>());

        player.addGame(game);

        assertEquals(1, player.getGames().size());
        assertTrue(player.getGames().contains(game));
    }

    @Test
    void testAddMultipleGames() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        UUID gameId1 = UUID.randomUUID();
        UUID gameId2 = UUID.randomUUID();
        Game game1 = new Game(gameId1, new ArrayList<>());
        Game game2 = new Game(gameId2, new ArrayList<>());

        player.addGame(game1);
        player.addGame(game2);

        assertEquals(2, player.getGames().size());
        assertTrue(player.getGames().contains(game1));
        assertTrue(player.getGames().contains(game2));
    }

    @Test
    void testRemoveGame() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        UUID gameId = UUID.randomUUID();
        Game game = new Game(gameId, new ArrayList<>());

        player.addGame(game);
        assertEquals(1, player.getGames().size());

        player.removeGame(game);
        assertEquals(0, player.getGames().size());
        assertFalse(player.getGames().contains(game));
    }

    @Test
    void testRemoveGame_NotInList() {
        Player player = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        UUID gameId1 = UUID.randomUUID();
        UUID gameId2 = UUID.randomUUID();
        Game game1 = new Game(gameId1, new ArrayList<>());
        Game game2 = new Game(gameId2, new ArrayList<>());

        player.addGame(game1);
        player.removeGame(game2);

        assertEquals(1, player.getGames().size());
        assertTrue(player.getGames().contains(game1));
    }

}

