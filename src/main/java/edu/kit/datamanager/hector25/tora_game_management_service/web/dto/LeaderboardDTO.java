package edu.kit.datamanager.hector25.tora_game_management_service.web.dto;

import java.io.Serializable;
import java.util.List;

public record LeaderboardDTO(
        List<LeaderboardElementDTO> leaderboardElementDTOS
) implements Serializable {
}
