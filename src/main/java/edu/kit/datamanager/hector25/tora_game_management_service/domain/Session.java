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

import java.util.UUID;

/**
 * Entity representing a Session, which contains a list of Classifications.
 * This class is mapped to a database table using JPA annotations.
 */

@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private final UUID playerId;

    public Session(){
    this(null, null);
    }

    public Session(UUID id, UUID playerId){
        this.id = id;
        this.playerId = playerId;
    }

    public UUID getSessionId() {
        return id;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    @Override
    public String toString() {
        return "Session{" + "id=" + id + ", playerId=" + playerId + '}';
    }
}
