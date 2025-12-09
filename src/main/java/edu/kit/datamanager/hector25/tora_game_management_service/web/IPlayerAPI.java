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
import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.PlayerCreationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/players")
@Tag(name = "Player API", description = "This API endpoint manages the players of the game.")
@Validated
public interface IPlayerAPI {

    /**
     * Creates a new player with the given first and last name.
     *
     * @param playerCreationDTO The DTO containing the player's first and last name.
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
     * Retrieves a player by their UUID.
     *
     * @param id The UUID of the player.
     * @return A ResponseEntity with the player if found (HTTP 200), or 404 if not found.
     */
    @Operation(
            summary = "Get a player by ID",
            description = "Retrieves a player with the specified UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the player",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class))),
                    @ApiResponse(responseCode = "404", description = "Player not found")
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<@NonNull Player> getPlayer(@Valid @PathVariable UUID id);

    /**
     * Retrieves all players matching the specified first and last name.
     *
     * @param firstName The first name to search for.
     * @param lastName  The last name to search for.
     * @return A ResponseEntity with a list of matching players.
     */
    @Operation(
            summary = "Get players by first name and last name",
            description = "Retrieves all players matching the specified first and last name.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved players",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @GetMapping("/search")
    ResponseEntity<@NonNull List<Player>> findPlayerByFirstNameAndLastName(
            @RequestParam @NotBlank @Size(min = 2, max = 100) String firstName,
            @RequestParam @NotBlank @Size(min = 2, max = 100) String lastName
    );

    /**
     * Retrieves all players with the specified first name.
     *
     * @param firstName The first name to search for.
     * @return A ResponseEntity with a list of matching players.
     */
    @Operation(
            summary = "Get players by first name",
            description = "Retrieves all players with the specified first name.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved players",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @GetMapping("/search/firstName")
    ResponseEntity<@NonNull List<Player>> findPlayerByFirstName(@RequestParam @NotBlank @Size(min = 2, max = 100) String firstName);

    /**
     * Retrieves all players with the specified last name.
     *
     * @param lastName The last name to search for.
     * @return A ResponseEntity with a list of matching players.
     */
    @Operation(
            summary = "Get players by last name",
            description = "Retrieves all players with the specified last name.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved players",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @GetMapping("/search/lastName")
    ResponseEntity<@NonNull List<Player>> findPlayerByLastName(@RequestParam @NotBlank @Size(min = 2, max = 100) String lastName);

    /**
     * Updates an existing player's first and last name.
     *
     * @param id                The UUID of the player to update.
     * @param playerCreationDTO The DTO containing the new first and last name.
     * @return A ResponseEntity with the updated player if found (HTTP 200),
     * or 404 if the player doesn't exist.
     */
    @Operation(
            summary = "Update an existing player",
            description = "Updates the first and last name of an existing player.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated the player",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class))),
                    @ApiResponse(responseCode = "404", description = "Player not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PutMapping("/{id}")
    ResponseEntity<@NonNull Player> updatePlayer(
            @Valid @PathVariable UUID id,
            @Valid @RequestBody PlayerCreationDTO playerCreationDTO
    );

    /**
     * Deletes a player with the specified UUID.
     *
     * @param id The UUID of the player to delete.
     * @return A ResponseEntity with HTTP status 204 (No Content) if successful,
     * or 404 if the player doesn't exist.
     */
    @Operation(
            summary = "Delete a player",
            description = "Deletes a player with the specified UUID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully deleted the player"),
                    @ApiResponse(responseCode = "404", description = "Player not found")
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<@NonNull Void> deletePlayer(@Valid @PathVariable UUID id);

    /**
     * Retrieves all games for a specific player.
     *
     * @param playerId The UUID of the player.
     * @return A ResponseEntity with a list of games for the player if found (HTTP 200),
     * or 404 if the player doesn't exist.
     */
    @Operation(
            summary = "Get games for a player",
            description = "Retrieves all games that contain the specified player.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved games",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Game.class))),
                    @ApiResponse(responseCode = "404", description = "Player not found")
            }
    )
    @GetMapping("/{playerId}/games")
    ResponseEntity<@NonNull List<@NonNull Game>> getGamesForPlayer(@Valid @PathVariable UUID playerId);

    /**
     * Retrieves all players.
     *
     * @return A ResponseEntity with a list of all players.
     */
    @Operation(
            summary = "Get all players",
            description = "Retrieves all players.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all players",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Player.class)))
            }
    )
    @GetMapping
    ResponseEntity<@NonNull List<@NonNull Player>> getAllPlayers();
}
