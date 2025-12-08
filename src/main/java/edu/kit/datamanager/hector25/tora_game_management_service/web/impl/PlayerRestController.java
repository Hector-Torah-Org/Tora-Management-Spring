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

package edu.kit.datamanager.hector25.tora_game_management_service.web.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Game;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.PlayerNotFoundException;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IPlayerService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.PlayerCreationDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.web.IPlayerAPI;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller implementation for the Player API.
 * This class implements the IPlayerAPI interface and delegates to the IPlayerService.
 */
@Validated
@RestController
public class PlayerRestController implements IPlayerAPI {

    private final IPlayerService playerService;

    public PlayerRestController(IPlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public ResponseEntity<@NonNull Player> createPlayer(PlayerCreationDTO playerCreationDTO) {
        Player createdPlayer = playerService.createPlayer(playerCreationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
    }

    @Override
    public ResponseEntity<@NonNull Player> getPlayer(UUID id) {
        return playerService.getPlayerById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id " + id + " not found"));
    }

    @Override
    public ResponseEntity<@NonNull List<Player>> findPlayerByFirstNameAndLastName(
            String firstName,
            String lastName) {
        List<Player> players = playerService.findPlayerByFirstNameAndLastName(firstName, lastName);
        return ResponseEntity.ok(players);
    }

    @Override
    public ResponseEntity<@NonNull List<Player>> findPlayerByFirstName(
            String firstName) {
        List<Player> players = playerService.findPlayerByFirstName(firstName);
        return ResponseEntity.ok(players);
    }

    @Override
    public ResponseEntity<@NonNull List<Player>> findPlayerByLastName(
            String lastName) {
        List<Player> players = playerService.findPlayerByLastName(lastName);
        return ResponseEntity.ok(players);
    }

    @Override
    public ResponseEntity<@NonNull Player> updatePlayer(
            UUID id,
            PlayerCreationDTO playerCreationDTO) {
        Player updatedPlayer = playerService.updatePlayer(id, playerCreationDTO);
        return ResponseEntity.ok(updatedPlayer);
    }

    @Override
    public ResponseEntity<@NonNull Void> deletePlayer(UUID id) {
        playerService.deletePlayerById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<@NonNull List<@NonNull Game>> getGamesForPlayer(UUID playerId) {
        List<Game> games = playerService.getGamesForPlayer(playerId);
        return ResponseEntity.ok(games);
    }
}

