///*
// * Copyright (c) 2025 Karlsruhe Institute of Technology.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package edu.kit.datamanager.hector25.tora_game_management_service.service.impl;
//
//import edu.kit.datamanager.hector25.tora_game_management_service.dao.IPlayerDao;
//import edu.kit.datamanager.hector25.tora_game_management_service.dao.ISessionDao;
//import edu.kit.datamanager.hector25.tora_game_management_service.domain.Player;
//import edu.kit.datamanager.hector25.tora_game_management_service.domain.Session;
//import edu.kit.datamanager.hector25.tora_game_management_service.exceptions.GameNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for GameService.
// * Tests all business logic and error scenarios.
// */
//@ExtendWith(MockitoExtension.class)
//class GameServiceTest {
//
//    @Mock
//    private ISessionDao sessionDao;
//
//    @Mock
//    private IPlayerDao playerDao;
//
//    @InjectMocks
//    private SessionService sessionService;
//
//    private UUID testGameId;
//    private UUID testPlayerId1;
//    private UUID testPlayerId2;
//    private Session testSession;
//    private Player testPlayer1;
//    private Player testPlayer2;
//    private List<UUID> playerIds;
//    private String testGameName;
//
//    @BeforeEach
//    void setUp() {
//        testGameId = UUID.randomUUID();
//        testPlayerId1 = UUID.randomUUID();
//        testPlayerId2 = UUID.randomUUID();
//        testGameName = "Test Game";
//
//        testPlayer1 = new Player(testPlayerId1, "John", "Doe", "new ArrayList<>()");
//        testPlayer2 = new Player(testPlayerId2, "Jane", "Smith", "new ArrayList<>()");
//
//        testSession = new Session(testGameId, testPlayer1);
//        playerIds = List.of(testPlayerId1, testPlayerId2);
//    }
//
//    // ==================== CREATE GAME TESTS ====================
//
//    @Test
//    void testCreateGame_Success() {
//        when(playerDao.findPlayerById(testPlayerId1)).thenReturn(Optional.of(testPlayer1));
//        when(playerDao.findPlayerById(testPlayerId2)).thenReturn(Optional.of(testPlayer2));
//        when(sessionDao.save(any(Session.class))).thenReturn(testSession);
//
//        Session result = sessionService.createSession(testPlayerId1);
//
//        assertNotNull(result);
//        verify(sessionDao, times(1)).save(any(Session.class));
//        verify(playerDao, times(2)).findPlayerById(any());
//    }
//
//    @Test
//    void testCreateGame_SavesToDatabase() {
//        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
//        when(playerDao.findPlayerById(testPlayerId1)).thenReturn(Optional.of(testPlayer1));
//        when(playerDao.findPlayerById(testPlayerId2)).thenReturn(Optional.of(testPlayer2));
//
//        sessionService.createSession(testPlayerId1);
//
//        verify(sessionDao).save(captor.capture());
//        Session savedSession = captor.getValue();
//        assertEquals(savedSession.getPlayer(),  testPlayer1);
//    }
//
//    // ==================== GET GAME BY ID TESTS ====================
//
//    @Test
//    void testGetGameById_Success() {
//        when(sessionDao.findSessionById(testGameId)).thenReturn(Optional.of(testSession));
//
//        Optional<Session> result = sessionService.getSession(testGameId);
//
//        assertTrue(result.isPresent());
//        assertEquals(testGameId, result.get().getSessionId());
//    }
//
//    @Test
//    void testGetGameById_NotFound() {
//        when(sessionDao.findSessionById(testGameId)).thenReturn(Optional.empty());
//
//        Optional<Session> result = sessionService.getSession(testGameId);
//
//        assertFalse(result.isPresent());
//    }
//
//    // ==================== GET PLAYERS FOR GAME TESTS ====================
//
//    @Test
//    void testGetPlayersForGame_Success() {
//        when(sessionDao.findSessionById(testGameId)).thenReturn(Optional.of(testSession));
//
//        Optional<Session> result = sessionService.getSession(testGameId);
//
//        assertNotNull(result);
//        assertTrue(result.get().getPlayer() == testPlayer1);
//    }
//
//    @Test
//    void testGetPlayersForGame_GameNotFound() {
//        when(sessionDao.findSessionById(testGameId)).thenReturn(Optional.empty());
//
//        assertThrows(GameNotFoundException.class, () ->
//                sessionService.getSession(testGameId)
//        );
//    }
//
//  //  @Test
//  //  void testGetPlayersForGame_EmptyGame() {
//  //      Session emptyGame = new Session(testGameId, testPlayer1);
//  //      when(sessionDao.findSessionById(testGameId)).thenReturn(Optional.of(emptyGame));
////
//  //      Optional<Session> result = sessionService.getSession(testGameId);
////
//  //      assertNotNull(result);
//  //      assertEquals(null, result);
//  //  }
//
//
//}
//
//