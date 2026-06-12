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

package edu.kit.datamanager.hector25.tora_game_management_service.web;

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.PlayerCreationDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.SessionLoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/players")
@Tag(name = "Player API", description = "This API endpoint manages the players of the game.")
@Validated
public interface IPlayerAPI {

    /**
     * Creates a new player with the given first, last and user name.
     *
     * @param playerCreationDTO The DTO containing the player's first, last and username.
     * @return A ResponseEntity with the created player and HTTP status 201 (Created).
     */
    @Operation(
            summary = "Create a new player",
            description = "Creates a new player with the given first and last name.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created a new player",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    ResponseEntity<@NonNull Player> createPlayer(@Valid @RequestBody PlayerCreationDTO playerCreationDTO);

    /**
     * Updates an existing player's first, last and user name.
     *
     * @param sessionId                The current session id.
     * @param playerCreationDTO The DTO containing the new first, last and user name.
     * @return A ResponseEntity with the updated player if found (HTTP 200),
     * or 404 if the player doesn't exist.
     */
    @Operation(
            summary = "Update an existing player",
            description = "Updates the first, last and user name of an existing player.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated the player",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class))),
                    @ApiResponse(responseCode = "404", description = "Player not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PutMapping("/{sessionId}")
    ResponseEntity<@NonNull Player> updatePlayer(
            @Valid @PathVariable UUID sessionId,
            @Valid @RequestBody PlayerCreationDTO playerCreationDTO
    );

    /**
     * Deletes the player the current session is assigned to.
     *
     * @param id The session id of the player to delete.
     * @return A ResponseEntity with HTTP status 204 (No Content) if successful,
     * or 404 if the player doesn't exist.
     */
    @Operation(
            summary = "Delete a player",
            description = "Deletes a player with the specified session id.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully deleted the player"),
                    @ApiResponse(responseCode = "404", description = "Player not found")
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<@NonNull Void> deletePlayer(@Valid @PathVariable UUID id);

    /**
     * Logs into a new session for the given player
     *
     * @param playerCreationDTO The DTO containing the three names of the player
     *
     * @return A ResponseEntity with the new session id and the game state of the player if found (HTTP 200), or 404 if the player doesn't exist.
     */
    @Operation(
            summary = "Log into session",
            description = "Logs into a new session for the specified player",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully logged into new session",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SessionLoginDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Player not found")
            }
    )
    @PostMapping("/login")
    ResponseEntity<SessionLoginDTO> logInPlayer(
            @Valid @RequestBody PlayerCreationDTO playerCreationDTO
    );

    /**
     * Updates the players game state on the server
     *
     * @param sessionId The session in which the player currently is
     * @param gameState The game state of the player saved by the game as a string
     *
     * @return HTTP 204 if the session is valid, 404 if the session doesn't exist
     */
    @Operation(
            summary = "Update the players game state",
            description = "Save a new game state on the server",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully updated game state"),
                    @ApiResponse(responseCode = "404", description = "Pass an existing session")
            }
    )
    @PostMapping("/{sessionId}")
    ResponseEntity<Void> updateGamestate(@PathVariable("sessionId") UUID sessionId,
                                         @RequestBody @Valid String gameState);



}
