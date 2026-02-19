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

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Session;
import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.PlayerNotFoundException;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IPlayerService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.ISessionService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.PlayerCreationDTO;
import edu.kit.datamanager.hector25.tora_game_management_service.service.impl.SessionService;
import edu.kit.datamanager.hector25.tora_game_management_service.web.IPlayerAPI;
import edu.kit.datamanager.hector25.tora_game_management_service.web.dto.SessionLoginDTO;
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
public abstract class PlayerRestController implements IPlayerAPI {

    private final IPlayerService playerService;
    private final ISessionService sessionService;

    public PlayerRestController(IPlayerService playerService,  ISessionService sessionService) {
        this.playerService = playerService;
        this.sessionService = sessionService;
    }

    @Override
    public ResponseEntity<@NonNull Player> createPlayer(PlayerCreationDTO playerCreationDTO) {
        Player createdPlayer = playerService.createPlayer(playerCreationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
    }

 //   @Override
 //   public ResponseEntity<@NonNull Player> getPlayer(UUID id) {
 //       return playerService.getPlayerById(id)
 //               .map(ResponseEntity::ok)
 //               .orElseThrow(() -> new PlayerNotFoundException("Player with id " + id + " not found"));
 //   }
//
 //   @Override
 //   public ResponseEntity<@NonNull List<Player>> findPlayerByFirstNameAndLastName(
 //           String firstName,
 //           String lastName) {
 //       List<Player> players = playerService.findPlayerByFirstNameAndLastName(firstName, lastName);
 //       return ResponseEntity.ok(players);
 //   }
//
 //   @Override
 //   public ResponseEntity<@NonNull List<Player>> findPlayerByFirstName(
 //           String firstName) {
 //       List<Player> players = playerService.findPlayerByFirstName(firstName);
 //       return ResponseEntity.ok(players);
 //   }

 //   @Override
 //   public ResponseEntity<@NonNull List<Player>> findPlayerByLastName(
 //           String lastName) {
 //       List<Player> players = playerService.findPlayerByLastName(lastName);
 //       return ResponseEntity.ok(players);
 //   }
//
   //  @Override
  //  public ResponseEntity<@NonNull Player> updatePlayer(
  //          UUID sessionId,
  //          PlayerCreationDTO playerCreationDTO) {
  //      UUID playerId = playerService.getPlayerBySessionId(sessionId).id;
  //      Player updatedPlayer = playerService.updatePlayer(id, playerCreationDTO, );
  //      return ResponseEntity.ok(updatedPlayer);
  //  }

    @Override
    public ResponseEntity<@NonNull Void> deletePlayer(UUID id) {
        playerService.deletePlayerById(id);
        return ResponseEntity.noContent().build();
    }

 //   @Override
 //   public ResponseEntity<@NonNull List<@NonNull Session>> getGamesForPlayer(UUID playerId) {
 //       List<Session> games = playerService.getGamesForPlayer(playerId);
 //       return ResponseEntity.ok(games);
 //   }
//
 //   @Override
 //   public ResponseEntity<@NonNull List<@NonNull Player>> getAllPlayers() {
 //       List<Player> players = playerService.getAllPlayers();
 //       return ResponseEntity.ok(players);
 //   }
    @Override
    public ResponseEntity<SessionLoginDTO> logInPlayer(String firstName, String lastName, String userName){
        return null;
    }
    @Override
    public ResponseEntity<Void> updateGamestate(UUID sessionId, String gameState) {
        return null;
    }
}

