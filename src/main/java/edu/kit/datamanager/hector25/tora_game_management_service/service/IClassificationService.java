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

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Classification;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Image;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import jdk.jfr.Description;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IClassificationService {

    Classification createClassification(UUID imageId, Boolean decorated, UUID sessionId);

    Optional<Classification> findClassificationById(UUID id);

    List<Classification> findClassificationsForImage(UUID imageId);

    List<Classification> findClassificationsOfPlayer(UUID playerId, Pageable pageable);

    List<Classification> findClassificationsForSession(UUID sessionId);

    List<Classification> findOtherPlayersClassification(UUID playerId, Pageable pageable);

    /**
     * Generates the confidence values for all classifications which don't have a final confidence set,
     *                  i.e. they are younger than 60 days. The function only returns an Optional containing false if
     *                  there has not been a test image in the last 30 days to make sure that there are always enough
     *                  tests to generate a valuable confidence.
     */
    Optional<Boolean> generatePlayerConfidences(UUID playerId);





}
