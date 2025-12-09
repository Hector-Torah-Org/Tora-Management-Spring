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
import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.PlayerNotFoundException;
import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.PlayerCreationDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPlayerService {
    /**
     * Creates a new player with the specified first and last name.
     *
     * @param playerCreationDTO The player data transfer object containing first and last name
     * @return The created player
     */
    Player createPlayer(PlayerCreationDTO playerCreationDTO);

    /**
     * Updates a player with the specified ID.
     *
     * @param id                The ID of the player to update
     * @param playerCreationDTO The player data transfer object containing new first and last name
     * @return The updated player
     * @throws PlayerNotFoundException if the player is not found
     */
    Player updatePlayer(UUID id, PlayerCreationDTO playerCreationDTO) throws PlayerNotFoundException;

    List<Player> findPlayerByFirstNameAndLastName(String firstName, String lastName);

    List<Player> findPlayerByFirstName(String firstName);

    List<Player> findPlayerByLastName(String lastName);

    Optional<Player> getPlayerById(UUID playerId);

    /**
     * Deletes a player with the specified ID.
     *
     * @param playerId The ID of the player to delete
     * @throws PlayerNotFoundException if the player is not found
     */
    void deletePlayerById(UUID playerId) throws PlayerNotFoundException;

    /**
     * Retrieves all games for a specific player.
     *
     * @param playerId The ID of the player
     * @return A list of games containing the player
     * @throws PlayerNotFoundException if the player is not found
     */
    List<Game> getGamesForPlayer(UUID playerId) throws PlayerNotFoundException;

    /**
     * Retrieves all players.
     *
     * @return A list of all players
     */
    List<Player> getAllPlayers();
}
