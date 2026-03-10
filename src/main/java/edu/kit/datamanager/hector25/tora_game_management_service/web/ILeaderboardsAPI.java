package edu.kit.datamanager.hector25.tora_game_management_service.web;

import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.LeaderboardDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Leaderboards")
@Validated
@Tag(name = "Leaderboards API", description = "This Api endpoint manages the players access to Leaderboards")
public interface ILeaderboardsAPI {

    /**
     * Returns a Leaderboard based on total classifications
     *
     * @param startPlace The point from which onward the next 100 best are shown
     *
     * @return A response Entity containing user names, the amount of classifications and HTTP 200,
     * or 404 if the startPlace is out of reach
     */
    @Operation(
            summary = "Get amount Leaderboard",
            description = "Get a Leaderboard by total classifications from a starting place",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Leaderboard",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LeaderboardDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Starting Place out of reach")
            }
    )
    @GetMapping("/totalClassifications/{startPlace}")
    ResponseEntity<LeaderboardDTO> getLeaderboardByClassifications(@PathVariable("startPlace") int startPlace);

    /**
     * Returns a Leaderboard based on total classifications
     *
     * @param sessionId The current sessionId of the player who will be in the center of the Leaderboard
     *
     * @return A response Entity containing user names, their place, the amount of classifications and HTTP 200,
     * or 404 if the sessionId isn't valid
     */
    @Operation(
            summary = "Get amount Leaderboard by player",
            description = "Get the Leaderboard by total classifications from a player",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Leaderboard",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LeaderboardDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pass an existing Session")
            }
    )
    @GetMapping("totalClassifications/fromPlayer/{sessionId}")
    ResponseEntity<LeaderboardDTO> getLeaderboardByPlayer(@PathVariable("sessionId") UUID sessionId);

    //Leaderboards to add: By correct classification rate (without exact percentage)
    //Stats to show to the player, e.g. Change in certainty by Session, classifications per session
}
