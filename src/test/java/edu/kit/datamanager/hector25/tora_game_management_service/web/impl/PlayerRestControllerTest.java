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
import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.PlayerNotFoundException;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IPlayerService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.PlayerCreationDTO;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for PlayerRestController.
 * Tests all CRUD operations and error scenarios.
 */
class PlayerRestControllerTest {

    private MockMvc mockMvc;
    private JsonMapper objectMapper;

    @Mock
    private IPlayerService playerService;

    private UUID testPlayerId;
    private Player testPlayer;
    private PlayerCreationDTO testPlayerDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PlayerRestController controller = new PlayerRestController(playerService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new edu.kit.datamanager.hector25.tora_game_management_service.web.exceptionhandling.RestExceptionHandler())
                .build();
        objectMapper = new JsonMapper();

        testPlayerId = UUID.randomUUID();
        testPlayerDto = new PlayerCreationDTO("John", "Doe");
        testPlayer = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
    }

    // ==================== CREATE PLAYER TESTS ====================

    @Test
    void testCreatePlayer_Success() throws Exception {
        when(playerService.createPlayer(any(PlayerCreationDTO.class)))
                .thenReturn(testPlayer);

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(testPlayerId.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(playerService, times(1)).createPlayer(any(PlayerCreationDTO.class));
    }

    @Test
    void testCreatePlayer_ValidationFails_BlankFirstName() throws Exception {
        PlayerCreationDTO invalidDto = new PlayerCreationDTO("", "Doe");

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errors", hasItem(containsString("firstName"))));

        verify(playerService, never()).createPlayer(any());
    }

    @Test
    void testCreatePlayer_ValidationFails_BlankLastName() throws Exception {
        PlayerCreationDTO invalidDto = new PlayerCreationDTO("John", "");

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(containsString("lastName"))));

        verify(playerService, never()).createPlayer(any());
    }

    @Test
    void testCreatePlayer_ValidationFails_FirstNameTooLong() throws Exception {
        String longName = "a".repeat(101);
        PlayerCreationDTO invalidDto = new PlayerCreationDTO(longName, "Doe");

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(containsString("firstName"))));

        verify(playerService, never()).createPlayer(any());
    }

    @Test
    void testCreatePlayer_ValidationFails_LastNameTooLong() throws Exception {
        String longName = "a".repeat(101);
        PlayerCreationDTO invalidDto = new PlayerCreationDTO("John", longName);

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(containsString("lastName"))));

        verify(playerService, never()).createPlayer(any());
    }

    // ==================== GET PLAYER BY ID TESTS ====================

    @Test
    void testGetPlayer_Success() throws Exception {
        when(playerService.getPlayerById(testPlayerId))
                .thenReturn(java.util.Optional.of(testPlayer));

        mockMvc.perform(get("/players/{id}", testPlayerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testPlayerId.toString()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(playerService, times(1)).getPlayerById(testPlayerId);
    }

    @Test
    void testGetPlayer_NotFound() throws Exception {
        when(playerService.getPlayerById(testPlayerId))
                .thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/players/{id}", testPlayerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(playerService, times(1)).getPlayerById(testPlayerId);
    }

    // ==================== UPDATE PLAYER TESTS ====================

    @Test
    void testUpdatePlayer_Success() throws Exception {
        Player updatedPlayer = new Player(testPlayerId, "Jane", "Smith", new ArrayList<>());
        PlayerCreationDTO updateDto = new PlayerCreationDTO("Jane", "Smith");

        when(playerService.updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class)))
                .thenReturn(updatedPlayer);

        mockMvc.perform(put("/players/{id}", testPlayerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testPlayerId.toString()))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"));

        verify(playerService, times(1)).updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class));
    }

    @Test
    void testUpdatePlayer_NotFound() throws Exception {
        PlayerCreationDTO updateDto = new PlayerCreationDTO("Jane", "Smith");

        when(playerService.updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class)))
                .thenThrow(new PlayerNotFoundException("Player with id " + testPlayerId + " not found"));

        mockMvc.perform(put("/players/{id}", testPlayerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));

        verify(playerService, times(1)).updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class));
    }

    @Test
    void testUpdatePlayer_ValidationFails() throws Exception {
        PlayerCreationDTO invalidDto = new PlayerCreationDTO("", "Smith");

        mockMvc.perform(put("/players/{id}", testPlayerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(containsString("firstName"))));

        verify(playerService, never()).updatePlayer(any(), any());
    }

    // ==================== DELETE PLAYER TESTS ====================

    @Test
    void testDeletePlayer_Success() throws Exception {
        doNothing().when(playerService).deletePlayerById(testPlayerId);

        mockMvc.perform(delete("/players/{id}", testPlayerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(playerService, times(1)).deletePlayerById(testPlayerId);
    }

    @Test
    void testDeletePlayer_NotFound() throws Exception {
        doThrow(new PlayerNotFoundException("Player with id " + testPlayerId + " not found"))
                .when(playerService).deletePlayerById(testPlayerId);

        mockMvc.perform(delete("/players/{id}", testPlayerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));

        verify(playerService, times(1)).deletePlayerById(testPlayerId);
    }

    // ==================== FIND PLAYER BY NAME TESTS ====================

    @Test
    void testFindPlayerByFirstNameAndLastName_Success() throws Exception {
        List<Player> players = List.of(testPlayer);

        when(playerService.findPlayerByFirstNameAndLastName("John", "Doe"))
                .thenReturn(players);

        mockMvc.perform(get("/players/search?firstName=John&lastName=Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));

        verify(playerService, times(1)).findPlayerByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void testFindPlayerByFirstNameAndLastName_NoResults() throws Exception {
        when(playerService.findPlayerByFirstNameAndLastName("Unknown", "Person"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/players/search?firstName=Unknown&lastName=Person")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(playerService, times(1)).findPlayerByFirstNameAndLastName("Unknown", "Person");
    }

    @Test
    void testFindPlayerByFirstName_Success() throws Exception {
        Player player2 = new Player(UUID.randomUUID(), "John", "Smith", new ArrayList<>());
        List<Player> players = List.of(testPlayer, player2);

        when(playerService.findPlayerByFirstName("John"))
                .thenReturn(players);

        mockMvc.perform(get("/players/search/firstName?firstName=John")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("John"));

        verify(playerService, times(1)).findPlayerByFirstName("John");
    }

    @Test
    void testFindPlayerByFirstName_NoResults() throws Exception {
        when(playerService.findPlayerByFirstName("NonExistent"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/players/search/firstName?firstName=NonExistent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(playerService, times(1)).findPlayerByFirstName("NonExistent");
    }

    @Test
    void testFindPlayerByLastName_Success() throws Exception {
        Player player2 = new Player(UUID.randomUUID(), "Jane", "Doe", new ArrayList<>());
        List<Player> players = List.of(testPlayer, player2);

        when(playerService.findPlayerByLastName("Doe"))
                .thenReturn(players);

        mockMvc.perform(get("/players/search/lastName?lastName=Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].lastName").value("Doe"));

        verify(playerService, times(1)).findPlayerByLastName("Doe");
    }

    @Test
    void testFindPlayerByLastName_NoResults() throws Exception {
        when(playerService.findPlayerByLastName("NonExistent"))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/players/search/lastName?lastName=NonExistent")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(playerService, times(1)).findPlayerByLastName("NonExistent");
    }

    // ==================== MULTIPLE RESULTS TESTS ====================

    @Test
    void testFindPlayerByFirstName_MultipleResults() throws Exception {
        Player player2 = new Player(UUID.randomUUID(), "John", "Smith", new ArrayList<>());
        Player player3 = new Player(UUID.randomUUID(), "John", "Johnson", new ArrayList<>());
        List<Player> players = List.of(testPlayer, player2, player3);

        when(playerService.findPlayerByFirstName("John"))
                .thenReturn(players);

        mockMvc.perform(get("/players/search/firstName?firstName=John")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].firstName", everyItem(equalTo("John"))));

        verify(playerService, times(1)).findPlayerByFirstName("John");
    }

    @Test
    void testFindPlayerByLastName_MultipleResults() throws Exception {
        Player player2 = new Player(UUID.randomUUID(), "Jane", "Doe", new ArrayList<>());
        Player player3 = new Player(UUID.randomUUID(), "Bob", "Doe", new ArrayList<>());
        List<Player> players = List.of(testPlayer, player2, player3);

        when(playerService.findPlayerByLastName("Doe"))
                .thenReturn(players);

        mockMvc.perform(get("/players/search/lastName?lastName=Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].lastName", everyItem(equalTo("Doe"))));

        verify(playerService, times(1)).findPlayerByLastName("Doe");
    }

    @Test
    void testFindPlayerByFirstNameAndLastName_MultipleResults() throws Exception {
        Player player2 = new Player(UUID.randomUUID(), "John", "Doe", new ArrayList<>());
        List<Player> players = List.of(testPlayer, player2);

        when(playerService.findPlayerByFirstNameAndLastName("John", "Doe"))
                .thenReturn(players);

        mockMvc.perform(get("/players/search?firstName=John&lastName=Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].firstName", everyItem(equalTo("John"))))
                .andExpect(jsonPath("$[*].lastName", everyItem(equalTo("Doe"))));

        verify(playerService, times(1)).findPlayerByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void testCreatePlayer_WithSpecialCharacters() throws Exception {
        Player playerWithSpecialChars = new Player(testPlayerId, "Jean-Pierre", "O'Brien", new ArrayList<>());
        PlayerCreationDTO dtoWithSpecialChars = new PlayerCreationDTO("Jean-Pierre", "O'Brien");

        when(playerService.createPlayer(any(PlayerCreationDTO.class)))
                .thenReturn(playerWithSpecialChars);

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoWithSpecialChars)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Jean-Pierre"))
                .andExpect(jsonPath("$.lastName").value("O'Brien"));

        verify(playerService, times(1)).createPlayer(any(PlayerCreationDTO.class));
    }

    @Test
    void testCreatePlayer_WithMaxLengthNames() throws Exception {
        String maxLengthName = "a".repeat(100);
        Player playerWithMaxNames = new Player(testPlayerId, maxLengthName, maxLengthName, new ArrayList<>());
        PlayerCreationDTO dtoWithMaxNames = new PlayerCreationDTO(maxLengthName, maxLengthName);

        when(playerService.createPlayer(any(PlayerCreationDTO.class)))
                .thenReturn(playerWithMaxNames);

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoWithMaxNames)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(maxLengthName))
                .andExpect(jsonPath("$.lastName").value(maxLengthName));

        verify(playerService, times(1)).createPlayer(any(PlayerCreationDTO.class));
    }

    @Test
    void testUpdatePlayer_WithSpecialCharacters() throws Exception {
        Player updatedPlayer = new Player(testPlayerId, "José", "García", new ArrayList<>());
        PlayerCreationDTO updateDto = new PlayerCreationDTO("José", "García");

        when(playerService.updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class)))
                .thenReturn(updatedPlayer);

        mockMvc.perform(put("/players/{id}", testPlayerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("José"))
                .andExpect(jsonPath("$.lastName").value("García"));

        verify(playerService, times(1)).updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class));
    }

    @Test
    void testUpdatePlayer_WithMaxLengthNames() throws Exception {
        String maxLengthName = "a".repeat(100);
        Player updatedPlayer = new Player(testPlayerId, maxLengthName, maxLengthName, new ArrayList<>());
        PlayerCreationDTO updateDto = new PlayerCreationDTO(maxLengthName, maxLengthName);

        when(playerService.updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class)))
                .thenReturn(updatedPlayer);

        mockMvc.perform(put("/players/{id}", testPlayerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(maxLengthName))
                .andExpect(jsonPath("$.lastName").value(maxLengthName));

        verify(playerService, times(1)).updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class));
    }

    @Test
    void testGetPlayer_WithDifferentUUIDs() throws Exception {
        UUID otherId = UUID.randomUUID();
        Player otherPlayer = new Player(otherId, "Jane", "Smith", new ArrayList<>());

        when(playerService.getPlayerById(otherId))
                .thenReturn(java.util.Optional.of(otherPlayer));

        mockMvc.perform(get("/players/{id}", otherId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(otherId.toString()))
                .andExpect(jsonPath("$.firstName").value("Jane"));

        verify(playerService, times(1)).getPlayerById(otherId);
    }

    @Test
    void testGetPlayer_ResponseContainsAllFields() throws Exception {
        when(playerService.getPlayerById(testPlayerId))
                .thenReturn(java.util.Optional.of(testPlayer));

        mockMvc.perform(get("/players/{id}", testPlayerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.games").exists());
    }

    @Test
    void testCreatePlayer_ResponseContainsAllFields() throws Exception {
        when(playerService.createPlayer(any(PlayerCreationDTO.class)))
                .thenReturn(testPlayer);

        mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists())
                .andExpect(jsonPath("$.games").exists());
    }

    @Test
    void testFindPlayerByFirstName_ResponseContainsAllFields() throws Exception {
        List<Player> players = List.of(testPlayer);

        when(playerService.findPlayerByFirstName("John"))
                .thenReturn(players);

        mockMvc.perform(get("/players/search/firstName?firstName=John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].firstName").exists())
                .andExpect(jsonPath("$[0].lastName").exists())
                .andExpect(jsonPath("$[0].games").exists());
    }

    @Test
    void testAllSuccessfulCreatesReturn201() throws Exception {
        when(playerService.createPlayer(any(PlayerCreationDTO.class)))
                .thenReturn(testPlayer);

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/players")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testPlayerDto)))
                    .andExpect(status().isCreated());
        }

        verify(playerService, times(5)).createPlayer(any(PlayerCreationDTO.class));
    }

    @Test
    void testAllSuccessfulReadsReturn200() throws Exception {
        when(playerService.getPlayerById(testPlayerId))
                .thenReturn(java.util.Optional.of(testPlayer));

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/players/{id}", testPlayerId))
                    .andExpect(status().isOk());
        }

        verify(playerService, times(5)).getPlayerById(testPlayerId);
    }

    @Test
    void testAllSuccessfulDeletesReturn204() throws Exception {
        doNothing().when(playerService).deletePlayerById(testPlayerId);

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(delete("/players/{id}", testPlayerId))
                    .andExpect(status().isNoContent());
        }

        verify(playerService, times(5)).deletePlayerById(testPlayerId);
    }

    // ==================== GET GAMES FOR PLAYER TESTS ====================

    @Test
    void testGetGamesForPlayer_Success() throws Exception {
        UUID gameId1 = UUID.randomUUID();
        UUID gameId2 = UUID.randomUUID();
        Game game1 = new Game(gameId1, List.of(testPlayer));
        Game game2 = new Game(gameId2, List.of(testPlayer));
        List<Game> games = List.of(game1, game2);

        when(playerService.getGamesForPlayer(testPlayerId)).thenReturn(games);

        mockMvc.perform(get("/players/{playerId}/games", testPlayerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(gameId1.toString()))
                .andExpect(jsonPath("$[1].id").value(gameId2.toString()));

        verify(playerService, times(1)).getGamesForPlayer(testPlayerId);
    }

    @Test
    void testGetGamesForPlayer_PlayerNotFound() throws Exception {
        when(playerService.getGamesForPlayer(testPlayerId))
                .thenThrow(new PlayerNotFoundException("Player not found"));

        mockMvc.perform(get("/players/{playerId}/games", testPlayerId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));

        verify(playerService, times(1)).getGamesForPlayer(testPlayerId);
    }

    @Test
    void testGetGamesForPlayer_EmptyGames() throws Exception {
        when(playerService.getGamesForPlayer(testPlayerId)).thenReturn(List.of());

        mockMvc.perform(get("/players/{playerId}/games", testPlayerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(playerService, times(1)).getGamesForPlayer(testPlayerId);
    }

    @Test
    void testGetGamesForPlayer_SingleGame() throws Exception {
        UUID gameId = UUID.randomUUID();
        Game game = new Game(gameId, List.of(testPlayer));
        List<Game> games = List.of(game);

        when(playerService.getGamesForPlayer(testPlayerId)).thenReturn(games);

        mockMvc.perform(get("/players/{playerId}/games", testPlayerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(gameId.toString()));

        verify(playerService, times(1)).getGamesForPlayer(testPlayerId);
    }
}

