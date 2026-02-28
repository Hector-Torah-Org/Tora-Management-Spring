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
import org.jspecify.annotations.NonNull;

import java.util.UUID;

/**
 * Entity representing an Image from the Dataset, which contains an ImageID, the Classification, if given, and the IIIF-Link.
 * This class is mapped to a database table using JPA annotations.
 */

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Boolean decorated;

    @NonNull
    private String link;

    private Character character;

    public Image(@NonNull String link, @NonNull Character character) {
        this.link = link;
        this.decorated = null;
        this.character = character;
    }

    protected Image() {}

    public Image(Boolean decorated, @NonNull String link,  @NonNull Character character) {
        this.decorated = decorated;
        this.link = link;
        this.character = character;
    }

    public UUID getId() {
        return id;
    }

    public Boolean isDecorated() {
        return decorated;
    }

    public Character getCharacter() {
        return character;
    }

    public @NonNull String getLink() {
        return link;
    }

    public String toString(){
        return "Image{" + "id=" + id + ", decorated=" + decorated + ", link=" + link + ", character=" + character + '}';
    }
}
