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

package edu.kit.datamanager.hector25.tora_game_management_service.service.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.dao.IGameDao;
import edu.kit.datamanager.hector25.tora_game_management_service.dao.IPlayerDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Game;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.GameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GameService.
 * Tests all business logic and error scenarios.
 */
@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private IGameDao gameDao;

    @Mock
    private IPlayerDao playerDao;

    @InjectMocks
    private GameService gameService;

    private UUID testGameId;
    private UUID testPlayerId1;
    private UUID testPlayerId2;
    private Game testGame;
    private Player testPlayer1;
    private Player testPlayer2;
    private List<UUID> playerIds;

    @BeforeEach
    void setUp() {
        testGameId = UUID.randomUUID();
        testPlayerId1 = UUID.randomUUID();
        testPlayerId2 = UUID.randomUUID();

        testPlayer1 = new Player(testPlayerId1, "John", "Doe", new ArrayList<>());
        testPlayer2 = new Player(testPlayerId2, "Jane", "Smith", new ArrayList<>());

        testGame = new Game(testGameId, List.of(testPlayer1, testPlayer2));
        playerIds = List.of(testPlayerId1, testPlayerId2);
    }

    // ==================== CREATE GAME TESTS ====================

    @Test
    void testCreateGame_Success() {
        when(playerDao.findPlayerById(testPlayerId1)).thenReturn(Optional.of(testPlayer1));
        when(playerDao.findPlayerById(testPlayerId2)).thenReturn(Optional.of(testPlayer2));
        when(gameDao.save(any(Game.class))).thenReturn(testGame);

        Game result = gameService.createGame(playerIds);

        assertNotNull(result);
        assertEquals(2, result.getPlayers().size());
        verify(gameDao, times(1)).save(any(Game.class));
        verify(playerDao, times(2)).findPlayerById(any());
    }

    @Test
    void testCreateGame_WithSinglePlayer() {
        List<UUID> singlePlayerIds = List.of(testPlayerId1);
        when(playerDao.findPlayerById(testPlayerId1)).thenReturn(Optional.of(testPlayer1));
        when(gameDao.save(any(Game.class))).thenReturn(new Game(testGameId, List.of(testPlayer1)));

        Game result = gameService.createGame(singlePlayerIds);

        assertNotNull(result);
        assertEquals(1, result.getPlayers().size());
    }

    @Test
    void testCreateGame_WithEmptyPlayerList() {
        when(gameDao.save(any(Game.class))).thenReturn(new Game(testGameId, List.of()));

        Game result = gameService.createGame(List.of());

        assertNotNull(result);
        assertEquals(0, result.getPlayers().size());
    }

    @Test
    void testCreateGame_SavesToDatabase() {
        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        when(playerDao.findPlayerById(testPlayerId1)).thenReturn(Optional.of(testPlayer1));
        when(playerDao.findPlayerById(testPlayerId2)).thenReturn(Optional.of(testPlayer2));

        gameService.createGame(playerIds);

        verify(gameDao).save(captor.capture());
        Game savedGame = captor.getValue();
        assertEquals(2, savedGame.getPlayers().size());
    }

    // ==================== UPDATE GAME TESTS ====================

    @Test
    void testUpdateGame_Success() {
        List<UUID> updatePlayerIds = List.of(testPlayerId1);
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(testGame));
        when(playerDao.findPlayerById(testPlayerId1)).thenReturn(Optional.of(testPlayer1));
        when(gameDao.save(any(Game.class))).thenReturn(testGame);

        Game result = gameService.updateGame(testGameId, updatePlayerIds);

        assertNotNull(result);
        assertEquals(testGameId, result.getId());
        verify(gameDao, times(1)).save(any(Game.class));
    }

    @Test
    void testUpdateGame_GameNotFound() {
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () ->
                gameService.updateGame(testGameId, playerIds)
        );
        verify(gameDao, never()).save(any());
    }

    @Test
    void testUpdateGame_ReplaceAllPlayers() {
        Player testPlayer3 = new Player(UUID.randomUUID(), "Bob", "Johnson", new ArrayList<>());
        List<UUID> newPlayerIds = List.of(testPlayer3.getId());

        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(testGame));
        when(playerDao.findPlayerById(testPlayer3.getId())).thenReturn(Optional.of(testPlayer3));
        when(gameDao.save(any(Game.class))).thenReturn(testGame);

        Game result = gameService.updateGame(testGameId, newPlayerIds);

        assertNotNull(result);
        verify(gameDao).save(any(Game.class));
    }

    // ==================== GET GAME BY ID TESTS ====================

    @Test
    void testGetGameById_Success() {
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(testGame));

        Optional<Game> result = gameService.getGameById(testGameId);

        assertTrue(result.isPresent());
        assertEquals(testGameId, result.get().getId());
    }

    @Test
    void testGetGameById_NotFound() {
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.empty());

        Optional<Game> result = gameService.getGameById(testGameId);

        assertFalse(result.isPresent());
    }

    // ==================== GET ALL GAMES TESTS ====================

    @Test
    void testGetAllGames_Success() {
        List<Game> games = List.of(testGame);
        when(gameDao.findAll()).thenReturn(games);

        List<Game> result = gameService.getAllGames();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testGameId, result.get(0).getId());
    }

    @Test
    void testGetAllGames_Empty() {
        when(gameDao.findAll()).thenReturn(List.of());

        List<Game> result = gameService.getAllGames();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetAllGames_MultipleGames() {
        UUID gameId2 = UUID.randomUUID();
        Game game2 = new Game(gameId2, List.of(testPlayer1));
        List<Game> games = List.of(testGame, game2);
        when(gameDao.findAll()).thenReturn(games);

        List<Game> result = gameService.getAllGames();

        assertEquals(2, result.size());
    }

    // ==================== DELETE GAME TESTS ====================

    @Test
    void testDeleteGameById_Success() {
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(testGame));

        gameService.deleteGameById(testGameId);

        verify(gameDao, times(1)).delete(testGame);
    }

    @Test
    void testDeleteGameById_GameNotFound() {
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () ->
                gameService.deleteGameById(testGameId)
        );
        verify(gameDao, never()).delete(any());
    }

    // ==================== GET PLAYERS FOR GAME TESTS ====================

    @Test
    void testGetPlayersForGame_Success() {
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(testGame));

        List<Player> result = gameService.getPlayersForGame(testGameId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testPlayer1));
        assertTrue(result.contains(testPlayer2));
    }

    @Test
    void testGetPlayersForGame_GameNotFound() {
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () ->
                gameService.getPlayersForGame(testGameId)
        );
    }

    @Test
    void testGetPlayersForGame_EmptyGame() {
        Game emptyGame = new Game(testGameId, List.of());
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(emptyGame));

        List<Player> result = gameService.getPlayersForGame(testGameId);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetPlayersForGame_SinglePlayer() {
        Game singlePlayerGame = new Game(testGameId, List.of(testPlayer1));
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(singlePlayerGame));

        List<Player> result = gameService.getPlayersForGame(testGameId);

        assertEquals(1, result.size());
        assertEquals(testPlayer1, result.get(0));
    }

    // ==================== ADD PLAYER TO GAME TESTS ====================

    @Test
    void testAddPlayerToGame_Success() {
        Game mutableGame = new Game(testGameId, new ArrayList<>(List.of(testPlayer1, testPlayer2)));
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(mutableGame));
        UUID newPlayerId = UUID.randomUUID();
        Player newPlayer = new Player(newPlayerId, "Bob", "Johnson", new ArrayList<>());
        when(playerDao.findPlayerById(newPlayerId)).thenReturn(Optional.of(newPlayer));
        when(gameDao.save(any(Game.class))).thenReturn(mutableGame);

        Game result = gameService.addPlayerToGame(testGameId, newPlayerId);

        assertNotNull(result);
        verify(gameDao, times(1)).save(mutableGame);
        verify(playerDao, times(1)).findPlayerById(newPlayerId);
    }

    @Test
    void testAddPlayerToGame_PlayerAlreadyInGame() {
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(testGame));
        when(playerDao.findPlayerById(testPlayerId1)).thenReturn(Optional.of(testPlayer1));

        Game result = gameService.addPlayerToGame(testGameId, testPlayerId1);

        assertNotNull(result);
        // Should not save again if player already exists
        verify(gameDao, never()).save(any());
    }

    @Test
    void testAddPlayerToGame_PlayerNotFound() {
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(testGame));
        UUID nonExistentPlayerId = UUID.randomUUID();
        when(playerDao.findPlayerById(nonExistentPlayerId)).thenReturn(Optional.empty());

        Game result = gameService.addPlayerToGame(testGameId, nonExistentPlayerId);

        assertNotNull(result);
        verify(gameDao, never()).save(any());
    }

    @Test
    void testAddPlayerToGame_GameNotFound() {
        UUID nonExistentGameId = UUID.randomUUID();
        when(gameDao.findGameById(nonExistentGameId)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () ->
                gameService.addPlayerToGame(nonExistentGameId, testPlayerId1)
        );
        verify(playerDao, never()).findPlayerById(any());
        verify(gameDao, never()).save(any());
    }

    // ==================== REMOVE PLAYER FROM GAME TESTS ====================

    @Test
    void testRemovePlayerFromGame_Success() {
        Game mutableGame = new Game(testGameId, new ArrayList<>(List.of(testPlayer1, testPlayer2)));
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(mutableGame));
        when(playerDao.findPlayerById(testPlayerId1)).thenReturn(Optional.of(testPlayer1));
        when(gameDao.save(any(Game.class))).thenReturn(mutableGame);

        Game result = gameService.removePlayerFromGame(testGameId, testPlayerId1);

        assertNotNull(result);
        verify(gameDao, times(1)).save(mutableGame);
        verify(playerDao, times(1)).findPlayerById(testPlayerId1);
    }

    @Test
    void testRemovePlayerFromGame_PlayerNotFound() {
        when(gameDao.findGameById(testGameId)).thenReturn(Optional.of(testGame));
        UUID nonExistentPlayerId = UUID.randomUUID();
        when(playerDao.findPlayerById(nonExistentPlayerId)).thenReturn(Optional.empty());

        Game result = gameService.removePlayerFromGame(testGameId, nonExistentPlayerId);

        assertNotNull(result);
        // Should not save if player doesn't exist
        verify(gameDao, never()).save(any());
    }

    @Test
    void testRemovePlayerFromGame_GameNotFound() {
        UUID nonExistentGameId = UUID.randomUUID();
        when(gameDao.findGameById(nonExistentGameId)).thenReturn(Optional.empty());

        assertThrows(GameNotFoundException.class, () ->
                gameService.removePlayerFromGame(nonExistentGameId, testPlayerId1)
        );
        verify(playerDao, never()).findPlayerById(any());
        verify(gameDao, never()).save(any());
    }
}

