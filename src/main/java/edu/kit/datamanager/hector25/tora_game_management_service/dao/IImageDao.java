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
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * This is a Spring Data JPA repository interface for managing Image entities.
 */
@Repository
public interface IImageDao extends CrudRepository<@NonNull Image, @NonNull UUID> {
    /**
     * Finds an Image by their unique identifier.
     *
     * @param id The UUID of the Image.
     * @return An Optional containing the Image if found, or empty if not found.
     */
    Optional<Image> findImageById(UUID id);

    /**
     * Finds all players by their first name.
     *
     * @param decorated Whether the Images returned should be decorated.
     * @return A list of Images with the specified Classification.
     */
    List<Image> findImagesByDecorated(Boolean decorated);
}