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

import edu.kit.datamanager.hector25.tora_game_management_service.dao.ISessionDao;
import edu.kit.datamanager.hector25.tora_game_management_service.domain.Session;
import edu.kit.datamanager.hector25.tora_game_management_service.service.ISessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService implements ISessionService {

    private static final Logger LOG = LoggerFactory.getLogger(SessionService.class);

    private final ISessionDao sessionDao;

    SessionService(ISessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    @Override
    public Session createSession(UUID playerId) {
        return sessionDao.save(new Session(null, playerId));
    }

    @Override
    public Optional<Session> getSession(UUID id) {
        return sessionDao.findSessionById(id);
    }

    @Override
    public List<Session> getSessionsOfPlayer(UUID playerId) {
        return sessionDao.findSessionsByPlayerId(playerId);
    }

}
