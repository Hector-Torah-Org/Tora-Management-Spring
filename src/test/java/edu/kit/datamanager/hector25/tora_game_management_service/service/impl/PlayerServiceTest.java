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

import edu.kit.datamanager.hector25.tora_game_management_service.dao.IPlayerDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Game;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.PlayerNotFoundException;
import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.PlayerCreationDTO;
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
 * Unit tests for PlayerService.
 * Tests all business logic and error scenarios.
 */
@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private IPlayerDao playerDao;

    @InjectMocks
    private PlayerService playerService;

    private UUID testPlayerId;
    private Player testPlayer;
    private PlayerCreationDTO playerCreationDTO;

    @BeforeEach
    void setUp() {
        testPlayerId = UUID.randomUUID();
        testPlayer = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
        playerCreationDTO = new PlayerCreationDTO("John", "Doe");
    }

    // ==================== CREATE PLAYER TESTS ====================

    @Test
    void testCreatePlayer_Success() {
        when(playerDao.save(any(Player.class))).thenReturn(testPlayer);

        Player result = playerService.createPlayer(playerCreationDTO);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(playerDao, times(1)).save(any(Player.class));
    }

    @Test
    void testCreatePlayer_SavesToDatabase() {
        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);

        playerService.createPlayer(playerCreationDTO);

        verify(playerDao).save(captor.capture());
        Player savedPlayer = captor.getValue();
        assertEquals("John", savedPlayer.getFirstName());
        assertEquals("Doe", savedPlayer.getLastName());
    }

    // ==================== UPDATE PLAYER TESTS ====================

    @Test
    void testUpdatePlayer_Success() {
        PlayerCreationDTO updateDto = new PlayerCreationDTO("Jane", "Smith");
        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.of(testPlayer));
        when(playerDao.save(any(Player.class))).thenReturn(testPlayer);

        Player result = playerService.updatePlayer(testPlayerId, updateDto);

        assertNotNull(result);
        assertEquals(testPlayerId, result.getId());
        verify(playerDao, times(1)).findPlayerById(testPlayerId);
        verify(playerDao, times(1)).save(any(Player.class));
    }

    @Test
    void testUpdatePlayer_PlayerNotFound() {
        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> playerService.updatePlayer(testPlayerId, playerCreationDTO));

        verify(playerDao, times(1)).findPlayerById(testPlayerId);
        verify(playerDao, never()).save(any());
    }

    @Test
    void testUpdatePlayer_UpdatesCorrectFields() {
        PlayerCreationDTO updateDto = new PlayerCreationDTO("Jane", "Smith");
        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.of(testPlayer));
        when(playerDao.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Player result = playerService.updatePlayer(testPlayerId, updateDto);

        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
    }

    // ==================== DELETE PLAYER TESTS ====================

    @Test
    void testDeletePlayer_Success() {
        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.of(testPlayer));
        doNothing().when(playerDao).delete(any(Player.class));

        playerService.deletePlayerById(testPlayerId);

        verify(playerDao, times(1)).findPlayerById(testPlayerId);
        verify(playerDao, times(1)).delete(testPlayer);
    }

    @Test
    void testDeletePlayer_PlayerNotFound() {
        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> playerService.deletePlayerById(testPlayerId));

        verify(playerDao, times(1)).findPlayerById(testPlayerId);
        verify(playerDao, never()).delete(any());
    }

    // ==================== GET PLAYER TESTS ====================

    @Test
    void testGetPlayerById_Success() {
        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.of(testPlayer));

        Optional<Player> result = playerService.getPlayerById(testPlayerId);

        assertTrue(result.isPresent());
        assertEquals(testPlayer, result.get());
        verify(playerDao, times(1)).findPlayerById(testPlayerId);
    }

    @Test
    void testGetPlayerById_NotFound() {
        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.empty());

        Optional<Player> result = playerService.getPlayerById(testPlayerId);

        assertFalse(result.isPresent());
        verify(playerDao, times(1)).findPlayerById(testPlayerId);
    }

    // ==================== FIND PLAYER BY NAME TESTS ====================

    @Test
    void testFindPlayerByFirstNameAndLastName_Success() {
        List<Player> expectedPlayers = List.of(testPlayer);
        when(playerDao.findPlayerByFirstNameAndLastName("John", "Doe"))
                .thenReturn(expectedPlayers);

        List<Player> result = playerService.findPlayerByFirstNameAndLastName("John", "Doe");

        assertEquals(1, result.size());
        assertEquals(testPlayer, result.getFirst());
        verify(playerDao, times(1)).findPlayerByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void testFindPlayerByFirstNameAndLastName_NoResults() {
        when(playerDao.findPlayerByFirstNameAndLastName("Unknown", "Person"))
                .thenReturn(new ArrayList<>());

        List<Player> result = playerService.findPlayerByFirstNameAndLastName("Unknown", "Person");

        assertTrue(result.isEmpty());
        verify(playerDao, times(1)).findPlayerByFirstNameAndLastName("Unknown", "Person");
    }

    @Test
    void testFindPlayerByFirstNameAndLastName_MultipleResults() {
        Player player2 = new Player(UUID.randomUUID(), "John", "Doe", new ArrayList<>());
        List<Player> expectedPlayers = List.of(testPlayer, player2);
        when(playerDao.findPlayerByFirstNameAndLastName("John", "Doe"))
                .thenReturn(expectedPlayers);

        List<Player> result = playerService.findPlayerByFirstNameAndLastName("John", "Doe");

        assertEquals(2, result.size());
        verify(playerDao, times(1)).findPlayerByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void testFindPlayerByFirstName_Success() {
        Player player2 = new Player(UUID.randomUUID(), "John", "Smith", new ArrayList<>());
        List<Player> expectedPlayers = List.of(testPlayer, player2);
        when(playerDao.findPlayersByFirstName("John"))
                .thenReturn(expectedPlayers);

        List<Player> result = playerService.findPlayerByFirstName("John");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getFirstName().equals("John")));
        verify(playerDao, times(1)).findPlayersByFirstName("John");
    }

    @Test
    void testFindPlayerByFirstName_NoResults() {
        when(playerDao.findPlayersByFirstName("NonExistent"))
                .thenReturn(new ArrayList<>());

        List<Player> result = playerService.findPlayerByFirstName("NonExistent");

        assertTrue(result.isEmpty());
        verify(playerDao, times(1)).findPlayersByFirstName("NonExistent");
    }

    @Test
    void testFindPlayerByLastName_Success() {
        Player player2 = new Player(UUID.randomUUID(), "Jane", "Doe", new ArrayList<>());
        List<Player> expectedPlayers = List.of(testPlayer, player2);
        when(playerDao.findPlayersByLastName("Doe"))
                .thenReturn(expectedPlayers);

        List<Player> result = playerService.findPlayerByLastName("Doe");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getLastName().equals("Doe")));
        verify(playerDao, times(1)).findPlayersByLastName("Doe");
    }

    @Test
    void testFindPlayerByLastName_NoResults() {
        when(playerDao.findPlayersByLastName("NonExistent"))
                .thenReturn(new ArrayList<>());

        List<Player> result = playerService.findPlayerByLastName("NonExistent");

        assertTrue(result.isEmpty());
        verify(playerDao, times(1)).findPlayersByLastName("NonExistent");
    }

    // ==================== EXCEPTION HANDLING TESTS ====================

    @Test
    void testUpdatePlayer_ThrowsCorrectException() {
        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.empty());

        PlayerNotFoundException exception = assertThrows(PlayerNotFoundException.class, () -> playerService.updatePlayer(testPlayerId, playerCreationDTO));

        assertTrue(exception.getMessage().contains(testPlayerId.toString()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testDeletePlayer_ThrowsCorrectException() {
        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.empty());

        PlayerNotFoundException exception = assertThrows(PlayerNotFoundException.class, () -> playerService.deletePlayerById(testPlayerId));

        assertTrue(exception.getMessage().contains(testPlayerId.toString()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    // ==================== GET GAMES FOR PLAYER TESTS ====================

    @Test
    void testGetGamesForPlayer_Success() {
        UUID gameId1 = UUID.randomUUID();
        UUID gameId2 = UUID.randomUUID();
        Game game1 = new Game(gameId1, gameId1.toString(), List.of(testPlayer));
        Game game2 = new Game(gameId2, gameId1.toString(), List.of(testPlayer));
        List<Game> games = List.of(game1, game2);
        testPlayer.setGames(games);

        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.of(testPlayer));

        List<Game> result = playerService.getGamesForPlayer(testPlayerId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(game1));
        assertTrue(result.contains(game2));
        verify(playerDao, times(1)).findPlayerById(testPlayerId);
    }

    @Test
    void testGetGamesForPlayer_PlayerNotFound() {
        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class, () -> playerService.getGamesForPlayer(testPlayerId));

        verify(playerDao, times(1)).findPlayerById(testPlayerId);
    }

    @Test
    void testGetGamesForPlayer_NoGames() {
        testPlayer.setGames(new ArrayList<>());
        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.of(testPlayer));

        List<Game> result = playerService.getGamesForPlayer(testPlayerId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(playerDao, times(1)).findPlayerById(testPlayerId);
    }

    @Test
    void testGetGamesForPlayer_SingleGame() {
        UUID gameId = UUID.randomUUID();
        Game game = new Game(gameId, gameId.toString(), List.of(testPlayer));
        testPlayer.setGames(List.of(game));

        when(playerDao.findPlayerById(testPlayerId)).thenReturn(Optional.of(testPlayer));

        List<Game> result = playerService.getGamesForPlayer(testPlayerId);

        assertEquals(1, result.size());
        assertEquals(game, result.get(0));
        verify(playerDao, times(1)).findPlayerById(testPlayerId);
    }
}

