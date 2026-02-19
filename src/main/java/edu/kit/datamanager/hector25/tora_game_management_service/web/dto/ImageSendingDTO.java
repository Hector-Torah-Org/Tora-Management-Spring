package edu.kit.datamanager.hector25.tora_game_management_service.web.dto;

import java.io.Serializable;
import java.util.UUID;

public record ImageSendingDTO(
        UUID id,
        String link,
        Character character
) implements Serializable {}
