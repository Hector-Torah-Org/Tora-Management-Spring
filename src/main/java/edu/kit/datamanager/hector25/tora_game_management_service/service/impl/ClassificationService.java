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
        LOG.info("createClassification");

        Session session = sessionDao.findSessionById(sessionId).orElseThrow();
        Image image = imageDao.findById(imageId).orElseThrow();

        if (image.isDecorated() == null){
            return classificationDao.save(new Classification(image, decorated, session));
        } else {
            return classificationDao.save(new Classification(image, decorated, session, image.isDecorated() == decorated));
        }


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

    @Override
    public Optional<Boolean> generatePlayerConfidences(UUID playerId){ //returns false if there are not enough tests ore if tests are too old
        List<Classification> classifications = classificationDao.findClassificationsByPlayerIdAndConfidenceIsFinal(playerId, false);

        int lastIndexWithEnoughTests = 0;
        int counter = 0;
        for (int i = classifications.size() - 1; i >= 0 ; i--) {  //finding the last classification after
            if (classifications.get(i).getCorrect() != null) {    //which we still find 10 test to
                counter++;                                        //accurately generate a confidence
                if (counter == 10){
                    if (classifications.get(i).getCreatedAt().isBefore(LocalDateTime.now().minusDays(30))) return Optional.of(false);
                    lastIndexWithEnoughTests = i;
                    break;
                }
                if (counter == 1 && classifications.get(i).getCreatedAt().isBefore(LocalDateTime.now().minusDays(30))) return Optional.of(false);
            }
        }
        if (counter < 10) {
            return Optional.of(false);
        }

        for (int i = 0; i < lastIndexWithEnoughTests; i++) {
            Classification classification = classifications.get(i);
            if (classification.getCorrect() == null){ //only generate confidence for classifications which are not tests
                double confidence;
                double weighedCountOfTests = 0;
                double weighedCountOfCorrectTests = 0;
                LocalDateTime classificationCreatedAt = classification.getCreatedAt();

                for (Classification testClassification : classifications) {
                    if (testClassification.getCorrect() != null) {
                        double timeBetween = Math.abs(Duration.between(classificationCreatedAt, testClassification.getCreatedAt()).toMinutes());
                        if (timeBetween <= Duration.ofDays(60).toMinutes()) { //only view test of last/next 60 days
                            double weight = Math.exp(- 6.94e-5 * timeBetween);
                            weighedCountOfTests += weight;
                            if (testClassification.getCorrect() == true) {
                                weighedCountOfCorrectTests += weight;
                            }
                        }
                    }
                }

                confidence = weighedCountOfCorrectTests / weighedCountOfTests;
                classification.setConfidence(confidence);
                classificationDao.save(classification);
            }
        }
        return Optional.empty();
    }


}
