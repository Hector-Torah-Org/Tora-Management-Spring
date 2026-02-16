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

package edu.kit.datamanager.hector25.tora_game_management_service.service;

import edu.kit.datamanager.hector25.tora_game_management_service.domain.Image;

import java.util.Optional;
import java.util.UUID;

public interface IImageService {
    public Image createImage(Boolean decorated, String link);

    public Optional<Image> getImage(UUID imageId);

    public Image getImageToClassify();

    public Image getTestImage();

    public Image getImageToClassifyForPlayer(UUID playerId);

    public Image getTestImageForPlayer(UUID playerId);
}
