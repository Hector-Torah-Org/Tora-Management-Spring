package edu.kit.datamanager.hector25.tora_game_management_service.web.dto;

import java.io.Serializable;
import java.util.UUID;

public record SessionLoginDTO (UUID sessionUUID, String gameState) implements Serializable {
}
