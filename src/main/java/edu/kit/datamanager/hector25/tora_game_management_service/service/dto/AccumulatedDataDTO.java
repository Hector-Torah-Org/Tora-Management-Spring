package edu.kit.datamanager.hector25.tora_game_management_service.service.dto;

import java.util.List;
import java.util.UUID;

public record AccumulatedDataDTO(
        List<AccumulatedImageDataDTO> ImageDTOs
) {
    public record AccumulatedImageDataDTO(
            UUID id,
            String link,
            Character character,
            AggregatedResults results,
            int classificationAmount

    ){
        public record AggregatedResults(
            float decoratedProbability,
            float undecoratedProbability,
            float badDataProbability
            ){}
    }
}
