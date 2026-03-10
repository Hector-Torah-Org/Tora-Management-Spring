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

package edu.kit.datamanager.hector25.tora_game_management_service.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link edu.kit.datamanager.hector25.tora_game_management_service.domain.Player}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PlayerCreationDTO(
        @Size(message = "First name must be between 1 and 100 characters", min = 1, max = 100) @NotBlank(message = "First name must not be blank") String firstName,
        @Size(message = "Last name must be between 1 and 100 characters", min = 1, max = 100) @NotBlank(message = "Last name must not be blank") String lastName,
        @Size(message = "User name must be between 1 and 100 characters", min = 1, max = 100) @NotBlank(message = "First name must not be blank") String userName) implements Serializable {
}