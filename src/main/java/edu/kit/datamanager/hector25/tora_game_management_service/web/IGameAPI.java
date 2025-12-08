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

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Game;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/games")
@Tag(name = "Game API", description = "This API endpoint manages the games.")
@Validated
public interface IGameAPI {

    /**
     * Creates a new game with the given players.
     *
     * @param playerIds The list of player IDs for the game.
     * @return A ResponseEntity with the created game and HTTP status 201 (Created).
     */
    @Operation(
            summary = "Create a new game",
            description = "Creates a new game with the given players.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created a new game",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Game.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    ResponseEntity<@NonNull Game> createGame(@Valid @RequestBody @NotNull List<UUID> playerIds);

    /**
     * Retrieves a game by its UUID.
     *
     * @param id The UUID of the game.
     * @return A ResponseEntity with the game if found (HTTP 200), or 404 if not found.
     */
    @Operation(
            summary = "Get a game by ID",
            description = "Retrieves a game with the specified UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the game",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Game.class))),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<@NonNull Game> getGame(@Valid @PathVariable UUID id);

    /**
     * Retrieves all games.
     *
     * @return A ResponseEntity with a list of all games.
     */
    @Operation(
            summary = "Get all games",
            description = "Retrieves all games.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all games",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Game.class)))
            }
    )
    @GetMapping
    ResponseEntity<@NonNull List<@NonNull Game>> getAllGames();

    /**
     * Updates an existing game's players.
     *
     * @param id        The UUID of the game to update.
     * @param playerIds The list of new player IDs.
     * @return A ResponseEntity with the updated game if found (HTTP 200),
     * or 404 if the game doesn't exist.
     */
    @Operation(
            summary = "Update an existing game",
            description = "Updates the players of an existing game.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated the game",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Game.class))),
                    @ApiResponse(responseCode = "404", description = "Game not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PutMapping("/{id}")
    ResponseEntity<@NonNull Game> updateGame(
            @Valid @PathVariable UUID id,
            @Valid @RequestBody List<UUID> playerIds
    );

    /**
     * Deletes a game with the specified UUID.
     *
     * @param id The UUID of the game to delete.
     * @return A ResponseEntity with HTTP status 204 (No Content) if successful,
     * or 404 if the game doesn't exist.
     */
    @Operation(
            summary = "Delete a game",
            description = "Deletes a game with the specified UUID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully deleted the game"),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<@NonNull Void> deleteGame(@Valid @PathVariable UUID id);

    /**
     * Retrieves all players for a specific game.
     *
     * @param gameId The UUID of the game.
     * @return A ResponseEntity with a list of players for the game if found (HTTP 200),
     * or 404 if the game doesn't exist.
     */
    @Operation(
            summary = "Get players for a game",
            description = "Retrieves all players in the specified game.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved players",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class))),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            }
    )
    @GetMapping("/{gameId}/players")
    ResponseEntity<@NonNull List<@NonNull Player>> getPlayersForGame(@Valid @PathVariable UUID gameId);

    /**
     * Adds a player to a game.
     *
     * @param gameId   The UUID of the game.
     * @param playerId The UUID of the player to add.
     * @return A ResponseEntity with the updated game if found (HTTP 200),
     * or 404 if the game doesn't exist.
     */
    @Operation(
            summary = "Add a player to a game",
            description = "Adds a player to the specified game.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully added player to game",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Game.class))),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            }
    )
    @PostMapping("/{gameId}/players/{playerId}")
    ResponseEntity<@NonNull Game> addPlayerToGame(
            @Valid @PathVariable UUID gameId,
            @Valid @PathVariable UUID playerId
    );

    /**
     * Removes a player from a game.
     *
     * @param gameId   The UUID of the game.
     * @param playerId The UUID of the player to remove.
     * @return A ResponseEntity with the updated game if found (HTTP 200),
     * or 404 if the game doesn't exist.
     */
    @Operation(
            summary = "Remove a player from a game",
            description = "Removes a player from the specified game.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed player from game",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Game.class))),
                    @ApiResponse(responseCode = "404", description = "Game not found")
            }
    )
    @DeleteMapping("/{gameId}/players/{playerId}")
    ResponseEntity<@NonNull Game> removePlayerFromGame(
            @Valid @PathVariable UUID gameId,
            @Valid @PathVariable UUID playerId
    );
}

