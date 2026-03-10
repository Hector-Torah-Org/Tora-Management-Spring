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

import edu.kit.datamanager.hector25.tora_game_management_service.dao.IImageDao;
import edu.kit.datamanager.hector25.tora_game_management_service.dao.IPlayerDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Classification;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Image;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IClassificationService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ImageService implements IImageService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);
    private final IImageDao imageDao;
    private final IClassificationService classificationService;
    private final IPlayerDao playerDao;

    public ImageService(IImageDao imageDao,  IClassificationService classificationService, IPlayerDao playerDao) {
        this.imageDao = imageDao;
        this.classificationService = classificationService;
        this.playerDao = playerDao;
    }

    @Override
    public Image createImage(Boolean decorated, String link, Character character) {
        Image image = new Image(decorated, link, character);
        imageDao.save(image);
        LOG.info("Image created with id: {}, link: {}, isDecorated: {}, Character: {}", image.getId(), image.getLink(), image.isDecorated(), image.getCharacter());
        return image;
    }

    public Image createImage(String link,  Character character) {
        Image image = new Image(link, character);
        imageDao.save(image);
        LOG.info("Image created with id: {}, link: {}, Character {}", image.getId(), image.getLink(), image.getCharacter());
        return image;
    }

    public Optional<Image> getImage(UUID imageId){
        return imageDao.findById(imageId);
    }

    public Image getImageToClassify(){
        return null;
    }

    public Image getTestImage(){
        return null;
    }

    public Image getImageToClassifyForPlayer(UUID playerId){

        List<Classification> classification = classificationService.findOtherPlayersClassification(playerId, PageRequest.of(0, 1));

        if (! classification.isEmpty()){
            return classification.getFirst().getImage();
        }
        else {
            Pageable pageRequest = PageRequest.of(0, 1);
            List<Image> image = imageDao.findFirstUnusedByPlayer(playerId, pageRequest);
            return image.getFirst();
        }
    }

    public Image getTestImageForPlayer(UUID playerId){
        int random = new Random().nextInt(2);
        Pageable pageRequest = PageRequest.of(0, 1);
        if (random == 0) {
            return imageDao.findTestImageForPlayer(Boolean.TRUE, playerId, pageRequest).getFirst();
        }
        else{
            return imageDao.findTestImageForPlayer(Boolean.FALSE, playerId, pageRequest).getFirst();
        }
    }

}