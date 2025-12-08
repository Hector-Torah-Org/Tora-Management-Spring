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

package edu.kit.datamanager.hector25.tora_game_management_service.exceptions;

/**
 * Exception thrown when a game is not found in the database.
 */
public class GameNotFoundException extends EntityNotFoundException {

    /**
     * Constructs a new GameNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public GameNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new GameNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public GameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

