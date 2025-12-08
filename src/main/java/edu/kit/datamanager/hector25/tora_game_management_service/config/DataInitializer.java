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

package edu.kit.datamanager.hector25.tora_game_management_service.config;

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Game;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IGameService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IPlayerService;
import edu.kit.datamanager.hector25.tora_game_management_service.service.dto.PlayerCreationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Configuration class for initializing sample data in the database.
 * This initializer runs at application startup and creates sample players and games.
 */
@Configuration
public class DataInitializer {
    private static final Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

    /**
     * CommandLineRunner bean that initializes the database with sample data.
     * Only runs when the 'dev' profile is active or when no profile is specified.
     *
     * @param playerService The player service for creating players
     * @param gameService   The game service for creating games
     * @return CommandLineRunner that executes the data initialization
     */
    @Bean
    @Profile({"dev", "default"})
    public CommandLineRunner initializeData(IPlayerService playerService, IGameService gameService) {
        return args -> {
            LOG.info("Starting data initialization...");

            // Create sample players
            List<UUID> playerIds = new ArrayList<>();

            Player player1 = playerService.createPlayer(new PlayerCreationDTO("Alice", "Anderson"));
            playerIds.add(player1.getId());
            LOG.info("Created player: {} {} (ID: {})", player1.getFirstName(), player1.getLastName(), player1.getId());

            Player player2 = playerService.createPlayer(new PlayerCreationDTO("Bob", "Brown"));
            playerIds.add(player2.getId());
            LOG.info("Created player: {} {} (ID: {})", player2.getFirstName(), player2.getLastName(), player2.getId());

            Player player3 = playerService.createPlayer(new PlayerCreationDTO("Charlie", "Clark"));
            playerIds.add(player3.getId());
            LOG.info("Created player: {} {} (ID: {})", player3.getFirstName(), player3.getLastName(), player3.getId());

            Player player4 = playerService.createPlayer(new PlayerCreationDTO("Diana", "Davis"));
            playerIds.add(player4.getId());
            LOG.info("Created player: {} {} (ID: {})", player4.getFirstName(), player4.getLastName(), player4.getId());

            Player player5 = playerService.createPlayer(new PlayerCreationDTO("Eve", "Evans"));
            playerIds.add(player5.getId());
            LOG.info("Created player: {} {} (ID: {})", player5.getFirstName(), player5.getLastName(), player5.getId());

            Player player6 = playerService.createPlayer(new PlayerCreationDTO("Frank", "Foster"));
            playerIds.add(player6.getId());
            LOG.info("Created player: {} {} (ID: {})", player6.getFirstName(), player6.getLastName(), player6.getId());

            // Create sample games with different player combinations
            Game game1 = gameService.createGame(List.of(playerIds.get(0), playerIds.get(1), playerIds.get(2)));
            LOG.info("Created game 1 (ID: {}) with 3 players: Alice, Bob, Charlie", game1.getId());

            Game game2 = gameService.createGame(List.of(playerIds.get(2), playerIds.get(3), playerIds.get(4), playerIds.get(5)));
            LOG.info("Created game 2 (ID: {}) with 4 players: Charlie, Diana, Eve, Frank", game2.getId());

            Game game3 = gameService.createGame(List.of(playerIds.get(0), playerIds.get(3)));
            LOG.info("Created game 3 (ID: {}) with 2 players: Alice, Diana", game3.getId());

            Game game4 = gameService.createGame(List.of(playerIds.get(1), playerIds.get(4)));
            LOG.info("Created game 4 (ID: {}) with 2 players: Bob, Eve", game4.getId());

            Game game5 = gameService.createGame(List.of(playerIds.get(0), playerIds.get(2), playerIds.get(4)));
            LOG.info("Created game 5 (ID: {}) with 3 players: Alice, Charlie, Eve", game5.getId());

            LOG.info("Data initialization completed successfully!");
            LOG.info("Summary: Created {} players and {} games", playerIds.size(), 5);
        };
    }
}

