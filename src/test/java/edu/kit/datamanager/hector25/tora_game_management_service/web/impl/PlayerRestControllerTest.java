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
//package edu.kit.datamanager.hector25.tora_game_management_service.web.impl;
//
//import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
//import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.PlayerNotFoundException;
//import edu.kit.datamanager.hector25.tora_game_management_service.service.IPlayerService;
//import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.PlayerCreationDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import tools.jackson.databind.json.JsonMapper;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.hamcrest.Matchers.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * Unit tests for PlayerRestController
// * <p>
// * This test class provides comprehensive coverage of the PlayerRestController's REST API endpoints,
// * including:
// * <ul>
// * <li>Player CRUD operations (Create, Read, Update, Delete)</li>
// * <li>Input validation and error handling</li>
// * <li>Search functionality by first name, last name, and both</li>
// * <li>Edge cases with special characters and max-length names</li>
// * <li>HTTP status code verification</li>
// * <li>Service method invocation verification</li>
// * </ul>
// * Each test is isolated and uses Mockito to mock the underlying IPlayerService dependency.
// * The tests verify both successful operations and error scenarios.
// */
//class PlayerRestControllerTest {
//    private MockMvc mockMvc;
//    private JsonMapper objectMapper;
//
//    @Mock
//    private IPlayerService playerService;
//
//    private UUID testPlayerId;
//    private Player testPlayer;
//    private PlayerCreationDTO testPlayerDto;
//
//    @BeforeEach
//    void setUp() throws Exception {
//        try (var ignored = MockitoAnnotations.openMocks(this)) {
//            PlayerRestController controller = new PlayerRestController(playerService);
//            mockMvc = MockMvcBuilders.standaloneSetup(controller)
//                    .setControllerAdvice(new edu.kit.datamanager.hector25.tora_game_management_service.web.exceptionhandling.RestExceptionHandler())
//                    .build();
//            objectMapper = new JsonMapper();
//
//            testPlayerId = UUID.randomUUID();
//            testPlayerDto = new PlayerCreationDTO("John", "Doe");
//            testPlayer = new Player(testPlayerId, "John", "Doe", new ArrayList<>());
//        }
//    }
//
//    // ==================== CREATE PLAYER TESTS ====================
//
//    /**
//     * Test: Successfully creating a new player with valid data.
//     * when(playerService.createPlayer(any(PlayerCreationDTO.class)))
//     * <p>
//     * Verifies that:
//     * - POST /players with valid PlayerCreationDTO returns 201 Created
//     * - Response body contains the created player with all fields
//     * - Service createPlayer method is called exactly once
//     */
//    @Test
//    void testCreatePlayer_Success() throws Exception {
//        when(playerService.createPlayer(any(PlayerCreationDTO.class)))
//                .thenReturn(testPlayer);
//
//        mockMvc.perform(post("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testPlayerDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(testPlayerId.toString()))
//                .andExpect(jsonPath("$.firstName").value("John"))
//                .andExpect(jsonPath("$.lastName").value("Doe"));
//
//        verify(playerService, times(1)).createPlayer(any(PlayerCreationDTO.class));
//    }
//
//    /**
//     * Test: Player creation fails validation when first name is blank.
//     * <p>
//     * Verifies that:
//     * - POST /players with blank firstName returns 400 Bad Request
//     * - Response contains validation error for firstName field
//     * - Service createPlayer method is never called
//     */
//    @Test
//    void testCreatePlayer_ValidationFails_BlankFirstName() throws Exception {
//        PlayerCreationDTO invalidDto = new PlayerCreationDTO("", "Doe");
//
//        mockMvc.perform(post("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value(400))
//                .andExpect(jsonPath("$.error").value("Validation Failed"))
//                .andExpect(jsonPath("$.errors", hasItem(containsString("firstName"))));
//
//        verify(playerService, never()).createPlayer(any());
//    }
//
//    /**
//     * Test: Player creation fails validation when last name is blank.
//     * <p>
//     * Verifies that:
//     * - POST /players with blank lastName returns 400 Bad Request
//     * - Response contains validation error for lastName field
//     * - Service createPlayer method is never called
//     */
//    @Test
//    void testCreatePlayer_ValidationFails_BlankLastName() throws Exception {
//        PlayerCreationDTO invalidDto = new PlayerCreationDTO("John", "");
//
//        mockMvc.perform(post("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors", hasItem(containsString("lastName"))));
//
//        verify(playerService, never()).createPlayer(any());
//    }
//
//    /**
//     * Test: Player creation fails validation when first name exceeds maximum length.
//     * <p>
//     * Verifies that:
//     * - POST /players with firstName exceeding 100 characters returns 400 Bad Request
//     * - Response contains validation error for firstName field
//     * - Service createPlayer method is never called
//     */
//    @Test
//    void testCreatePlayer_ValidationFails_FirstNameTooLong() throws Exception {
//        String longName = "a".repeat(101);
//        PlayerCreationDTO invalidDto = new PlayerCreationDTO(longName, "Doe");
//
//        mockMvc.perform(post("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors", hasItem(containsString("firstName"))));
//
//        verify(playerService, never()).createPlayer(any());
//    }
//
//    /**
//     * Test: Player creation fails validation when last name exceeds maximum length.
//     * <p>
//     * Verifies that:
//     * - POST /players with lastName exceeding 100 characters returns 400 Bad Request
//     * - Response contains validation error for lastName field
//     * - Service createPlayer method is never called
//     */
//    @Test
//    void testCreatePlayer_ValidationFails_LastNameTooLong() throws Exception {
//        String longName = "a".repeat(101);
//        PlayerCreationDTO invalidDto = new PlayerCreationDTO("John", longName);
//
//        mockMvc.perform(post("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors", hasItem(containsString("lastName"))));
//
//        verify(playerService, never()).createPlayer(any());
//    }
//
//    /**
//     * Test: Player creation fails validation when first name is null.
//     * <p>
//     * Verifies that:
//     * - POST /players with null firstName returns 400 Bad Request
//     * - Service createPlayer method is never called
//     */
//    @Test
//    void testCreatePlayer_ValidationFails_NullFirstName() throws Exception {
//        mockMvc.perform(post("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"firstName\": null, \"lastName\": \"Doe\"}"))
//                .andExpect(status().isBadRequest());
//
//        verify(playerService, never()).createPlayer(any());
//    }
//
//    /**
//     * Test: Player creation fails validation when last name is null.
//     * <p>
//     * Verifies that:
//     * - POST /players with null lastName returns 400 Bad Request
//     * - Service createPlayer method is never called
//     */
//    @Test
//    void testCreatePlayer_ValidationFails_NullLastName() throws Exception {
//        mockMvc.perform(post("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"firstName\": \"John\", \"lastName\": null}"))
//                .andExpect(status().isBadRequest());
//
//        verify(playerService, never()).createPlayer(any());
//    }
//
//    /**
//     * Test: Player creation fails when request body contains malformed JSON.
//     * <p>
//     * Verifies that:
//     * - POST /players with invalid JSON returns 400 Bad Request
//     * - Error response indicates "Malformed JSON Request"
//     * - Service createPlayer method is never called
//     */
//    @Test
//    void testCreatePlayer_MalformedJson() throws Exception {
//        mockMvc.perform(post("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{invalid json"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value(400))
//                .andExpect(jsonPath("$.error").value("Malformed JSON Request"));
//
//        verify(playerService, never()).createPlayer(any());
//    }
//
//    /**
//     * Test: Successfully creating a player with special characters in name.
//     * <p>
//     * Verifies that:
//     * - POST /players with special characters (José, O'Brien-Smith) returns 201 Created
//     * - Response body preserves the special characters correctly
//     * - Service createPlayer method is called exactly once
//     */
//    @Test
//    void testCreatePlayer_WithSpecialCharacters() throws Exception {
//        Player specialPlayer = new Player(testPlayerId, "José", "O'Brien-Smith", new ArrayList<>());
//        PlayerCreationDTO specialDto = new PlayerCreationDTO("José", "O'Brien-Smith");
//        when(playerService.createPlayer(any(PlayerCreationDTO.class)))
//                .thenReturn(specialPlayer);
//
//        mockMvc.perform(post("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(specialDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.firstName").value("José"))
//                .andExpect(jsonPath("$.lastName").value("O'Brien-Smith"));
//
//        verify(playerService, times(1)).createPlayer(any(PlayerCreationDTO.class));
//    }
//
//    // ==================== GET PLAYER BY ID TESTS ====================
//
//    /**
//     * Test: Successfully retrieving a player by ID with valid UUID.
//     * <p>
//     * Verifies that:
//     * - GET /players/{id} with valid UUID returns 200 OK
//     * - Response body contains the correct player with all fields
//     * - Service getPlayerById method is called exactly once
//     */
//    @Test
//    void testGetPlayer_Success() throws Exception {
//        when(playerService.getPlayerById(testPlayerId))
//                .thenReturn(java.util.Optional.of(testPlayer));
//
//        mockMvc.perform(get("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(testPlayerId.toString()))
//                .andExpect(jsonPath("$.firstName").value("John"))
//                .andExpect(jsonPath("$.lastName").value("Doe"));
//
//        verify(playerService, times(1)).getPlayerById(testPlayerId);
//    }
//
//    /**
//     * Test: Retrieving a player fails when the player ID does not exist.
//     * <p>
//     * Verifies that:
//     * - GET /players/{id} with non-existent UUID returns 404 Not Found
//     * - Service getPlayerById method is called exactly once
//     */
//    @Test
//    void testGetPlayer_NotFound() throws Exception {
//        when(playerService.getPlayerById(testPlayerId))
//                .thenReturn(java.util.Optional.empty());
//
//        mockMvc.perform(get("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//
//        verify(playerService, times(1)).getPlayerById(testPlayerId);
//    }
//
//    // ==================== UPDATE PLAYER TESTS ====================
//
//    /**
//     * Test: Successfully updating a player with valid data.
//     * <p>
//     * Verifies that:
//     * - PUT /players/{id} with valid PlayerCreationDTO returns 200 OK
//     * - Response body contains the updated player with new values
//     * - Service updatePlayer method is called exactly once with correct parameters
//     */
//    @Test
//    void testUpdatePlayer_Success() throws Exception {
//        Player updatedPlayer = new Player(testPlayerId, "Jane", "Smith", new ArrayList<>());
//        PlayerCreationDTO updateDto = new PlayerCreationDTO("Jane", "Smith");
//
//        when(playerService.updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class)))
//                .thenReturn(updatedPlayer);
//
//        mockMvc.perform(put("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(testPlayerId.toString()))
//                .andExpect(jsonPath("$.firstName").value("Jane"))
//                .andExpect(jsonPath("$.lastName").value("Smith"));
//
//        verify(playerService, times(1)).updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class));
//    }
//
//    /**
//     * Test: Player update fails when the player ID does not exist.
//     * <p>
//     * Verifies that:
//     * - PUT /players/{id} with non-existent UUID throws PlayerNotFoundException
//     * - Response returns 404 Not Found with proper error structure
//     * - Service updatePlayer method is called exactly once
//     */
//    @Test
//    void testUpdatePlayer_NotFound() throws Exception {
//        PlayerCreationDTO updateDto = new PlayerCreationDTO("Jane", "Smith");
//
//        when(playerService.updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class)))
//                .thenThrow(new PlayerNotFoundException("Player with id " + testPlayerId + " not found"));
//
//        mockMvc.perform(put("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDto)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value(404))
//                .andExpect(jsonPath("$.error").value("Not Found"));
//
//        verify(playerService, times(1)).updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class));
//    }
//
//    /**
//     * Test: Player update fails validation when first name is blank.
//     * <p>
//     * Verifies that:
//     * - PUT /players/{id} with blank firstName returns 400 Bad Request
//     * - Response contains validation error for firstName field
//     * - Service updatePlayer method is never called
//     */
//    @Test
//    void testUpdatePlayer_ValidationFails() throws Exception {
//        PlayerCreationDTO invalidDto = new PlayerCreationDTO("", "Smith");
//
//        mockMvc.perform(put("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors", hasItem(containsString("firstName"))));
//
//        verify(playerService, never()).updatePlayer(any(), any());
//    }
//
//    /**
//     * Test: Player update fails validation when last name is blank.
//     * <p>
//     * Verifies that:
//     * - PUT /players/{id} with blank lastName returns 400 Bad Request
//     * - Response contains validation error for lastName field
//     * - Service updatePlayer method is never called
//     */
//    @Test
//    void testUpdatePlayer_ValidationFails_LastNameBlank() throws Exception {
//        PlayerCreationDTO invalidDto = new PlayerCreationDTO("John", "");
//
//        mockMvc.perform(put("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors", hasItem(containsString("lastName"))));
//
//        verify(playerService, never()).updatePlayer(any(), any());
//    }
//
//    /**
//     * Test: Player update fails validation when both fields exceed maximum length.
//     * <p>
//     * Verifies that:
//     * - PUT /players/{id} with both firstName and lastName exceeding 100 characters returns 400 Bad Request
//     * - Service updatePlayer method is never called
//     */
//    @Test
//    void testUpdatePlayer_ValidationFails_BothFieldsTooLong() throws Exception {
//        String longName = "a".repeat(101);
//        PlayerCreationDTO invalidDto = new PlayerCreationDTO(longName, longName);
//
//        mockMvc.perform(put("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidDto)))
//                .andExpect(status().isBadRequest());
//
//        verify(playerService, never()).updatePlayer(any(), any());
//    }
//
//    /**
//     * Test: Player update fails when request body contains malformed JSON.
//     * <p>
//     * Verifies that:
//     * - PUT /players/{id} with invalid JSON returns 400 Bad Request
//     * - Error response indicates "Malformed JSON Request"
//     * - Service updatePlayer method is never called
//     */
//    @Test
//    void testUpdatePlayer_MalformedJson() throws Exception {
//        mockMvc.perform(put("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{invalid json"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value(400))
//                .andExpect(jsonPath("$.error").value("Malformed JSON Request"));
//
//        verify(playerService, never()).updatePlayer(any(), any());
//    }
//
//    // ==================== DELETE PLAYER TESTS ====================
//
//    /**
//     * Test: Successfully deleting a player with valid UUID.
//     * <p>
//     * Verifies that:
//     * - DELETE /players/{id} with valid UUID returns 204 No Content
//     * - Service deletePlayerById method is called exactly once
//     */
//    @Test
//    void testDeletePlayer_Success() throws Exception {
//        doNothing().when(playerService).deletePlayerById(testPlayerId);
//
//        mockMvc.perform(delete("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNoContent());
//
//        verify(playerService, times(1)).deletePlayerById(testPlayerId);
//    }
//
//    /**
//     * Test: Player deletion fails when the player ID does not exist.
//     * <p>
//     * Verifies that:
//     * - DELETE /players/{id} with non-existent UUID throws PlayerNotFoundException
//     * - Response returns 404 Not Found with proper error structure
//     * - Service deletePlayerById method is called exactly once
//     */
//    @Test
//    void testDeletePlayer_NotFound() throws Exception {
//        doThrow(new PlayerNotFoundException("Player with id " + testPlayerId + " not found"))
//                .when(playerService).deletePlayerById(testPlayerId);
//
//        mockMvc.perform(delete("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value(404))
//                .andExpect(jsonPath("$.error").value("Not Found"));
//
//        verify(playerService, times(1)).deletePlayerById(testPlayerId);
//    }
//
//    // ==================== FIND PLAYER BY NAME TESTS ====================
//
//    /**
//     * Test: Successfully finding a player by first name and last name.
//     * <p>
//     * Verifies that:
//     * - GET /players/search?firstName=...&lastName=... with matching values returns 200 OK
//     * - Response body contains list with matching player
//     * - Service findPlayerByFirstNameAndLastName method is called exactly once
//     */
//    @Test
//    void testFindPlayerByFirstNameAndLastName_Success() throws Exception {
//        List<Player> players = List.of(testPlayer);
//
//        when(playerService.findPlayerByFirstNameAndLastName("John", "Doe"))
//                .thenReturn(players);
//
//        mockMvc.perform(get("/players/search?firstName=John&lastName=Doe")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].firstName").value("John"))
//                .andExpect(jsonPath("$[0].lastName").value("Doe"));
//
//        verify(playerService, times(1)).findPlayerByFirstNameAndLastName("John", "Doe");
//    }
//
//    /**
//     * Test: Finding a player by first name and last name returns empty list when no matches found.
//     * <p>
//     * Verifies that:
//     * - GET /players/search?firstName=...&lastName=... with non-matching values returns 200 OK
//     * - Response body contains empty list
//     * - Service findPlayerByFirstNameAndLastName method is called exactly once
//     */
//    @Test
//    void testFindPlayerByFirstNameAndLastName_NoResults() throws Exception {
//        when(playerService.findPlayerByFirstNameAndLastName("Unknown", "Person"))
//                .thenReturn(new ArrayList<>());
//
//        mockMvc.perform(get("/players/search?firstName=Unknown&lastName=Person")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)));
//
//        verify(playerService, times(1)).findPlayerByFirstNameAndLastName("Unknown", "Person");
//    }
//
//    /**
//     * Test: Finding a player by first name and last name returns multiple results when multiple matches exist.
//     * <p>
//     * Verifies that:
//     * - GET /players/search?firstName=...&lastName=... returns 200 OK
//     * - Response body contains list with all matching players
//     * - Service findPlayerByFirstNameAndLastName method is called exactly once
//     */
//    @Test
//    void testFindPlayerByFirstNameAndLastName_MultipleResults() throws Exception {
//        Player player2 = new Player(UUID.randomUUID(), "John", "Doe", new ArrayList<>());
//        List<Player> players = List.of(testPlayer, player2);
//
//        when(playerService.findPlayerByFirstNameAndLastName("John", "Doe"))
//                .thenReturn(players);
//
//        mockMvc.perform(get("/players/search?firstName=John&lastName=Doe")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].firstName").value("John"))
//                .andExpect(jsonPath("$[0].lastName").value("Doe"))
//                .andExpect(jsonPath("$[1].firstName").value("John"))
//                .andExpect(jsonPath("$[1].lastName").value("Doe"));
//
//        verify(playerService, times(1)).findPlayerByFirstNameAndLastName("John", "Doe");
//    }
//
//    /**
//     * Test: Successfully finding players by first name only.
//     * <p>
//     * Verifies that:
//     * - GET /players/search/firstName?firstName=... with matching values returns 200 OK
//     * - Response body contains list with all matching players
//     * - Service findPlayerByFirstName method is called exactly once
//     */
//    @Test
//    void testFindPlayerByFirstName_Success() throws Exception {
//        Player player2 = new Player(UUID.randomUUID(), "John", "Smith", new ArrayList<>());
//        List<Player> players = List.of(testPlayer, player2);
//
//        when(playerService.findPlayerByFirstName("John"))
//                .thenReturn(players);
//
//        mockMvc.perform(get("/players/search/firstName?firstName=John")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].firstName").value("John"))
//                .andExpect(jsonPath("$[1].firstName").value("John"));
//
//        verify(playerService, times(1)).findPlayerByFirstName("John");
//    }
//
//    /**
//     * Test: Finding players by first name returns empty list when no matches found.
//     * <p>
//     * Verifies that:
//     * - GET /players/search/firstName?firstName=... with non-matching values returns 200 OK
//     * - Response body contains empty list
//     * - Service findPlayerByFirstName method is called exactly once
//     */
//    @Test
//    void testFindPlayerByFirstName_NoResults() throws Exception {
//        when(playerService.findPlayerByFirstName("NonExistent"))
//                .thenReturn(new ArrayList<>());
//
//        mockMvc.perform(get("/players/search/firstName?firstName=NonExistent")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)));
//
//        verify(playerService, times(1)).findPlayerByFirstName("NonExistent");
//    }
//
//    /**
//     * Test: Successfully finding players by last name only.
//     * <p>
//     * Verifies that:
//     * - GET /players/search/lastName?lastName=... with matching values returns 200 OK
//     * - Response body contains list with all matching players
//     * - Service findPlayerByLastName method is called exactly once
//     */
//    @Test
//    void testFindPlayerByLastName_Success() throws Exception {
//        Player player2 = new Player(UUID.randomUUID(), "Jane", "Doe", new ArrayList<>());
//        List<Player> players = List.of(testPlayer, player2);
//
//        when(playerService.findPlayerByLastName("Doe"))
//                .thenReturn(players);
//
//        mockMvc.perform(get("/players/search/lastName?lastName=Doe")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].lastName").value("Doe"))
//                .andExpect(jsonPath("$[1].lastName").value("Doe"));
//
//        verify(playerService, times(1)).findPlayerByLastName("Doe");
//    }
//
//    /**
//     * Test: Finding players by last name returns empty list when no matches found.
//     * <p>
//     * Verifies that:
//     * - GET /players/search/lastName?lastName=... with non-matching values returns 200 OK
//     * - Response body contains empty list
//     * - Service findPlayerByLastName method is called exactly once
//     */
//    @Test
//    void testFindPlayerByLastName_NoResults() throws Exception {
//        when(playerService.findPlayerByLastName("NonExistent"))
//                .thenReturn(new ArrayList<>());
//
//        mockMvc.perform(get("/players/search/lastName?lastName=NonExistent")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)));
//
//        verify(playerService, times(1)).findPlayerByLastName("NonExistent");
//    }
//
//    // ==================== MULTIPLE RESULTS TESTS ====================
//
//    /**
//     * Test: Finding players by first name returns multiple results with matching first names.
//     * <p>
//     * Verifies that:
//     * - GET /players/search/firstName?firstName=... returns 200 OK
//     * - Response body contains list with all players matching the first name
//     * - All results have the same first name
//     * - Service findPlayerByFirstName method is called exactly once
//     */
//    @Test
//    void testFindPlayerByFirstName_MultipleResults() throws Exception {
//        Player player2 = new Player(UUID.randomUUID(), "John", "Smith", new ArrayList<>());
//        Player player3 = new Player(UUID.randomUUID(), "John", "Johnson", new ArrayList<>());
//        List<Player> players = List.of(testPlayer, player2, player3);
//
//        when(playerService.findPlayerByFirstName("John"))
//                .thenReturn(players);
//
//        mockMvc.perform(get("/players/search/firstName?firstName=John")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[*].firstName", everyItem(equalTo("John"))));
//
//        verify(playerService, times(1)).findPlayerByFirstName("John");
//    }
//
//    /**
//     * Test: Finding players by last name returns multiple results with matching last names.
//     * <p>
//     * Verifies that:
//     * - GET /players/search/lastName?lastName=... returns 200 OK
//     * - Response body contains list with all players matching the last name
//     * - All results have the same last name
//     * - Service findPlayerByLastName method is called exactly once
//     */
//    @Test
//    void testFindPlayerByLastName_MultipleResults() throws Exception {
//        Player player2 = new Player(UUID.randomUUID(), "Jane", "Doe", new ArrayList<>());
//        Player player3 = new Player(UUID.randomUUID(), "Bob", "Doe", new ArrayList<>());
//        List<Player> players = List.of(testPlayer, player2, player3);
//
//        when(playerService.findPlayerByLastName("Doe"))
//                .thenReturn(players);
//
//        mockMvc.perform(get("/players/search/lastName?lastName=Doe")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)))
//                .andExpect(jsonPath("$[*].lastName", everyItem(equalTo("Doe"))));
//
//        verify(playerService, times(1)).findPlayerByLastName("Doe");
//    }
//
//    /**
//     * Test: Successfully creating a player with names at maximum allowed length.
//     * <p>
//     * Verifies that:
//     * - POST /players with firstName and lastName of exactly 100 characters returns 201 Created
//     * - Response body contains player with full-length names preserved
//     * - Service createPlayer method is called exactly once
//     */
//    @Test
//    void testCreatePlayer_WithMaxLengthNames() throws Exception {
//        String maxLengthName = "a".repeat(100);
//        Player playerWithMaxNames = new Player(testPlayerId, maxLengthName, maxLengthName, new ArrayList<>());
//        PlayerCreationDTO dtoWithMaxNames = new PlayerCreationDTO(maxLengthName, maxLengthName);
//
//        when(playerService.createPlayer(any(PlayerCreationDTO.class)))
//                .thenReturn(playerWithMaxNames);
//
//        mockMvc.perform(post("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dtoWithMaxNames)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.firstName").value(maxLengthName))
//                .andExpect(jsonPath("$.lastName").value(maxLengthName));
//
//        verify(playerService, times(1)).createPlayer(any(PlayerCreationDTO.class));
//    }
//
//    /**
//     * Test: Successfully updating a player with special characters in name.
//     * <p>
//     * Verifies that:
//     * - PUT /players/{id} with special characters (José, García) returns 200 OK
//     * - Response body preserves the special characters correctly
//     * - Service updatePlayer method is called exactly once
//     */
//    @Test
//    void testUpdatePlayer_WithSpecialCharacters() throws Exception {
//        Player updatedPlayer = new Player(testPlayerId, "José", "García", new ArrayList<>());
//        PlayerCreationDTO updateDto = new PlayerCreationDTO("José", "García");
//
//        when(playerService.updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class)))
//                .thenReturn(updatedPlayer);
//
//        mockMvc.perform(put("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value("José"))
//                .andExpect(jsonPath("$.lastName").value("García"));
//
//        verify(playerService, times(1)).updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class));
//    }
//
//    /**
//     * Test: Successfully updating a player with names at maximum allowed length.
//     * <p>
//     * Verifies that:
//     * - PUT /players/{id} with firstName and lastName of exactly 100 characters returns 200 OK
//     * - Response body contains updated player with full-length names preserved
//     * - Service updatePlayer method is called exactly once
//     */
//    @Test
//    void testUpdatePlayer_WithMaxLengthNames() throws Exception {
//        String maxLengthName = "a".repeat(100);
//        Player updatedPlayer = new Player(testPlayerId, maxLengthName, maxLengthName, new ArrayList<>());
//        PlayerCreationDTO updateDto = new PlayerCreationDTO(maxLengthName, maxLengthName);
//
//        when(playerService.updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class)))
//                .thenReturn(updatedPlayer);
//
//        mockMvc.perform(put("/players/{id}", testPlayerId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value(maxLengthName))
//                .andExpect(jsonPath("$.lastName").value(maxLengthName));
//
//        verify(playerService, times(1)).updatePlayer(eq(testPlayerId), any(PlayerCreationDTO.class));
//    }
//
//    /**
//     * Test: Successfully retrieving a different player by ID using a different UUID.
//     * <p>
//     * Verifies that:
//     * - GET /players/{id} with different valid UUID returns 200 OK
//     * - Response body contains the correct player matching the requested ID
//     * - Service getPlayerById method is called exactly once with the correct UUID
//     */
//    @Test
//    void testGetPlayer_WithDifferentUUIDs() throws Exception {
//        UUID otherId = UUID.randomUUID();
//        Player otherPlayer = new Player(otherId, "Jane", "Smith", new ArrayList<>());
//
//        when(playerService.getPlayerById(otherId))
//                .thenReturn(java.util.Optional.of(otherPlayer));
//
//        mockMvc.perform(get("/players/{id}", otherId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(otherId.toString()))
//                .andExpect(jsonPath("$.firstName").value("Jane"));
//
//        verify(playerService, times(1)).getPlayerById(otherId);
//    }
//
//    /**
//     * Test: Response from retrieving a player contains all required fields.
//     * <p>
//     * Verifies that:
//     * - GET /players/{id} response contains all expected fields (id, firstName, lastName)
//     * - All fields are present in the JSON response
//     * - Fields exist with non-null values
//     */
//    @Test
//    void testGetPlayer_ResponseContainsAllFields() throws Exception {
//        when(playerService.getPlayerById(testPlayerId))
//                .thenReturn(java.util.Optional.of(testPlayer));
//
//        mockMvc.perform(get("/players/{id}", testPlayerId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.firstName").exists())
//                .andExpect(jsonPath("$.lastName").exists());
//    }
//
//    /**
//     * Test: Response from creating a player contains all required fields.
//     * <p>
//     * Verifies that:
//     * - POST /players response contains all expected fields (id, firstName, lastName)
//     * - All fields are present in the JSON response
//     * - Fields exist with non-null values
//     */
//    @Test
//    void testCreatePlayer_ResponseContainsAllFields() throws Exception {
//        when(playerService.createPlayer(any(PlayerCreationDTO.class)))
//                .thenReturn(testPlayer);
//
//        mockMvc.perform(post("/players")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(testPlayerDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.firstName").exists())
//                .andExpect(jsonPath("$.lastName").exists());
//    }
//
//    /**
//     * Test: Response from searching players by first name contains all required fields for each player.
//     * <p>
//     * Verifies that:
//     * - GET /players/search/firstName?... response contains all expected fields (id, firstName, lastName)
//     * - All fields are present in the JSON response for each player
//     * - Fields exist with non-null values
//     */
//    @Test
//    void testFindPlayerByFirstName_ResponseContainsAllFields() throws Exception {
//        List<Player> players = List.of(testPlayer);
//
//        when(playerService.findPlayerByFirstName("John"))
//                .thenReturn(players);
//
//        mockMvc.perform(get("/players/search/firstName?firstName=John"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").exists())
//                .andExpect(jsonPath("$[0].firstName").exists())
//                .andExpect(jsonPath("$[0].lastName").exists());
//    }
//
//    /**
//     * Test: All successful player creation requests return HTTP 201 Created status.
//     * <p>
//     * Verifies that:
//     * - Multiple POST /players requests with valid data consistently return 201 Created
//     * - Service createPlayer method is called 5 times
//     * - Consistency of HTTP status code across multiple requests
//     */
//    @Test
//    void testAllSuccessfulCreatesReturn201() throws Exception {
//        when(playerService.createPlayer(any(PlayerCreationDTO.class)))
//                .thenReturn(testPlayer);
//
//        for (int i = 0; i < 5; i++) {
//            mockMvc.perform(post("/players")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(testPlayerDto)))
//                    .andExpect(status().isCreated());
//        }
//
//        verify(playerService, times(5)).createPlayer(any(PlayerCreationDTO.class));
//    }
//
//    /**
//     * Test: All successful player retrieval requests return HTTP 200 OK status.
//     * <p>
//     * Verifies that:
//     * - Multiple GET /players/{id} requests with valid IDs consistently return 200 OK
//     * - Service getPlayerById method is called 5 times
//     * - Consistency of HTTP status code across multiple requests
//     */
//    @Test
//    void testAllSuccessfulReadsReturn200() throws Exception {
//        when(playerService.getPlayerById(testPlayerId))
//                .thenReturn(java.util.Optional.of(testPlayer));
//
//        for (int i = 0; i < 5; i++) {
//            mockMvc.perform(get("/players/{id}", testPlayerId))
//                    .andExpect(status().isOk());
//        }
//
//        verify(playerService, times(5)).getPlayerById(testPlayerId);
//    }
//
//    /**
//     * Test: All successful player deletion requests return HTTP 204 No Content status.
//     * <p>
//     * Verifies that:
//     * - Multiple DELETE /players/{id} requests with valid IDs consistently return 204 No Content
//     * - Service deletePlayerById method is called 5 times
//     * - Consistency of HTTP status code across multiple requests
//     */
//    @Test
//    void testAllSuccessfulDeletesReturn204() throws Exception {
//        doNothing().when(playerService).deletePlayerById(testPlayerId);
//
//        for (int i = 0; i < 5; i++) {
//            mockMvc.perform(delete("/players/{id}", testPlayerId))
//                    .andExpect(status().isNoContent());
//        }
//
//        verify(playerService, times(5)).deletePlayerById(testPlayerId);
//    }
//
//    // ==================== GET GAMES FOR PLAYER TESTS ====================
//
//    /**
//     * Test: Successfully retrieving all games for a player with multiple games.
//     * <p>
//     * Verifies that:
//     * - GET /players/{playerId}/games with valid UUID returns 200 OK
//     * - Response body contains list with all games for the player
//     * - Service getGamesForPlayer method is called exactly once
//     */
//    @Test
//    void testGetGamesForPlayer_Success() throws Exception {
//        UUID gameId1 = UUID.randomUUID();
//        UUID gameId2 = UUID.randomUUID();
//        Game game1 = new Game(gameId1, "Game 1", List.of(testPlayer));
//        Game game2 = new Game(gameId2, "Game 2", List.of(testPlayer));
//        List<Game> games = List.of(game1, game2);
//
//        when(playerService.getGamesForPlayer(testPlayerId)).thenReturn(games);
//
//        mockMvc.perform(get("/players/{playerId}/games", testPlayerId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id").value(gameId1.toString()))
//                .andExpect(jsonPath("$[1].id").value(gameId2.toString()));
//
//        verify(playerService, times(1)).getGamesForPlayer(testPlayerId);
//    }
//
//    /**
//     * Test: Retrieving games for a player fails when the player ID does not exist.
//     * <p>
//     * Verifies that:
//     * - GET /players/{playerId}/games with non-existent UUID throws PlayerNotFoundException
//     * - Response returns 404 Not Found with proper error structure
//     * - Service getGamesForPlayer method is called exactly once
//     */
//    @Test
//    void testGetGamesForPlayer_PlayerNotFound() throws Exception {
//        when(playerService.getGamesForPlayer(testPlayerId))
//                .thenThrow(new PlayerNotFoundException("Player not found"));
//
//        mockMvc.perform(get("/players/{playerId}/games", testPlayerId))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value(404))
//                .andExpect(jsonPath("$.error").value("Not Found"));
//
//        verify(playerService, times(1)).getGamesForPlayer(testPlayerId);
//    }
//
//    /**
//     * Test: Retrieving games for a player returns empty list when player has no games.
//     * <p>
//     * Verifies that:
//     * - GET /players/{playerId}/games with valid UUID but no games returns 200 OK
//     * - Response body contains empty list
//     * - Service getGamesForPlayer method is called exactly once
//     */
//    @Test
//    void testGetGamesForPlayer_EmptyGames() throws Exception {
//        when(playerService.getGamesForPlayer(testPlayerId)).thenReturn(List.of());
//
//        mockMvc.perform(get("/players/{playerId}/games", testPlayerId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(0)));
//
//        verify(playerService, times(1)).getGamesForPlayer(testPlayerId);
//    }
//
//    /**
//     * Test: Successfully retrieving a single game for a player.
//     * <p>
//     * Verifies that:
//     * - GET /players/{playerId}/games with valid UUID and single game returns 200 OK
//     * - Response body contains list with one game
//     * - Service getGamesForPlayer method is called exactly once
//     */
//    @Test
//    void testGetGamesForPlayer_SingleGame() throws Exception {
//        UUID gameId = UUID.randomUUID();
//        Game game = new Game(gameId, "Test Game", List.of(testPlayer));
//        List<Game> games = List.of(game);
//
//        when(playerService.getGamesForPlayer(testPlayerId)).thenReturn(games);
//
//        mockMvc.perform(get("/players/{playerId}/games", testPlayerId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].id").value(gameId.toString()));
//
//        verify(playerService, times(1)).getGamesForPlayer(testPlayerId);
//    }
//}
//
//