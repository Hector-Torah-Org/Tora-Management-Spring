package edu.kit.datamanager.hector25.tora_game_management_service.web.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Image;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IClassificationService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IImageService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IPlayerService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.ISessionService;
import edu.kit.datamanager.hector25.tora_game_management_service.web.IImageAPI;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.ClassificationReceiveDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.ImageSendingDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ImageSendingDTO> getImage(String sessionId) {
        int random = new Random().nextInt(25);
        Image image;
        if (random == 0){
            image = imageService.getTestImageForPlayer(playerService.getPlayerBySessionId(UUID.fromString(sessionId)).getId());
        }
        else {
            image = imageService.getImageToClassifyForPlayer(playerService.getPlayerBySessionId(UUID.fromString(sessionId)).getId());
        }

        if (image == null){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ImageSendingDTO(image.getId(), image.getLink(), image.getCharacter()));
        }
    }

    @Override
    public ResponseEntity<String> saveClassifications(String sessionId, List<ClassificationReceiveDTO> classifications) {
        try {
            for (ClassificationReceiveDTO classification : classifications) {
                classificationService.createClassification(classification.imageId(), classification.isDecorated(), UUID.fromString(sessionId));
            }
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }

        int random = new Random().nextInt(25);
        if (random == 0){
            return ResponseEntity.status(HttpStatus.OK).body("Message");
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }


    }
}
