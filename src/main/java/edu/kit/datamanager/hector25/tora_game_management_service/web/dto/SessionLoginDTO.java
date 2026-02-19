package edu.kit.datamanager.hector25.tora_game_management_service.web.dto;

import java.util.UUID;

public class SessionLoginDTO {

    private UUID uuid;
    private String gameState;

    SessionLoginDTO(UUID uuid, String gameState) {
        this.uuid = uuid;
        this.gameState = gameState;
    }

    public UUID getUuid() {
        return uuid;
    }
    public String getGameState() {
        return gameState;
    }
    public void setGameState(String gameState) {
        this.gameState = gameState;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
