package edu.kit.datamanager.hector25.tora_game_management_service.service.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.dao.IClassificationDao;
import edu.kit.datamanager.hector25.tora_game_management_service.dao.IImageDao;
import edu.kit.datamanager.hector25.tora_game_management_service.dao.ISessionDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Classification;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Image;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Session;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IClassificationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClassificationService implements IClassificationService {

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
        return classificationDao.save(new Classification(imageDao.findImageById(imageId).orElseThrow(), decorated, sessionDao.findSessionById(sessionId).orElseThrow()));
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


}
