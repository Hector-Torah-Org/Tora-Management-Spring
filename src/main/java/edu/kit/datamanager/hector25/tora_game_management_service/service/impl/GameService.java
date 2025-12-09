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

import edu.kit.datamanager.hector25.tora_game_management_service.dao.IGameDao;
import edu.kit.datamanager.hector25.tora_game_management_service.dao.IPlayerDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Game;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.GameNotFoundException;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GameService implements IGameService {
    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);
    private final IGameDao gameDao;
    private final IPlayerDao playerDao;

    public GameService(IGameDao gameDao, IPlayerDao playerDao) {
        this.gameDao = gameDao;
        this.playerDao = playerDao;
    }

    @Override
    @Transactional
    public Game createGame(String name, List<UUID> playerIds) {
        List<Player> players = playerIds.stream()
                .map(playerDao::findPlayerById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        Game game = new Game(name, players);
        gameDao.save(game);

        LOG.info("Created game '{}' with id {} and {} players", name, game.getId(), players.size());
        return game;
    }

    @Override
    @Transactional
    public Game updateGame(UUID id, String name, List<UUID> playerIds) throws GameNotFoundException {
        LOG.info("Updating game with id {}", id);
        Game game = gameDao.findGameById(id)
                .orElseThrow(() -> {
                    LOG.warn("Game with id {} not found", id);
                    return new GameNotFoundException("Game with id " + id + " not found");
                });

        // Set new name and players
        game.setName(name);

        List<Player> newPlayers = playerIds.stream()
                .map(playerDao::findPlayerById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        game.setPlayers(newPlayers);

        gameDao.save(game);
        LOG.info("Updated game: {}", game);
        return game;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Game> getGameById(UUID gameId) {
        LOG.debug("Retrieving game by ID {}", gameId);
        return gameDao.findGameById(gameId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Game> getAllGames() {
        LOG.debug("Retrieving all games");
        List<Game> games = new ArrayList<>();
        gameDao.findAll().forEach(games::add);
        return games;
    }

    @Override
    @Transactional
    public void deleteGameById(UUID gameId) throws GameNotFoundException {
        Game game = gameDao.findGameById(gameId)
                .orElseThrow(() -> {
                    LOG.warn("Game with id {} not found", gameId);
                    return new GameNotFoundException("Game with id " + gameId + " not found");
                });
        gameDao.delete(game);
        LOG.info("Deleted game with id {}", gameId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> getPlayersForGame(UUID gameId) throws GameNotFoundException {
        LOG.debug("Retrieving players for game {}", gameId);
        Game game = gameDao.findGameById(gameId)
                .orElseThrow(() -> {
                    LOG.warn("Game with id {} not found", gameId);
                    return new GameNotFoundException("Game with id " + gameId + " not found");
                });
        LOG.info("Found {} players for game {}", game.getPlayers().size(), gameId);
        return game.getPlayers();
    }

    @Override
    @Transactional
    public Game addPlayerToGame(UUID gameId, UUID playerId) throws GameNotFoundException {
        LOG.info("Adding player {} to game {}", playerId, gameId);
        Game game = gameDao.findGameById(gameId)
                .orElseThrow(() -> {
                    LOG.warn("Game with id {} not found", gameId);
                    return new GameNotFoundException("Game with id " + gameId + " not found");
                });

        Optional<Player> playerOpt = playerDao.findPlayerById(playerId);
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            if (!game.getPlayers().contains(player)) {
                game.addPlayer(player);
                gameDao.save(game);
                LOG.info("Added player {} to game {}", playerId, gameId);
            } else {
                LOG.info("Player {} is already in game {}", playerId, gameId);
            }
        } else {
            LOG.warn("Player with id {} not found", playerId);
        }
        return game;
    }

    @Override
    @Transactional
    public Game removePlayerFromGame(UUID gameId, UUID playerId) throws GameNotFoundException {
        LOG.info("Removing player {} from game {}", playerId, gameId);
        Game game = gameDao.findGameById(gameId)
                .orElseThrow(() -> {
                    LOG.warn("Game with id {} not found", gameId);
                    return new GameNotFoundException("Game with id " + gameId + " not found");
                });

        Optional<Player> playerOpt = playerDao.findPlayerById(playerId);
        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
            game.removePlayer(player);
            gameDao.save(game);
            LOG.info("Removed player {} from game {}", playerId, gameId);
        } else {
            LOG.warn("Player with id {} not found", playerId);
        }
        return game;
    }
}

