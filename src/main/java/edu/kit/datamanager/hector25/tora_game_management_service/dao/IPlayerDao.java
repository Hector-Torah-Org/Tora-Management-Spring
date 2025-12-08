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

package edu.kit.datamanager.hector25.tora_game_management_service.dao;

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This is a Spring Data JPA repository interface for managing Player entities.
 */
@Repository
public interface IPlayerDao extends CrudRepository<@NonNull Player, @NonNull UUID> {
    /**
     * Finds a player by their unique identifier.
     *
     * @param id The UUID of the player.
     * @return An Optional containing the player if found, or empty if not found.
     */
    Optional<Player> findPlayerById(UUID id);

    /**
     * Finds all players by their first name.
     *
     * @param firstName The first name of the players to search for.
     * @return A list of players with the specified first name.
     */
    List<Player> findPlayersByFirstName(String firstName);

    /**
     * Finds all players by their last name.
     *
     * @param lastName The last name of the players to search for.
     * @return A list of players with the specified last name.
     */
    List<Player> findPlayersByLastName(String lastName);

    /**
     * Finds all players by their first and last name.
     *
     * @param firstName The first name of the players to search for.
     * @param lastName  The last name of the players to search for.
     * @return A list of players with the specified first and last name.
     */
    List<Player> findPlayerByFirstNameAndLastName(String firstName, String lastName);
}
