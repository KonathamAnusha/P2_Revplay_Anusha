package com.rev.service;

import com.rev.dto.ArtistAnalyticsDTO;
import com.rev.dto.TopListenerDTO;
import com.rev.dto.TopSongsDTO;
import com.rev.repository.FavoriteRepository;
import com.rev.repository.ListeningHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistAnalyticsServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ListeningHistoryRepository historyRepository;

    @InjectMocks
    private ArtistAnalyticsServiceImpl artistAnalyticsService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getArtistAnalytics_Success() {
        Long artistId = 1L;
        when(favoriteRepository.countFavoritesByArtist(artistId)).thenReturn(5L);
        when(historyRepository.countPlaysByArtist(artistId)).thenReturn(100L);

        List<TopSongsDTO> topSongs = new ArrayList<>();
        when(historyRepository.findTopSongsForArtist(eq(artistId), any(PageRequest.class))).thenReturn(topSongs);

        List<TopListenerDTO> topListeners = new ArrayList<>();
        when(historyRepository.findTopListenersForArtist(eq(artistId), any(PageRequest.class)))
                .thenReturn(topListeners);

        ArtistAnalyticsDTO result = artistAnalyticsService.getArtistAnalytics(artistId);

        assertNotNull(result);
        assertEquals(5L, result.getTotalFavorites());
        assertEquals(100L, result.getTotalPlays());
        assertEquals(topSongs, result.getTopSongs());
        assertEquals(topListeners, result.getTopListeners());

        verify(favoriteRepository).countFavoritesByArtist(artistId);
        verify(historyRepository).countPlaysByArtist(artistId);
        verify(historyRepository).findTopSongsForArtist(eq(artistId), any(PageRequest.class));
        verify(historyRepository).findTopListenersForArtist(eq(artistId), any(PageRequest.class));
    }
}
