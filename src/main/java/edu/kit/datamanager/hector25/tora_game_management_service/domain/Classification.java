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
 * Entity representing a Classification, which contains an ImageID and the Classification by the User.
 * This class is mapped to a database table using JPA annotations.
 */

@Entity
public class Classification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    private final UUID imageId;

    @NonNull
    private final Boolean decorated;

    @NonNull
    private final UUID sessionId;

    public Classification(@NonNull UUID imageId, @NonNull Boolean decorated,  @NonNull UUID sessionId) {
        this.imageId = imageId;
        this.decorated = decorated;
        this.sessionId = sessionId;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    @NonNull
    public UUID getImageId() {
        return imageId;
    }

    @NonNull
    public Boolean getDecorated() {
        return decorated;
    }

    @NonNull
    public UUID getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "Classification{" + "id=" + id + ", image=" + imageId + ", decorated=" + decorated + '}';
    }

}
