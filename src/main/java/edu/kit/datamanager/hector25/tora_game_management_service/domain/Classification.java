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

    @ManyToOne
    @NonNull
    private final Image image;

    @NonNull
    private final Boolean decorated;

    public Classification(@NonNull Image image, @NonNull Boolean decorated) {
        this.image = image;
        this.decorated = decorated;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    @NonNull
    public Image getImage() {
        return image;
    }

    @NonNull
    public Boolean getDecorated() {
        return decorated;
    }

    @Override
    public String toString() {
        return "Classification{" + "id=" + id + ", image=" + image + ", decorated=" + decorated + '}';
    }

}
