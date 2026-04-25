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

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Classification;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.ClassificationReceiveDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.ImageSendingDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.ImagesSendDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/image")
@Validated
@Tag(name = "Image Api", description = "This Api manages the Images")
public interface IImageAPI {

    /**
     * Returns images for the player to classify
     *
     * @param sessionId The session in which the player currently is
     * @param amount The amount of images to return
     *
     * @return A responseEntity containing a List with items containing the image link, the imageId and the character (HTTP 200) or 404 if the session doesn't exist
     */
    @Operation(
            description = "Returns an Image for the player to classify",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved Image",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ImagesSendDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pass an existing session")
            })
    @GetMapping("/{sessionId}/{amount}")
    ResponseEntity<ImagesSendDTO> getImage(@PathVariable("sessionId") String sessionId, @PathVariable("amount") int amount);

    /**
     * Saves the classifications made by the player
     *
     * @param sessionId             The session in which the player currently is
     * @param imageClassifications  The classifications the player accumulated in the game
     *
     * @return HTTP 204 if the Classifications are valid, 404 if they aren't,
     * 200 if the Server decides to give a brief Feedback for the Player
     */
    @Operation(
            summary = "Return Classifications",
            description = "Saves the classifications the Player made by the session id",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Successfully saved the classifications"),
                    @ApiResponse(responseCode = "200", description = "Successfully saved the classifications", // not added yet
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "404", description = "Session not found")
            }
    )
    @PostMapping("/{sessionId}")
    ResponseEntity<String> saveClassifications(@PathVariable("sessionId") String sessionId,
                                               @Valid @RequestBody List<ClassificationReceiveDTO> imageClassifications);



}
