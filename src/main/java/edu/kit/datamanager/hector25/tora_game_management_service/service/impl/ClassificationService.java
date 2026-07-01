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

package edu.kit.datamanager.hector25.tora_game_management_service.service.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.dao.IClassificationDao;
import edu.kit.datamanager.hector25.tora_game_management_service.dao.IImageDao;
import edu.kit.datamanager.hector25.tora_game_management_service.dao.ISessionDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Classification;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Image;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Session;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IClassificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClassificationService implements IClassificationService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);
    private final IClassificationDao classificationDao;
    private final ISessionDao sessionDao;
    private final IImageDao imageDao;

    public ClassificationService(IClassificationDao classificationDao, ISessionDao sessionDao,  IImageDao imageDao) {
        this.classificationDao = classificationDao;
        this.sessionDao = sessionDao;
        this.imageDao = imageDao;
    }

    @Override
    public Classification createClassification(UUID imageId, Boolean decorated, UUID sessionId) {
        LOG.info("createdClassification");

        Session session = sessionDao.findSessionById(sessionId).orElseThrow();
        Image image = imageDao.findById(imageId).orElseThrow();

        if (image.isDecorated() == null){
            return classificationDao.save(new Classification(image, decorated, session));
        } else {
            return classificationDao.save(new Classification(image, decorated, session, image.isDecorated() == decorated));
        }
    }

    @Override
    public Classification createBadDatasetClassification(UUID imagedId, UUID sessionId){
        LOG.info("createBadDatasetClassification");
        Session session = sessionDao.findSessionById(sessionId).orElseThrow();
        Image image = imageDao.findById(imagedId).orElseThrow();

        return classificationDao.save(new Classification(image, session, true));
    }

    @Override
    public Optional<Classification> findClassificationById(UUID id) {
        return classificationDao.findClassificationById(id);
    }

    @Override
    public List<Classification> findClassificationsForImage(UUID imageId) {
        return classificationDao.findClassificationsByImageId(imageId);
    }

    @Override
    public List<Classification> findClassificationsOfPlayer(UUID playerId, Pageable pageable) {
        return classificationDao.findClassificationsByPlayerId(playerId, pageable);
    }

    @Override
    public List<Classification> findClassificationsForSession(UUID sessionId) {
        return classificationDao.findClassificationsBySessionId(sessionId);
    }

    @Override
    public List<Classification> findOtherPlayersClassification(UUID playerId, Pageable pageable) {
        return classificationDao.findClassificationForPlayer(playerId, pageable);
    }

    public Optional<Boolean> generatePlayerConfidences(UUID playerId) {

        List<Classification> classificationsNeedingConfidences = classificationDao.findClassificationsNeedingConfidencesByPlayerId(playerId);

        if (classificationsNeedingConfidences.isEmpty()){
            return Optional.of(false);
        }
        LocalDateTime oldestClassification = classificationsNeedingConfidences.getFirst().getCreatedAt();

        List<Classification> relevantTestClassifications = classificationDao.findTestsByPlayerAfter(playerId, oldestClassification.minusDays(60));

        if (relevantTestClassifications.size() < 10) {
            return Optional.of(false);
        }
        if (relevantTestClassifications.get(relevantTestClassifications.size() - 10).getCreatedAt().isBefore(LocalDateTime.now().minusDays(30))) {
            return Optional.of(false); //if there are not enough recent tests false is returned, leading to the Controller sending more tests to the user
        }

        for (Classification classification : classificationsNeedingConfidences) {
            double weighedCorrectTests = 0;
            double weighedWrongTests = 0;

            for (Classification testClassification : relevantTestClassifications) {
                long timeDifference = Math.abs(Duration.between(classification.getCreatedAt(), testClassification.getCreatedAt()).toMinutes());
                if (timeDifference > 60*24*60) {
                    continue; //Don't take tests into account which are farther than 60 Days from the classification
                }
                double weight = Math.exp(-2e-5 * timeDifference);

                if (testClassification.getCorrect()) {
                    weighedCorrectTests += weight;
                }else  {
                    weighedWrongTests += weight;
                }
            }

            double confidence = weighedCorrectTests / (weighedCorrectTests + weighedWrongTests);
            classification.setConfidence(confidence);
        }
        classificationDao.saveAll(classificationsNeedingConfidences);
        return Optional.of(true);
    }


}
