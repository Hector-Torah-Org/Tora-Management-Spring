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

package edu.kit.datamanager.hector25.tora_game_management_service.web.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Image;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IClassificationService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IImageService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IPlayerService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.ISessionService;
import edu.kit.datamanager.hector25.tora_game_management_service.web.IImageAPI;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.ClassificationReceiveDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.ImageSendingDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.ImagesSendDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Validated
@RestController
public class ImageRestController implements IImageAPI {

    private final IImageService imageService;
    private final ISessionService sessionService;
    private final IPlayerService playerService;
    private final IClassificationService classificationService;

    ImageRestController(IImageService imageService,  ISessionService sessionService,  IPlayerService playerService,  IClassificationService classificationService) {
        this.imageService = imageService;
        this.sessionService = sessionService;
        this.playerService = playerService;
        this.classificationService = classificationService;
    }

    @Override
    public ResponseEntity<ImagesSendDTO> getImage(String sessionId, int amount, boolean forTutorial) {
        List<Image> images = new ArrayList<>();
        if (forTutorial) {
            images.addAll(imageService.getTestImagesForTutorial(amount));
        } else {
            images.addAll(imageService.getImagesForPlayer(playerService.getPlayerBySessionId(UUID.fromString(sessionId)).getId(), amount));
        }
        //Building the individual DTOs for each image
        List<ImageSendingDTO> imageSendingDTOS = new ArrayList<>();
        for (Image image : images) {
            imageSendingDTOS.add(new ImageSendingDTO(image.getId(), image.getLink(), image.getCharacter()));
        }

        //Building one DTO containing them
        ImagesSendDTO imagesSendDTO = new ImagesSendDTO(imageSendingDTOS);

        if (images.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(imagesSendDTO);
        }
    }

    @Override
    public ResponseEntity<Double> saveClassifications(String sessionId, List<ClassificationReceiveDTO> classifications, boolean giveFeedback) {


        if (giveFeedback) {
            List<UUID> uuids = new ArrayList<>();
            for  (ClassificationReceiveDTO classification : classifications) {
                uuids.add(classification.imageId());
            }

            List<Image> images = imageService.getImages(uuids);
            int correctClassifications = 0;
            int failedClassifications = 0;

            for (int i = 0; i < classifications.size(); i++) {
                if (images.get(i).isDecorated() != null && classifications.get(i).isDecorated() == images.get(i).isDecorated()) {
                    correctClassifications++;
                } else if (images.get(i).isDecorated() != null && classifications.get(i).isDecorated() != images.get(i).isDecorated()) {
                    failedClassifications++;
                }
            }

            double correctRatio = (double)correctClassifications/(double)(failedClassifications +  correctClassifications);

            return ResponseEntity.ok().body(correctRatio);
        }

        try {
            for (ClassificationReceiveDTO classification : classifications) {
                Logger LOGGER = LoggerFactory.getLogger(ImageRestController.class);
                LOGGER.info("received " + classification.toString());
                System.out.println("hreceived " + classification.toString());
                if (classification.isDatasetError()) {
                    classificationService.createBadDatasetClassification(classification.imageId(), UUID.fromString(sessionId));
                } else {
                    classificationService.createClassification(classification.imageId(), classification.isDecorated(), UUID.fromString(sessionId));
                }

            }
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }

        classificationService.generatePlayerConfidences(playerService.getPlayerBySessionId(UUID.fromString(sessionId)).getId());


        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();



    }
}
