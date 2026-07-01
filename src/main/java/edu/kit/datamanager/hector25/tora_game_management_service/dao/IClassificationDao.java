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
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import org.jspecify.annotations.NonNull;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    /**
     * Finds all classifications of a player
     * @param playerId The PlayerId for whom to return classifications
     * @return A list of the classifications
     */
    @Query("""
            SELECT c FROM Classification c
            WHERE c.session.player.id = :playerId""")
    List<Classification> findClassificationsByPlayerId(UUID playerId,  Pageable pageable);

    /**
     * Finds a classification by another player for the given player to also classify the same image
     *
     * @param playerId The player id
     * @param pageable The page to return; 1,1 for first item
     * @return The Classification, if available
     */
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

    /**
     * Enables the confidence service to find all Classifications of a player which don't have a final confidence set
     * @param playerId The Players Id
     * @param confidenceIsFinal Whether the confidence is final
     * @return The List of classifications
     */
    @Query("""
            SELECT c FROM Classification c
            WHERE c.confidenceIsFinal = :confidenceIsFinal
            AND c.session.player.id = :playerId
            ORDER BY c.createdAt asc""")

    List<Classification> findClassificationsByPlayerIdAndConfidenceIsFinal(UUID playerId, boolean confidenceIsFinal);

    /***
     * Finds all Classifications of a player that need confidences generated, i.e. they don't have a final confidence set, and they aren't tests
     * @param playerId The ID of the player we need to generate confidences for
     * @return A list of the Classifications Found
     */
    @Query("""
            SELECT c from Classification c
            WHERE c.confidenceIsFinal = false
            AND c.correct IS NULL
            AND c.session.player.id = :playerId
            ORDER BY c.createdAt asc""")
    List<Classification> findClassificationsNeedingConfidencesByPlayerId(UUID playerId);

    /**
     * Finds all classifications the player made in a given year
     * @param playerId The players id
     * @param year the year to search for
     * @return All classifications of the player in the year
     */
    @Query("""
            SELECT c FROM Classification c
            WHERE c.session.player.id = :playerId
            AND year(c.createdAt) = :year
            order by c.createdAt asc""")
    List<Classification> findClassificationsByPlayerIdAndYear(UUID playerId, @Param("year") int year);

    @Query("""
            SELECT avg(c.confidence), month(c.createdAt), day(c.createdAt) FROM Classification c
            WHERE c.session.player.id = :playerId
            AND year(c.createdAt) = :year
            AND c.confidence IS NOT NULL
            AND c.correct IS NULL
            GROUP BY month(c.createdAt), day(c.createdAt)
            order by month(c.createdAt), day(c.createdAt) asc""")
    List<Object[]> findAvgByYearByPlayer(@Param("year") int year, UUID playerId);

    @Query("""
            SELECT c FROM Classification c
            WHERE c.session.player.id = :playerId
            AND c.createdAt > :localDateTime
            AND c.correct IS NOT NULL
            ORDER BY c.createdAt""")
    List<Classification> findTestsByPlayerAfter(UUID playerId, LocalDateTime localDateTime);
}