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

    @BeforeEach
    void setUp() {
        testGameId = UUID.randomUUID();
        testPlayerId = UUID.randomUUID();
        testPlayer = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        testGame = new Game(testGameId, new ArrayList<>());
    }

    // ==================== CONSTRUCTOR TESTS ====================

    @Test
    void testConstructor_NoArgs() {
        Game game = new Game();
        assertNotNull(game);
        assertNull(game.getId());
        assertNotNull(game.getPlayers());
        assertTrue(game.getPlayers().isEmpty());
    }

    @Test
    void testConstructor_WithIdAndPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);
        Game game = new Game(testGameId, players);

        assertEquals(testGameId, game.getId());
        assertEquals(players, game.getPlayers());
        assertEquals(1, game.getPlayers().size());
    }

    // ==================== GETTER/SETTER TESTS ====================

    @Test
    void testGetId() {
        assertEquals(testGameId, testGame.getId());
    }

    @Test
    void testGetPlayers() {
        assertNotNull(testGame.getPlayers());
        assertTrue(testGame.getPlayers().isEmpty());
    }

    @Test
    void testSetPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(testPlayer);
        testGame.setPlayers(players);

        assertEquals(players, testGame.getPlayers());
        assertEquals(1, testGame.getPlayers().size());
    }

    // ==================== ADD/REMOVE PLAYER TESTS ====================

    @Test
    void testAddPlayer() {
        testGame.addPlayer(testPlayer);

        assertEquals(1, testGame.getPlayers().size());
        assertTrue(testGame.getPlayers().contains(testPlayer));
    }

    @Test
    void testAddMultiplePlayers() {
        Player player2 = new Player(UUID.randomUUID(), "Jane", "Smith", new ArrayList<>());

        testGame.addPlayer(testPlayer);
        testGame.addPlayer(player2);

        assertEquals(2, testGame.getPlayers().size());
        assertTrue(testGame.getPlayers().contains(testPlayer));
        assertTrue(testGame.getPlayers().contains(player2));
    }

    @Test
    void testRemovePlayer() {
        testGame.addPlayer(testPlayer);
        assertEquals(1, testGame.getPlayers().size());

        testGame.removePlayer(testPlayer);
        assertEquals(0, testGame.getPlayers().size());
        assertFalse(testGame.getPlayers().contains(testPlayer));
    }

    @Test
    void testRemovePlayer_NotInList() {
        Player player2 = new Player(UUID.randomUUID(), "Jane", "Smith", new ArrayList<>());
        testGame.addPlayer(testPlayer);

        testGame.removePlayer(player2);
        assertEquals(1, testGame.getPlayers().size());
        assertTrue(testGame.getPlayers().contains(testPlayer));
    }

    // ==================== EQUALS TESTS ====================

    @Test
    void testEquals_SameGame() {
        assertEquals(testGame, testGame);
    }

    @Test
    void testEquals_EqualGames() {
        Game game1 = new Game(testGameId, new ArrayList<>());
        Game game2 = new Game(testGameId, new ArrayList<>());
        assertEquals(game1, game2);
    }

    @Test
    void testEquals_DifferentId() {
        Game game1 = new Game(testGameId, new ArrayList<>());
        Game game2 = new Game(UUID.randomUUID(), new ArrayList<>());
        assertNotEquals(game1, game2);
    }

    @Test
    void testEquals_DifferentPlayers() {
        List<Player> players1 = new ArrayList<>();
        List<Player> players2 = new ArrayList<>();
        players2.add(testPlayer);

        Game game1 = new Game(testGameId, players1);
        Game game2 = new Game(testGameId, players2);
        assertNotEquals(game1, game2);
    }

    @Test
    void testEquals_WithNull() {
        assertNotEquals(null, testGame);
    }

    @Test
    void testEquals_WithDifferentClass() {
        assertNotEquals("NotAGame", testGame);
    }

    // ==================== HASHCODE TESTS ====================

    @Test
    void testHashCode_SameGames() {
        Game game1 = new Game(testGameId, new ArrayList<>());
        Game game2 = new Game(testGameId, new ArrayList<>());
        assertEquals(game1.hashCode(), game2.hashCode());
    }

    @Test
    void testHashCode_DifferentGames() {
        Game game1 = new Game(testGameId, new ArrayList<>());
        Game game2 = new Game(UUID.randomUUID(), new ArrayList<>());
        assertNotEquals(game1.hashCode(), game2.hashCode());
    }

    // ==================== TOSTRING TESTS ====================

    @Test
    void testToString_EmptyPlayers() {
        String result = testGame.toString();
        assertNotNull(result);
        assertTrue(result.contains("Game{"));
        assertTrue(result.contains("id=" + testGameId));
    }

    @Test
    void testToString_WithPlayers() {
        testGame.addPlayer(testPlayer);
        String result = testGame.toString();

        assertNotNull(result);
        assertTrue(result.contains("Game{"));
        assertTrue(result.contains("id=" + testGameId));
        assertTrue(result.contains("players="));
    }

    // ==================== EDGE CASES ====================

    @Test
    void testAddSamePlayerTwice() {
        testGame.addPlayer(testPlayer);
        testGame.addPlayer(testPlayer);

        assertEquals(2, testGame.getPlayers().size());
    }

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

