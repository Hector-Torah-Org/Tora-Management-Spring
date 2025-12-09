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

package edu.kit.datamanager.hector25.tora_game_management_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing a Game, which contains a list of Players.
 * This class is mapped to a database table using JPA annotations.
 */
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NonNull
    @NotBlank(message = "Game name must not be blank")
    @Size(min = 1, max = 255, message = "Game name must be between 1 and 255 characters")
    private String name;

    /**
     * List of players participating in the game.
     * This is a many-to-many relationship, as a player can participate in multiple games.
     */
    @ManyToMany
    @JoinTable(
            name = "game_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "players_id")
    )
    @NonNull
    private List<Player> players;

    public Game() {
        this(null, "", List.of());
    }

    public Game(UUID id, @NonNull String name, @NonNull List<Player> players) {
        super();
        this.id = id;
        this.name = name;
        this.players = players;
    }

    public Game(@NonNull String name, @NonNull List<Player> players) {
        this(null, name, players);
    }

    public void addPlayer(@NonNull Player player) {
        players.add(player);
    }

    public void removePlayer(@NonNull Player player) {
        players.remove(player);
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getPlayers().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;
        return getId().equals(game.getId()) &&
                getName().equals(game.getName()) &&
                getPlayers().equals(game.getPlayers());
    }

    @Override
    public String toString() {
        String playerIDs = getPlayers().stream()
                .map(Player::getId)
                .map(UUID::toString)
                .reduce(", ", String::concat);

        return "Game{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", players=" + playerIDs +
                '}';
    }

    public @NonNull UUID getId() {
        return id;
    }

    public @NonNull String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public @NonNull List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(@NonNull List<Player> players) {
        this.players = players;
    }
}
