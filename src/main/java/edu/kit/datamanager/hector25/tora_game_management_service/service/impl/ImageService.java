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

package edu.kit.datamanager.hector25.tora_game_management_service.service.impl;

import edu.kit.datamanager.hector25.tora_game_management_service.dao.IImageDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Image;
import edu.kit.datamanager.hector25.tora_game_management_service.service.IImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ImageService implements IImageService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);
    private final IImageDao imageDao;

    public ImageService(IImageDao imageDao) {
        this.imageDao = imageDao;
    }

    public Image createImage(Boolean decorated, String link) {
        Image image = new Image(decorated, link);
        imageDao.save(image);
        LOG.info("Image created with id: {}, link: {}, isDecorated: {}", image.getId(), image.getLink(), image.isDecorated());
        return image;
    }

    public Image createImage(String link) {
        Image image = new Image(link);
        imageDao.save(image);
        LOG.info("Image created with id: {}, link: {}", image.getId(), image.getLink());
        return image;
    }

    public Optional<Image> getImage(UUID imageId){
        return imageDao.findById(imageId);
    }

    public Image getImageToClassify(){
        return null;
    }

    public Image getTestImage(){
        return null;
    }

    public Image getImageToClassifyForPlayer(UUID playerId){
        return null;
    }

    public Image getTestImageForPlayer(UUID playerId){
        return null;
    }

}