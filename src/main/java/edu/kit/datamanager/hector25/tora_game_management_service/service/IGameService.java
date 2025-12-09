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

package edu.kit.datamanager.hector25.tora_game_management_service.service;

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Game;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.GameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IGameService {
    /**
     * Creates a new game with the specified name and players.
     *
     * @param name      The name of the game
     * @param playerIds The list of player IDs for the game
     * @return The created game
     */
    Game createGame(String name, List<UUID> playerIds);

    /**
     * Updates a game with the specified ID.
     *
     * @param id        The ID of the game to update
     * @param name      The new name of the game
     * @param playerIds The list of new player IDs
     * @return The updated game
     * @throws GameNotFoundException if the game is not found
     */
    Game updateGame(UUID id, String name, List<UUID> playerIds) throws GameNotFoundException;

    /**
     * Retrieves a game by its ID.
     *
     * @param gameId The ID of the game
     * @return An Optional containing the game if found, empty otherwise
     */
    Optional<Game> getGameById(UUID gameId);

    /**
     * Retrieves all games.
     *
     * @return A list of all games
     */
    List<Game> getAllGames();

    /**
     * Deletes a game with the specified ID.
     *
     * @param gameId The ID of the game to delete
     * @throws GameNotFoundException if the game is not found
     */
    void deleteGameById(UUID gameId) throws GameNotFoundException;

    /**
     * Retrieves all players for a specific game.
     *
     * @param gameId The ID of the game
     * @return A list of players in the game
     * @throws GameNotFoundException if the game is not found
     */
    List<Player> getPlayersForGame(UUID gameId) throws GameNotFoundException;

    /**
     * Adds a player to a game.
     *
     * @param gameId   The ID of the game
     * @param playerId The ID of the player to add
     * @return The updated game
     * @throws GameNotFoundException if the game is not found
     */
    Game addPlayerToGame(UUID gameId, UUID playerId) throws GameNotFoundException;

    /**
     * Removes a player from a game.
     *
     * @param gameId   The ID of the game
     * @param playerId The ID of the player to remove
     * @return The updated game
     * @throws GameNotFoundException if the game is not found
     */
    Game removePlayerFromGame(UUID gameId, UUID playerId) throws GameNotFoundException;
}

