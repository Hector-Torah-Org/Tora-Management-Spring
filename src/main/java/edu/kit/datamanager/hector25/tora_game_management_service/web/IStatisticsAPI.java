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
    ResponseEntity<LeaderboardDTO> getLeaderboardByClassifications(@PathVariable("page") int page, @PathVariable("pagesize") int pagesize);

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
    @GetMapping("totalClassifications/fromPlayer/{sessionId}/{pagesize}")
    ResponseEntity<LeaderboardDTO> getLeaderboardByClassificationByPlayer(@PathVariable("sessionId") UUID sessionId, @PathVariable("pagesize")  int pagesize);

    /**
     * Returns a Leaderboard based on accuracy
     *
     * @param page The page to return
     * @param pagesize The size of the pages
     *
     * @return A response Entity containing user names, the page and HTTP 200,
     * or 404 if the page is out of reach
     */
    @Operation(
            summary = "Get amount Leaderboard",
            description = "Get a Leaderboard by accuracy from a starting place",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Leaderboard",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LeaderboardDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Starting page out of reach")
            }
    )
    @GetMapping("/totalAccuracy/{page}/{pagesize}")
    ResponseEntity<LeaderboardDTO> getLeaderboardByAccuracy(@PathVariable("page") int page, @PathVariable("pagesize") int pagesize);

    /**
     * Returns a Leaderboard based on total accuracy from a player
     *
     * @param sessionId The current sessionId of the player who will be in the center of the Leaderboard
     * @param pagesize The size each page should have
     *
     * @return A response Entity containing user names, their place, the page and HTTP 200,
     * or 404 if the sessionId isn't valid
     */
    @Operation(
            summary = "Get amount Leaderboard by player",
            description = "Get the Leaderboard by total Accuracy from a player",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Leaderboard",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LeaderboardDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pass an existing Session")
            }
    )
    @GetMapping("totalAccuracy/fromPlayer/{sessionId}/{pagesize}")
    ResponseEntity<LeaderboardDTO> getLeaderboardByAccuracyByPlayer(@PathVariable("sessionId") UUID sessionId, @PathVariable("pagesize")  int pagesize);


    //================= Statistics =================//

    /**
     * Returns a statistic of amount of own annotations per week
     *
     * @param sessionId The current sessionId of the player whom the statistic is about
     * @param year The year for which the statistic will be returned
     *
     * @return A response Entity containing the year and the values for each week
     */
    @Operation(
            summary = "Get amount Statistics",
            description = "Get amount of annotations per week for player",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatisticsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pass an existing Session")
            }
    )
    @GetMapping("playerAmount/{sessionId}/{year}")
    ResponseEntity<StatisticsDTO> getAmountStatisticForPlayer(@PathVariable("sessionId") UUID sessionId, @PathVariable("year") Integer year);

    /**
     * Returns a statistic of accuracy of own annotations per week
     *
     * @param sessionId The current sessionId of the player whom the statistic is about
     * @param year The year for which the statistic will be returned
     *
     * @return A response Entity containing the year and the values for each week
     */
    @Operation(
            summary = "Get accuracy Statistics",
            description = "Get accuracy of annotations per week for player",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved statistics",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = StatisticsDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pass an existing Session")
            }
    )
    @GetMapping("playerAccuracy/{sessionId}/{year}")
    ResponseEntity<StatisticsDTO> getAccuracyStatisticForPlayer(@PathVariable("sessionId") UUID sessionId, @PathVariable("year") Integer year);

    //Leaderboards to add: By correct classification rate (without exact percentage)
    //Stats to show to the player, e.g. Change in certainty by Session, classifications per session
}
