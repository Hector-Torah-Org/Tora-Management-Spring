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
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public List<Classification> findClassificationsOfPlayer(UUID playerId) {
        List<Session> sessions = sessionDao.findSessionsByPlayerId(playerId);
        List<Classification> classifications = new ArrayList<>();
        for(Session session : sessions) {
            classifications.addAll(classificationDao.findClassificationsBySessionId(session.getSessionId()));
        }
        return  classifications;
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
    public void generatePlayerConfidences(Player player){
        List<Classification> classifications = classificationDao.findClassificationsByPlayerByConfidenceIsNull(player.getId());

        int lastIndexWithEnoughTests;
        int counter = 0;
        for (int i = classifications.size() - 1; i >= 0 ; i--) {
            if (classifications.get(i).getCorrect() != null) {
                counter++;
                if (counter == 10){
                    lastIndexWithEnoughTests = i;
                    break;
                }
            }
        }

        classifications.removeIf(classification -> {if (classification.getCorrect() != null) return false; //don't remove if test
                                                                if (classification.getCreatedAt().isAfter(LocalDateTime.now().)});


    }


}
