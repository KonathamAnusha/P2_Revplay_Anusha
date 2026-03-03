package com.rev.service;

import com.rev.dto.UserStatsDTO;
import com.rev.repository.FavoriteRepository;
import com.rev.repository.ListeningHistoryRepository;
import com.rev.repository.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStatsServiceImplTest {

    @Mock
    private ListeningHistoryRepository historyRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private UserStatsServiceImpl userStatsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getUserStats_Success() {
        Long userId = 1L;
        when(playlistRepository.countByUserId(userId)).thenReturn(5);
        when(favoriteRepository.countByUser_UserId(userId)).thenReturn(20);
        when(historyRepository.sumDurationByUserId(userId)).thenReturn(3600L);

        UserStatsDTO result = userStatsService.getUserStats(userId);

        assertNotNull(result);
        assertEquals(5, result.getTotalPlaylists());
        assertEquals(20, result.getFavoriteSongsCount());
        assertEquals(3600L, result.getTotalListeningTime());

        verify(playlistRepository).countByUserId(userId);
        verify(favoriteRepository).countByUser_UserId(userId);
        verify(historyRepository).sumDurationByUserId(userId);
    }
}
