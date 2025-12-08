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
import edu.kit.datamanager.hector25.tora_game_management_service.service.IGameService;
import edu.kit.datamanager.hector25.tora_game_management_service.web.IGameAPI;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * REST controller implementation for the Game API.
 * This class implements the IGameAPI interface and delegates to the IGameService.
 */
@Validated
@RestController
public class GameRestController implements IGameAPI {

    private final IGameService gameService;

    public GameRestController(IGameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public ResponseEntity<@NonNull Game> createGame(List<UUID> playerIds) {
        Game createdGame = gameService.createGame(playerIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
    }

    @Override
    public ResponseEntity<@NonNull Game> getGame(UUID id) {
        return gameService.getGameById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
    }

    @Override
    public ResponseEntity<@NonNull List<@NonNull Game>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    @Override
    public ResponseEntity<@NonNull Game> updateGame(UUID id, List<UUID> playerIds) {
        Game updatedGame = gameService.updateGame(id, playerIds);
        return ResponseEntity.ok(updatedGame);
    }

    @Override
    public ResponseEntity<@NonNull Void> deleteGame(UUID id) {
        gameService.deleteGameById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<@NonNull List<@NonNull Player>> getPlayersForGame(UUID gameId) {
        List<Player> players = gameService.getPlayersForGame(gameId);
        return ResponseEntity.ok(players);
    }

    @Override
    public ResponseEntity<@NonNull Game> addPlayerToGame(UUID gameId, UUID playerId) {
        Game updatedGame = gameService.addPlayerToGame(gameId, playerId);
        return ResponseEntity.ok(updatedGame);
    }

    @Override
    public ResponseEntity<@NonNull Game> removePlayerFromGame(UUID gameId, UUID playerId) {
        Game updatedGame = gameService.removePlayerFromGame(gameId, playerId);
        return ResponseEntity.ok(updatedGame);
    }
}

