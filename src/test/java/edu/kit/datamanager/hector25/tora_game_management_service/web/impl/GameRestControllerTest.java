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

package edu.kit.datamanager.hector25.tora_game_management_service.web.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Game;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.GameNotFoundException;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for GameRestController.
 * Tests all CRUD operations and error scenarios.
 */
class GameRestControllerTest {

    private MockMvc mockMvc;
    private JsonMapper objectMapper;

    @Mock
    private IGameService gameService;

    private UUID testGameId;
    private UUID testPlayerId1;
    private UUID testPlayerId2;
    private Game testGame;
    private Player testPlayer1;
    private Player testPlayer2;
    private List<UUID> testPlayerIds;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        GameRestController controller = new GameRestController(gameService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new edu.kit.datamanager.hector25.tora_game_management_service.web.exceptionhandling.RestExceptionHandler())
                .build();
        objectMapper = new JsonMapper();

        testGameId = UUID.randomUUID();
        testPlayerId1 = UUID.randomUUID();
        testPlayerId2 = UUID.randomUUID();

        testPlayer1 = new Player(testPlayerId1, "John", "Doe", new ArrayList<>());
        testPlayer2 = new Player(testPlayerId2, "Jane", "Smith", new ArrayList<>());

        testPlayerIds = List.of(testPlayerId1, testPlayerId2);
        testGame = new Game(testGameId, List.of(testPlayer1, testPlayer2));
    }

    // ==================== CREATE GAME TESTS ====================

    @Test
    void testCreateGame_Success() throws Exception {
        when(gameService.createGame(any(List.class)))
                .thenReturn(testGame);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayerIds)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testGameId.toString()))
                .andExpect(jsonPath("$.players", hasSize(2)));

        verify(gameService, times(1)).createGame(any(List.class));
    }

    @Test
    void testCreateGame_ValidationFails_NullPlayerIds() throws Exception {
        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());

        verify(gameService, never()).createGame(any());
    }

    // ==================== GET GAME BY ID TESTS ====================

    @Test
    void testGetGame_Success() throws Exception {
        when(gameService.getGameById(testGameId)).thenReturn(java.util.Optional.of(testGame));

        mockMvc.perform(get("/games/" + testGameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testGameId.toString()))
                .andExpect(jsonPath("$.players", hasSize(2)));

        verify(gameService, times(1)).getGameById(testGameId);
    }

    @Test
    void testGetGame_NotFound() throws Exception {
        when(gameService.getGameById(testGameId))
                .thenThrow(new GameNotFoundException("Game not found"));

        mockMvc.perform(get("/games/" + testGameId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));

        verify(gameService, times(1)).getGameById(testGameId);
    }

    // ==================== GET ALL GAMES TESTS ====================

    @Test
    void testGetAllGames_Success() throws Exception {
        List<Game> games = List.of(testGame);
        when(gameService.getAllGames()).thenReturn(games);

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(testGameId.toString()));

        verify(gameService, times(1)).getAllGames();
    }

    @Test
    void testGetAllGames_Empty() throws Exception {
        when(gameService.getAllGames()).thenReturn(List.of());

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(gameService, times(1)).getAllGames();
    }

    @Test
    void testGetAllGames_MultipleGames() throws Exception {
        UUID gameId2 = UUID.randomUUID();
        Game game2 = new Game(gameId2, List.of(testPlayer1));
        List<Game> games = List.of(testGame, game2);
        when(gameService.getAllGames()).thenReturn(games);

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(gameService, times(1)).getAllGames();
    }

    // ==================== UPDATE GAME TESTS ====================

    @Test
    void testUpdateGame_Success() throws Exception {
        when(gameService.updateGame(eq(testGameId), any(List.class)))
                .thenReturn(testGame);

        mockMvc.perform(put("/games/" + testGameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayerIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testGameId.toString()))
                .andExpect(jsonPath("$.players", hasSize(2)));

        verify(gameService, times(1)).updateGame(eq(testGameId), any(List.class));
    }

    @Test
    void testUpdateGame_NotFound() throws Exception {
        when(gameService.updateGame(eq(testGameId), any(List.class)))
                .thenThrow(new GameNotFoundException("Game not found"));

        mockMvc.perform(put("/games/" + testGameId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayerIds)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(gameService, times(1)).updateGame(eq(testGameId), any(List.class));
    }

    // ==================== DELETE GAME TESTS ====================

    @Test
    void testDeleteGame_Success() throws Exception {
        doNothing().when(gameService).deleteGameById(testGameId);

        mockMvc.perform(delete("/games/" + testGameId))
                .andExpect(status().isNoContent());

        verify(gameService, times(1)).deleteGameById(testGameId);
    }

    @Test
    void testDeleteGame_NotFound() throws Exception {
        doThrow(new GameNotFoundException("Game not found"))
                .when(gameService).deleteGameById(testGameId);

        mockMvc.perform(delete("/games/" + testGameId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(gameService, times(1)).deleteGameById(testGameId);
    }

    // ==================== GET PLAYERS FOR GAME TESTS ====================

    @Test
    void testGetPlayersForGame_Success() throws Exception {
        List<Player> players = List.of(testPlayer1, testPlayer2);
        when(gameService.getPlayersForGame(testGameId)).thenReturn(players);

        mockMvc.perform(get("/games/" + testGameId + "/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(testPlayerId1.toString()))
                .andExpect(jsonPath("$[1].id").value(testPlayerId2.toString()));

        verify(gameService, times(1)).getPlayersForGame(testGameId);
    }

    @Test
    void testGetPlayersForGame_GameNotFound() throws Exception {
        when(gameService.getPlayersForGame(testGameId))
                .thenThrow(new GameNotFoundException("Game not found"));

        mockMvc.perform(get("/games/" + testGameId + "/players"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(gameService, times(1)).getPlayersForGame(testGameId);
    }

    @Test
    void testGetPlayersForGame_EmptyGame() throws Exception {
        when(gameService.getPlayersForGame(testGameId)).thenReturn(List.of());

        mockMvc.perform(get("/games/" + testGameId + "/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(gameService, times(1)).getPlayersForGame(testGameId);
    }

    // ==================== ADD PLAYER TO GAME TESTS ====================

    @Test
    void testAddPlayerToGame_Success() throws Exception {
        when(gameService.addPlayerToGame(testGameId, testPlayerId1)).thenReturn(testGame);

        mockMvc.perform(post("/games/" + testGameId + "/players/" + testPlayerId1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testGameId.toString()))
                .andExpect(jsonPath("$.players", hasSize(2)));

        verify(gameService, times(1)).addPlayerToGame(testGameId, testPlayerId1);
    }

    @Test
    void testAddPlayerToGame_GameNotFound() throws Exception {
        when(gameService.addPlayerToGame(testGameId, testPlayerId1))
                .thenThrow(new GameNotFoundException("Game not found"));

        mockMvc.perform(post("/games/" + testGameId + "/players/" + testPlayerId1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(gameService, times(1)).addPlayerToGame(testGameId, testPlayerId1);
    }

    @Test
    void testAddPlayerToGame_WithNewPlayer() throws Exception {
        UUID newPlayerId = UUID.randomUUID();
        Player newPlayer = new Player(newPlayerId, "Bob", "Johnson", new ArrayList<>());
        Game updatedGame = new Game(testGameId, List.of(testPlayer1, testPlayer2, newPlayer));

        when(gameService.addPlayerToGame(testGameId, newPlayerId)).thenReturn(updatedGame);

        mockMvc.perform(post("/games/" + testGameId + "/players/" + newPlayerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testGameId.toString()))
                .andExpect(jsonPath("$.players", hasSize(3)));

        verify(gameService, times(1)).addPlayerToGame(testGameId, newPlayerId);
    }

    // ==================== REMOVE PLAYER FROM GAME TESTS ====================

    @Test
    void testRemovePlayerFromGame_Success() throws Exception {
        Game updatedGame = new Game(testGameId, List.of(testPlayer2));
        when(gameService.removePlayerFromGame(testGameId, testPlayerId1)).thenReturn(updatedGame);

        mockMvc.perform(delete("/games/" + testGameId + "/players/" + testPlayerId1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testGameId.toString()))
                .andExpect(jsonPath("$.players", hasSize(1)));

        verify(gameService, times(1)).removePlayerFromGame(testGameId, testPlayerId1);
    }

    @Test
    void testRemovePlayerFromGame_GameNotFound() throws Exception {
        when(gameService.removePlayerFromGame(testGameId, testPlayerId1))
                .thenThrow(new GameNotFoundException("Game not found"));

        mockMvc.perform(delete("/games/" + testGameId + "/players/" + testPlayerId1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(gameService, times(1)).removePlayerFromGame(testGameId, testPlayerId1);
    }

    @Test
    void testRemovePlayerFromGame_LastPlayer() throws Exception {
        Game emptyGame = new Game(testGameId, List.of());
        when(gameService.removePlayerFromGame(testGameId, testPlayerId1)).thenReturn(emptyGame);

        mockMvc.perform(delete("/games/" + testGameId + "/players/" + testPlayerId1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testGameId.toString()))
                .andExpect(jsonPath("$.players", hasSize(0)));

        verify(gameService, times(1)).removePlayerFromGame(testGameId, testPlayerId1);
    }
}

