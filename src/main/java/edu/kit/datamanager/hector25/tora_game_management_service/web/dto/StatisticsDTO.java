package edu.kit.datamanager.hector25.tora_game_management_service.web.dto;

import java.io.Serializable;

public record StatisticsDTO(
        Integer year,
        Integer[] values
) implements Serializable {}
