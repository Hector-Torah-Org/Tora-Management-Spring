package edu.kit.datamanager.hector25.tora_game_management_service.service;

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.LeaderboardDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.StatisticsDTO;

import org.springframework.data.domain.Pageable;

public interface IStatisticService {

    LeaderboardDTO getLeaderboardByAccuracy(Pageable pageable);

    LeaderboardDTO getLeaderboardByAccuracyByPlayer(Player player);

    LeaderboardDTO getLeaderboardByAmountByPlayer(Player player, int pagesize);

    LeaderboardDTO getLeaderboardByAmount(Pageable pageable);

    StatisticsDTO getAmountStatisticsByPlayer(Player player);

    StatisticsDTO getAccuracyStatisticsByPlayer(Player player);
}
