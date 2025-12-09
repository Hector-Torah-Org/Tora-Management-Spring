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
 * Unit tests for Game domain class.
 * Tests all constructors, getters, setters, and domain logic.
 */
class GameTest {

    private UUID testGameId;
    private UUID testPlayerId;
    private Game testGame;
    private Player testPlayer;
    private String testGameName;

    @BeforeEach
    void setUp() {
        testGameId = UUID.randomUUID();
        testPlayerId = UUID.randomUUID();
        testGameName = "Test Game";
        testPlayer = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        testGame = new Game(testGameId, testGameName, new ArrayList<>());
    }

    // ==================== CONSTRUCTOR TESTS ====================

    /**
     * Tests the no-argument constructor.
     * Verifies that a Game object is created with default values.
     */
    @Test
    void testConstructor_NoArgs() {
        Game game = new Game();
        assertNotNull(game);
        assertNull(game.getId());
        assertNotNull(game.getName());
        assertTrue(game.getName().isEmpty());
        assertNotNull(game.getPlayers());
        assertTrue(game.getPlayers().isEmpty());
    }

    /**
     * Tests the constructor with id and name parameters.
     * Verifies that a Game object is created with the specified id and name.
     */
    @Test
    void testConstructor_WithIdAndPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);
        Game game = new Game(testGameId, testGameName, players);

        assertEquals(testGameId, game.getId());
        assertEquals(testGameName, game.getName());
        assertEquals(players, game.getPlayers());
        assertEquals(1, game.getPlayers().size());
    }

    // ==================== GETTER/SETTER TESTS ====================

    /**
     * Tests the getter and setter methods for Game class.
     * Verifies that the properties can be set and retrieved correctly.
     */
    @Test
    void testGetId() {
        assertEquals(testGameId, testGame.getId());
    }

    /**
     * Tests setting a new ID for the Game.
     * Verifies that the ID is updated correctly.
     */
    @Test
    void testGetName() {
        assertEquals(testGameName, testGame.getName());
    }

    /**
     * Tests setting a new name for the Game.
     * Verifies that the name is updated correctly.
     */
    @Test
    void testSetName() {
        String newName = "Updated Game Name";
        testGame.setName(newName);
        assertEquals(newName, testGame.getName());
    }

    /**
     * Tests getting the players list from the Game.
     * Verifies that the list is initialized correctly.
     */
    @Test
    void testGetPlayers() {
        assertNotNull(testGame.getPlayers());
        assertTrue(testGame.getPlayers().isEmpty());
    }

    /**
     * Tests setting the players list for the Game.
     * Verifies that the list is updated correctly.
     */
    @Test
    void testSetPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);
        testGame.setPlayers(players);

        assertEquals(players, testGame.getPlayers());
        assertEquals(1, testGame.getPlayers().size());
    }

    // ==================== ADD/REMOVE PLAYER TESTS ====================

    /**
     * Tests adding a player to the Game.
     * Verifies that the player is added to the players list.
     */
    @Test
    void testAddPlayer() {
        testGame.addPlayer(testPlayer);

        assertEquals(1, testGame.getPlayers().size());
        assertTrue(testGame.getPlayers().contains(testPlayer));
    }

    /**
     * Tests adding multiple players to the Game.
     * Verifies that all players are added to the players list.
     */
    @Test
    void testAddMultiplePlayers() {
        Player player2 = new Player(UUID.randomUUID(), "Jane", "Smith", new ArrayList<>());

        testGame.addPlayer(testPlayer);
        testGame.addPlayer(player2);

        assertEquals(2, testGame.getPlayers().size());
        assertTrue(testGame.getPlayers().contains(testPlayer));
        assertTrue(testGame.getPlayers().contains(player2));
    }

    /**
     * Tests removing a player from the Game.
     * Verifies that the player is removed from the players list.
     */
    @Test
    void testRemovePlayer() {
        testGame.addPlayer(testPlayer);
        assertEquals(1, testGame.getPlayers().size());

        testGame.removePlayer(testPlayer);
        assertEquals(0, testGame.getPlayers().size());
        assertFalse(testGame.getPlayers().contains(testPlayer));
    }

    /**
     * Tests removing a player who is not in the Game.
     * Verifies that the players list remains unchanged.
     */
    @Test
    void testRemovePlayer_NotInList() {
        Player player2 = new Player(UUID.randomUUID(), "Jane", "Smith", new ArrayList<>());
        testGame.addPlayer(testPlayer);

        testGame.removePlayer(player2);
        assertEquals(1, testGame.getPlayers().size());
        assertTrue(testGame.getPlayers().contains(testPlayer));
    }

    // ==================== EQUALS TESTS ====================

    /**
     * Tests equality of the same Game object.
     * Verifies that a Game is equal to itself.
     */
    @Test
    void testEquals_SameGame() {
        assertEquals(testGame, testGame);
    }

    /**
     * Tests equality of two Game objects with the same properties.
     * Verifies that two Games with identical id, name, and players are equal.
     */
    @Test
    void testEquals_EqualGames() {
        Game game1 = new Game(testGameId, testGameName, new ArrayList<>());
        Game game2 = new Game(testGameId, testGameName, new ArrayList<>());
        assertEquals(game1, game2);
    }

    /**
     * Tests inequality of two Game objects with different IDs.
     * Verifies that Games with different ids are not equal.
     */
    @Test
    void testEquals_DifferentId() {
        Game game1 = new Game(testGameId, testGameName, new ArrayList<>());
        Game game2 = new Game(UUID.randomUUID(), testGameName, new ArrayList<>());
        assertNotEquals(game1, game2);
    }

    /**
     * Tests inequality of two Game objects with different players.
     * Verifies that Games with different players lists are not equal.
     */
    @Test
    void testEquals_DifferentPlayers() {
        List<Player> players1 = new ArrayList<>();
        List<Player> players2 = new ArrayList<>();
        players2.add(testPlayer);

        Game game1 = new Game(testGameId, testGameName, players1);
        Game game2 = new Game(testGameId, testGameName, players2);
        assertNotEquals(game1, game2);
    }

    /**
     * Tests equality with null.
     * Verifies that a Game is not equal to null.
     */
    @Test
    void testEquals_WithNull() {
        assertNotEquals(null, testGame);
    }

    /**
     * Tests equality with an object of a different class.
     * Verifies that a Game is not equal to an object of another type.
     */
    @Test
    void testEquals_WithDifferentClass() {
        assertNotEquals("NotAGame", testGame);
    }

    /**
     * Tests inequality of two Game objects with different names.
     * Verifies that Games with different names are not equal.
     */
    @Test
    void testEquals_DifferentName() {
        Game game1 = new Game(testGameId, "Game 1", new ArrayList<>());
        Game game2 = new Game(testGameId, "Game 2", new ArrayList<>());
        assertNotEquals(game1, game2);
    }

    // ==================== HASHCODE TESTS ====================

    /**
     * Tests hashCode consistency for the same Game object.
     * Verifies that the hash code remains the same across multiple calls.
     */
    @Test
    void testHashCode_SameGames() {
        Game game1 = new Game(testGameId, testGameName, new ArrayList<>());
        Game game2 = new Game(testGameId, testGameName, new ArrayList<>());
        assertEquals(game1.hashCode(), game2.hashCode());
    }

    /**
     * Tests hashCode difference for Game objects with different IDs.
     * Verifies that Games with different ids produce different hash codes.
     */
    @Test
    void testHashCode_DifferentGames() {
        Game game1 = new Game(testGameId, testGameName, new ArrayList<>());
        Game game2 = new Game(UUID.randomUUID(), testGameName, new ArrayList<>());
        assertNotEquals(game1.hashCode(), game2.hashCode());
    }

    // ==================== TOSTRING TESTS ====================

    /**
     * Tests the toString method with an empty players list.
     * Verifies that the string representation contains game information.
     */
    @Test
    void testToString_EmptyPlayers() {
        String result = testGame.toString();
        assertNotNull(result);
        assertTrue(result.contains("Game{"));
        assertTrue(result.contains("id=" + testGameId));
    }

    /**
     * Tests the toString method with players in the list.
     * Verifies that the string representation includes game and player information.
     */
    @Test
    void testToString_WithPlayers() {
        testGame.addPlayer(testPlayer);
        String result = testGame.toString();

        assertNotNull(result);
        assertTrue(result.contains("Game{"));
        assertTrue(result.contains("id=" + testGameId));
        assertTrue(result.contains("name='" + testGameName + "'"));
        assertTrue(result.contains("players="));
    }

    // ==================== EDGE CASES ====================

    /**
     * Tests adding the same player twice to a game.
     * Verifies that duplicate players are allowed in the list.
     */
    @Test
    void testAddSamePlayerTwice() {
        testGame.addPlayer(testPlayer);
        testGame.addPlayer(testPlayer);

        assertEquals(2, testGame.getPlayers().size());
    }

    /**
     * Tests replacing the entire players list with setPlayers.
     * Verifies that the old list is completely replaced with the new one.
     */
    @Test
    void testSetPlayers_ReplacesList() {
        testGame.addPlayer(testPlayer);
        assertEquals(1, testGame.getPlayers().size());

        List<Player> newPlayers = new ArrayList<>();
        Player player2 = new Player(UUID.randomUUID(), "Jane", "Smith", new ArrayList<>());
        newPlayers.add(player2);

        testGame.setPlayers(newPlayers);
        assertEquals(1, testGame.getPlayers().size());
        assertFalse(testGame.getPlayers().contains(testPlayer));
        assertTrue(testGame.getPlayers().contains(player2));
    }
}

