package edu.kit.datamanager.hector25.tora_game_management_service.service.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.dao.IPlayerDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IStatisticService;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.LeaderboardDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.LeaderboardElementDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticService implements IStatisticService {

    IPlayerDao playerDao;


    public StatisticService(IPlayerDao playerDao) {
        this.playerDao = playerDao;
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
    public 
}
