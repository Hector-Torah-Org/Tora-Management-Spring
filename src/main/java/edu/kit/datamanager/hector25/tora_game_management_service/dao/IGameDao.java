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

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Game;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * This is a Spring Data JPA repository interface for managing Game entities.
 * It extends the CrudRepository interface, providing CRUD operations for Game objects.
 * The repository uses UUID as the identifier type for Game entities.
 * It includes a method to find a Game by its unique identifier.
 * This interface is dynamically implemented by Spring Data JPA at runtime.
 */
@Repository
public interface IGameDao extends CrudRepository<@NonNull Game, @NonNull UUID> {
    Optional<Game> findGameById(UUID id);
}
