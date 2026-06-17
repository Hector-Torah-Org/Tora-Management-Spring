/*
 * Copyright (c) 2025 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.kit.datamanager.hector25.tora_game_management_service.web;

import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.AccumulatedDataDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.LeaderboardDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.StatisticsDTO;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.UUID;

@RestController
@RequestMapping("/Statistics")
@Validated
@Tag(name = "Statistics API", description = "This Api endpoint manages the players access to Leaderboards and Statistics")
public interface IStatisticsAPI {

    //================== Leaderboards ================//

    /**
     * Returns a Leaderboard based on total classifications
     *
     * @param page The page to return
     * @param pagesize The size of the pages
     *
     * @return A response Entity containing user names, the amount of classifications, the page and HTTP 200,
     * or 404 if the page is out of reach
     */
    @Operation(
            summary = "Get amount Leaderboard",
            description = "Get a Leaderboard by total classifications for a page",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Leaderboard",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LeaderboardDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Starting page out of reach")
            }
    )
    @GetMapping("/totalClassifications/{page}/{pagesize}")
    ResponseEntity<LeaderboardDTO> getLeaderboardByAmount(@PathVariable int page, @PathVariable int pagesize);

    /**
     * Returns a Leaderboard based on total classifications from a player
     *
     * @param sessionId The current sessionId of the player who will be in the center of the Leaderboard
     * @param pagesize The size each page should have
     *
     * @return A response Entity containing user names, their place, the amount of classifications, the page and HTTP 200,
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
    @GetMapping("/totalClassifications/fromPlayer/{sessionId}/{pagesize}")
    ResponseEntity<LeaderboardDTO> getLeaderboardByAmountByPlayer(@PathVariable UUID sessionId, @PathVariable int pagesize);

    /**
     * Returns a Leaderboard based on confidence
     *
     * @param page The page to return
     * @param pagesize The size of the pages
     *
     * @return A response Entity containing user names, the page and HTTP 200,
     * or 404 if the page is out of reach
     */
    @Operation(
            summary = "Get confidence Leaderboard",
            description = "Get a Leaderboard by confidence for a page",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Leaderboard",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LeaderboardDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Starting page out of reach")
            }
    )
    @GetMapping("/totalConfidence/{page}/{pagesize}")
    ResponseEntity<LeaderboardDTO> getLeaderboardByConfidence(@PathVariable int page, @PathVariable int pagesize);

    /**
     * Returns a Leaderboard based on total confidence from a player
     *
     * @param sessionId The current sessionId of the player who will be in the center of the Leaderboard
     * @param pagesize The size each page should have
     *
     * @return A response Entity containing user names, their place, the page and HTTP 200,
     * or 404 if the sessionId isn't valid
     */
    @Operation(
            summary = "Get confidence Leaderboard by player",
            description = "Get the Leaderboard by total confidence from a player",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Leaderboard",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LeaderboardDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pass an existing Session")
            }
    )
    @GetMapping("/totalConfidence/fromPlayer/{sessionId}/{pagesize}")
    ResponseEntity<LeaderboardDTO> getLeaderboardByConfidenceByPlayer(@PathVariable UUID sessionId, @PathVariable int pagesize);


    //================= Statistics =================//

    /**
     * Returns a statistic of amount of own annotations per day
     *
     * @param sessionId The current sessionId of the player whom the statistic is about
     * @param year The year for which the statistic will be returned
     *
     * @return A response Entity containing the year and the values for each day
     */
    @Operation(
            summary = "Get amount Statistics",
            description = "Get amount of annotations per day for player",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatisticsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pass an existing Session")
            }
    )
    @GetMapping("/playerAmount/{sessionId}/{year}")
    ResponseEntity<StatisticsDTO> getAmountStatisticForPlayer(@PathVariable UUID sessionId, @PathVariable int year);

    /**
     * Returns a statistic of confidence of own annotations per day
     *
     * @param sessionId The current sessionId of the player whom the statistic is about
     * @param year The year for which the statistic will be returned
     *
     * @return A response Entity containing the year and the values for each week
     */
    @Operation(
            summary = "Get confidence Statistics",
            description = "Get confidence of annotations per day for player",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StatisticsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pass an existing Session")
            }
    )
    @GetMapping("/playerConfidence/{sessionId}/{year}")
    ResponseEntity<StatisticsDTO> getConfidenceStatisticForPlayer(@PathVariable UUID sessionId, @PathVariable int year);

    //==========Complete Statistics==============////

    /**
     * Returns the accumulated Data per image
     */
    @Operation(
            summary = "Get accumulated data",
            description = "Get id link charackter",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved statistic",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccumulatedDataDTO.class)))
            }
    )
    @GetMapping("/completeStatistic")
    ResponseEntity<AccumulatedDataDTO> getCompleteStatistic();
}
