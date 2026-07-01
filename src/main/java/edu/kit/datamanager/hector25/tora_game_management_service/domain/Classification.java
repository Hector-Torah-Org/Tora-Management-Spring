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
import org.hibernate.annotations.CreationTimestamp;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
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
    @ManyToOne(optional = false)
    private Image image;

    private Boolean decorated;
    private boolean isDatasetError;

    @NonNull
    @ManyToOne(optional = false)
    private Session session;

    private Boolean correct;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Nullable
    //between 0 and 1, 0 if the user gets everything wrong, 1 if he is correct every time
    private Double confidence;
    private boolean confidenceIsFinal;


    public Classification(@NonNull Image image, @NonNull Boolean decorated,  @NonNull Session session) {
        this.image = image;
        this.decorated = decorated;
        this.session = session;
        this.confidenceIsFinal = false;
    }

    public Classification(@NonNull Image image,   @NonNull Session session, @NonNull boolean isDatasetError){
        this.image = image;
        this.isDatasetError = isDatasetError;
        this.session = session;
        this.confidenceIsFinal = false;
    }

    public Classification(@NonNull Image image, @NonNull Boolean decorated,  @NonNull Session session, Boolean correct) {
        this.image = image;
        this.decorated = decorated;
        this.session = session;
        this.correct = correct;
        this.confidence = null;
        this.confidenceIsFinal = false;
    }

    protected Classification() {}

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

    @NonNull
    public Session getSession() {
        return session;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public void setConfidenceToFinal() {
        this.confidenceIsFinal = true;
    }

    public Double getConfidence() {
        return confidence;
    }

    public boolean isConfidenceIsFinal() {
        return confidenceIsFinal;
    }

    public boolean getIsDatasetError() {
        return isDatasetError;
    }

    @Override
    public String toString() {
        return "Classification{" + "id=" + id + ", image=" + image + ", session=" + session + ", decorated=" + decorated + ", correct = " + correct + ", confidence = " + confidence + ", confidenceIsFinal=" + confidenceIsFinal + '}';
    }

}
