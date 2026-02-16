package edu.kit.datamanager.hector25.tora_game_management_service.service.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Classification;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Image;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IClassificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClassificationService implements IClassificationService {

    @Override
    public Classification createClassification(Image image, Boolean isDecorated) {
        return null;
    }

    public Classification findClassificationById(UUID id) {
        return null;
    }

    @Override
    public List<Classification> findClassificationsForImage(Image image) {
        return List.of();
    }
}
