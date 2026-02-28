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
 * This is a Spring Data JPA repository interface for managing Image entities.
 */
@Repository
public interface IImageDao extends JpaRepository<@NonNull Image, @NonNull UUID> {
    /**
     * Finds an Image by their unique identifier.
     *
     * @param id The UUID of the Image.
     * @return An Optional containing the Image if found, or empty if not found.
     */
    Optional<Image> findImageById(UUID id);

    /**
     * Finds images being decorated or not.
     *
     * @param decorated Whether the Images returned should be decorated.
     * @return A list of Images with the specified Classification.
     */
    List<Image> findImagesByDecorated(Boolean decorated);

    @Query("""
            SELECT i FROM Image i
            WHERE i.decorated = :decorated
            AND NOT EXISTS (
                SELECT c FROM Classification c
                WHERE c.image = i
                AND c.session.player.id = :playerId
            )
            """)
    List<Image> findTestImageForPlayer(Boolean decorated,  UUID playerId, Pageable pageable);

    /**
     * Finds an Image that wasn't qualified by the player and of which we don't know if they're decorated
     *
     * @param playerId The id of the player images are searched for
     * @return The first image the DB finds
     */

    @Query("""
            SELECT i FROM Image i
            WHERE i.decorated IS NULL
            AND (SELECT COUNT(cc) FROM Classification cc WHERE cc.image = i) = 0
            AND NOT EXISTS (
                SELECT c FROM Classification c
                WHERE c.image = i
                AND c.session.player.id = :playerId
            )
            """)
    List<Image> findFirstUnusedByPlayer(UUID playerId, Pageable pageable);
}