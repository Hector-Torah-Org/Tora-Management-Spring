package edu.kit.datamanager.hector25.tora_game_management_service.service.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.dao.IClassificationDao;
import edu.kit.datamanager.hector25.tora_game_management_service.dao.IPlayerDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Classification;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IClassificationService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IStatisticService;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.LeaderboardDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.LeaderboardElementDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.StatisticsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticService implements IStatisticService {

    IClassificationDao classificationDao;
    IPlayerDao playerDao;
    IClassificationService classificationService;


    public StatisticService(IPlayerDao playerDao, IClassificationService classificationService, IClassificationDao iClassificationDao) {
        this.playerDao = playerDao;
        this.classificationService = classificationService;
        this.classificationDao = iClassificationDao;
    }

    @Override
    public LeaderboardDTO getLeaderboardByAmount(Pageable pageable){
        List<Object[]> leaderboardObjects = playerDao.getLeaderboardByAmount(pageable);
        List<LeaderboardElementDTO> leaderboardDTOs = new ArrayList<LeaderboardElementDTO>();
        int offsetForPage = pageable.getPageNumber() *  pageable.getPageSize();
        for (int i = 0; i < pageable.getPageSize(); i++) {
            leaderboardDTOs.add(new LeaderboardElementDTO((String) leaderboardObjects.get(i)[0], offsetForPage + i, Optional.ofNullable(leaderboardObjects.get(i)[1].toString())));
        }
        return new LeaderboardDTO(pageable.getPageNumber(), leaderboardDTOs);
    }

    @Override
    public LeaderboardDTO getLeaderboardByAmountByPlayer(Player player, int pagesize){
        Integer playerRank = playerDao.getRankForAmountByPlayerId(player.getId());
        Pageable pageable = PageRequest.of((playerRank / pagesize), playerRank % pagesize);

        return this.getLeaderboardByAmount(pageable);
    }

    @Override
    public LeaderboardDTO getLeaderboardByConfidence(Pageable pageable){
        List<Object[]> leaderboardObjects = playerDao.getLeaderboardByConfidence(pageable);
        List<LeaderboardElementDTO> leaderboardDTOs = new ArrayList<LeaderboardElementDTO>();
        int offsetForPage = pageable.getPageNumber() *  pageable.getPageSize();
        for (int i = 0; i < pageable.getPageSize(); i++) {
            leaderboardDTOs.add(new LeaderboardElementDTO((String) leaderboardObjects.get(i)[0], offsetForPage + i, Optional.ofNullable(leaderboardObjects.get(i)[1].toString())));
        }
        return new LeaderboardDTO(pageable.getPageNumber(), leaderboardDTOs);
    }

    @Override
    public LeaderboardDTO getLeaderboardByConfidenceByPlayer(Player player, int pagesize){
        Integer playerRank = playerDao.getRankForConfidenceByPlayerId(player.getId());
        Pageable pageable = PageRequest.of((playerRank / pagesize), playerRank % pagesize);

        return this.getLeaderboardByAmount(pageable);
    }

    @Override
    public StatisticsDTO getAmountStatisticsByPlayerAndYear(Player player, int year) {
        List<Classification> classifications = classificationDao.findClassificationsByPlayerIdAndYear(player.getId(), year);
        return null;

    }

    @Override
    public StatisticsDTO getConfidenceStatisticsByPlayer(Player player) {
        return null;
    }
}
