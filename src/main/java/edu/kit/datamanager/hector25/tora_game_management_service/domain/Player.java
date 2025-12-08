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
 * This is an example of a class that contains relevant information about players.
 * It matches a table in the database.
 */
@Entity
public class Player {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @NonNull
    @NotBlank(message = "First name must not be blank")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    private String firstName;

    @Column(nullable = false)
    @NonNull
    @NotBlank(message = "Last name must not be blank")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    private String lastName;

    @ManyToMany
    @NonNull
    private List<Game> games;

    public Player(@NonNull String firstName, @NonNull String lastName) {
        this(null, firstName, lastName);
    }

    public Player(UUID id, @NonNull String firstName, @NonNull String lastName) {
        this(id, firstName, lastName, List.of());
    }

    public Player(UUID id, @NonNull String firstName, @NonNull String lastName, @NonNull List<Game> games) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.games = games;
    }

    public Player() {
        this(null, "", "", List.of());
    }

    /**
     * Combines the first name and last name into a full name.
     *
     * @return The full name of the player.
     */
    public @NonNull String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getGames().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;
        boolean equals = true;
        equals &= getId().equals(player.getId());
        equals &= getFirstName().equals(player.getFirstName());
        equals &= getLastName().equals(player.getLastName());
        equals &= getGames().equals(player.getGames());
        return equals;
    }

    @Override
    public String toString() {
        String gameIds = games.stream().map(Game::getId).map(UUID::toString).reduce(", ", String::concat);

        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", games=" + games +
                '}';
    }

    public @NonNull UUID getId() {
        return id;
    }

    public @NonNull String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull String firstName) {
        this.firstName = firstName;
    }

    public @NonNull String getLastName() {
        return lastName;
    }

    public void setLastName(@NonNull String lastName) {
        this.lastName = lastName;
    }

    public @NonNull List<Game> getGames() {
        return games;
    }

    public void setGames(@NonNull List<Game> games) {
        this.games = games;
    }

    public void addGame(@NonNull Game game) {
        this.games.add(game);
    }

    public void removeGame(@NonNull Game game) {
        this.games.remove(game);
    }
}
