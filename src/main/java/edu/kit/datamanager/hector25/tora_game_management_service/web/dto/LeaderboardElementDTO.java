package edu.kit.datamanager.hector25.tora_game_management_service.web.dto;

import java.io.Serializable;
import java.util.Optional;

public record LeaderboardElementDTO(
        String username,
        int place,
        Optional<String> score
) implements Serializable {
}
