package com.rev.service;

import com.rev.dto.ListeningHistoryDTO;
import com.rev.entity.ListeningHistory;
import com.rev.entity.Songs;
import com.rev.entity.UserAccount;
import com.rev.repository.ListeningHistoryRepository;
import com.rev.repository.SongsRepository;
import com.rev.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListeningHistoryServiceImplTest {

    @Mock
    private ListeningHistoryRepository historyRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private SongsRepository songsRepo;
    @InjectMocks
    private ListeningHistoryServiceImpl historyService;

    private UserAccount testUser;
    private Songs testSong;
    private ListeningHistory testHistory;

    @BeforeEach
    void setUp() {
        testUser = new UserAccount();
        testUser.setUserId(1L);
        testUser.setEmail("test@example.com");

        testSong = Songs.builder().songId(1L).title("Test Song").duration(240).build();

        testHistory = ListeningHistory.builder()
                .historyId(1L)
                .user(testUser)
                .song(testSong)
                .playedAt(LocalDateTime.now())
                .actionType(ListeningHistory.ActionType.PLAY)
                .build();
    }

    @Test
    void addListeningHistory_Success() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(songsRepo.findById(1L)).thenReturn(Optional.of(testSong));
        when(historyRepo.saveAndFlush(any(ListeningHistory.class))).thenReturn(testHistory);

        ListeningHistoryDTO result = historyService.addListeningHistory(1L, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getSongId());
        assertEquals("PLAY", result.getActionType());
    }

    @Test
    void getUserHistory() {
        when(historyRepo.findByUser_UserIdOrderByPlayedAtDesc(1L))
                .thenReturn(Arrays.asList(testHistory));

        List<ListeningHistoryDTO> result = historyService.getUserHistory(1L);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getSongId());
    }

    @Test
    void getRecentUserHistory() {
        when(historyRepo.findTop50ByUser_UserIdOrderByPlayedAtDesc(1L))
                .thenReturn(Arrays.asList(testHistory));

        List<ListeningHistoryDTO> result = historyService.getRecentUserHistory(1L);
        assertEquals(1, result.size());
    }

    @Test
    void getUserPlayCount() {
        when(historyRepo.countByUser_UserId(1L)).thenReturn(10L);
        Long count = historyService.getUserPlayCount(1L);
        assertEquals(10L, count);
    }

    @Test
    void clearUserHistory() {
        doNothing().when(historyRepo).deleteByUser_UserId(1L);
        assertDoesNotThrow(() -> historyService.clearUserHistory(1L));
    }

    @Test
    void getTopPlayedSongs() {
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(new Object[] { 1L, 50L });
        when(historyRepo.findTopPlayedSongs()).thenReturn(mockResult);

        List<Object[]> result = historyService.getTopPlayedSongs();
        assertEquals(1, result.size());
    }
}
