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

package edu.kit.datamanager.hector25.tora_game_management_service.service.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.dao.IPlayerDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Game;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Session;
import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.PlayerNotFoundException;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IPlayerService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.PlayerCreationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService implements IPlayerService {
    private static final Logger LOG = LoggerFactory.getLogger(PlayerService.class);
    private final IPlayerDao playerDao;

    public PlayerService(IPlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Override
    @Transactional
    public Player createPlayer(PlayerCreationDTO playerCreationDTO) {
        Player player = new Player(playerCreationDTO.firstName(), playerCreationDTO.lastName(), playerCreationDTO.userName());
        playerDao.save(player);
        LOG.info("Created player with firstName {}, lastName {}, userName {} and id {}", player.getFirstName(), player.getLastName(), player.getUserName(), player.getId());
        return player;
    }

    @Override
    @Transactional
    public Player updatePlayer(UUID id, PlayerCreationDTO playerCreationDTO) throws PlayerNotFoundException {
        LOG.info("Updating player with id {}", id);
        Player player = playerDao.findPlayerById(id)
                .orElseThrow(() -> {
                    LOG.warn("Player with id {} not found", id);
                    return new PlayerNotFoundException("Player with id " + id + " not found");
                });
        LOG.debug("Found player with id {}", id);
        player.setFirstName(playerCreationDTO.firstName());
        player.setLastName(playerCreationDTO.lastName());
        playerDao.save(player);
        LOG.info("Updated player: {}", player);
        return player;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> findPlayerByFirstNameAndLastName(String firstName, String lastName) {
        LOG.info("Finding player by first name and last name");
        List<Player> players = playerDao.findPlayerByFirstNameAndLastName(firstName, lastName);
        players.forEach(player -> player.getSessions().size()); // Trigger lazy loading
        return players;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> findPlayerByFirstName(String firstName) {
        LOG.info("Finding player by first name {}", firstName);
        List<Player> players = playerDao.findPlayersByFirstName(firstName);
        players.forEach(player -> player.getSessions().size()); // Trigger lazy loading
        return players;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> findPlayerByLastName(String lastName) {
        LOG.debug("Finding player by lastName {}", lastName);
        List<Player> players = playerDao.findPlayersByLastName(lastName);
        players.forEach(player -> player.getSessions().size()); // Trigger lazy loading
        return players;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerById(UUID playerId) {
        LOG.debug("Retrieving player by ID {}", playerId);
        Optional<Player> playerOpt = playerDao.findPlayerById(playerId);
        // Ensure games collection is loaded before returning
        playerOpt.ifPresent(player -> {
            player.getSessions().size(); // Trigger lazy loading
            LOG.debug("Loaded {} games for player {}", player.getSessions().size(), playerId);
        });
        return playerOpt;
    }

    @Override
    @Transactional
    public void deletePlayerById(UUID playerId) throws PlayerNotFoundException {
        Player player = playerDao.findPlayerById(playerId)
                .orElseThrow(() -> {
                    LOG.warn("Player with id {} not found", playerId);
                    return new PlayerNotFoundException("Player with id " + playerId + " not found");
                });
        playerDao.delete(player);
        LOG.info("Deleted player with id {}", playerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Session> getGamesForPlayer(UUID playerId) throws PlayerNotFoundException {
        LOG.debug("Retrieving games for player {}", playerId);
        Player player = playerDao.findPlayerById(playerId)
                .orElseThrow(() -> {
                    LOG.warn("Player with id {} not found", playerId);
                    return new PlayerNotFoundException("Player with id " + playerId + " not found");
                });
        LOG.info("Found {} games for player {}", player.getSessions().size(), playerId);
        return player.getSessions();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> getAllPlayers() {
        LOG.debug("Retrieving all players");
        List<Player> players = new java.util.ArrayList<>();
        playerDao.findAll().forEach(player -> {
            player.getSessions().size(); // Trigger lazy loading of games
            players.add(player);
        });
        LOG.info("Found {} players", players.size());
        return players;
    }

}
