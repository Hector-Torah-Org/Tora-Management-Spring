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

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Classification;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Image;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This is a Spring Data JPA repository interface for managing Classification entities.
 */
@Repository
public interface IClassificationDao extends JpaRepository<@NonNull Classification, @NonNull UUID> {
    /**
     * Finds a player by their unique identifier.
     *
     * @param id The UUID of the classification.
     * @return An Optional containing the classification if found, or empty if not found.
     */
    Optional<Classification> findClassificationById(UUID id);

    /**
     * Finds all classifications regarding a certain image.
     *
     * @param imageId The image which the Classifications are about.
     * @return A list of classifications about the image.
     */
    List<Classification> findClassificationsByImageId(UUID imageId);

    List<Classification> findClassificationsBySessionId(UUID sessionId);

    @Query("""
            SELECT c FROM Classification c
            WHERE
              c.image.decorated IS NULL
              AND (SELECT COUNT(cc) FROM Classification cc WHERE cc.image = c.image) < 5
              AND NOT EXISTS (
                  SELECT cc2 FROM Classification cc2
                  WHERE cc2.image = c.image
                  AND cc2.session.player.id = :playerId
              )
          """)
    List<Classification> findClassificationForPlayer(UUID playerId, Pageable pageable);
}