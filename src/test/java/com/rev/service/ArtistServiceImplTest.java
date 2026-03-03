package com.rev.service;

import com.rev.dto.ArtistDTO;
import com.rev.dto.TopListenerDTO;
import com.rev.entity.ArtistProfile;
import com.rev.entity.UserAccount;
import com.rev.mapper.ArtistMapper;
import com.rev.repository.ArtistRepository;
import com.rev.repository.FavoriteRepository;
import com.rev.repository.ListeningHistoryRepository;
import com.rev.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceImplTest {

    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ArtistMapper artistMapper;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private ListeningHistoryRepository historyRepository;

    @InjectMocks
    private ArtistServiceImpl artistService;

    private UserAccount testUser;
    private ArtistProfile testProfile;
    private ArtistDTO testDTO;

    @BeforeEach
    void setUp() {
        testUser = new UserAccount();
        testUser.setUserId(1L);
        testUser.setEmail("artist@example.com");

        testProfile = new ArtistProfile();
        testProfile.setArtistId(1L);
        testProfile.setStageName("Stage Name");
        testProfile.setGenre("Rock");
        testProfile.setUserAccount(testUser);

        testDTO = new ArtistDTO();
        testDTO.setStageName("Stage Name");
        testDTO.setGenre("Rock");
        testDTO.setBio("Bio");
    }

    @Test
    void addOrUpdateArtistProfile_CreateNew() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(artistMapper.toEntity(eq(testDTO), eq(testUser))).thenReturn(testProfile);
        when(artistRepository.save(any(ArtistProfile.class))).thenReturn(testProfile);

        ArtistProfile result = artistService.addOrUpdateArtistProfile(1L, testDTO);

        assertNotNull(result);
        assertEquals("Stage Name", result.getStageName());
        verify(artistRepository).save(any(ArtistProfile.class));
    }

    @Test
    void addOrUpdateArtistProfile_UpdateExisting() {
        testUser.setArtistProfile(testProfile);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(artistRepository.save(any(ArtistProfile.class))).thenReturn(testProfile);

        ArtistProfile result = artistService.addOrUpdateArtistProfile(1L, testDTO);

        assertNotNull(result);
        verify(artistRepository).save(testProfile);
        verify(artistMapper, never()).toEntity(any(), any());
    }

    @Test
    void getArtistProfileById_Success() {
        when(artistRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        ArtistProfile result = artistService.getArtistProfileById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getArtistId());
    }

    @Test
    void getArtistProfileById_NotFound() {
        when(artistRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> artistService.getArtistProfileById(999L));
    }

    @Test
    void getArtistProfileByUserId_Success() {
        testUser.setArtistProfile(testProfile);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        ArtistProfile result = artistService.getArtistProfileByUserId(1L);
        assertNotNull(result);
    }

    @Test
    void getAllArtists() {
        when(artistRepository.findAll()).thenReturn(Arrays.asList(testProfile));
        List<ArtistProfile> result = artistService.getAllArtists();
        assertEquals(1, result.size());
    }

    @Test
    void deleteArtist() {
        when(artistRepository.findById(1L)).thenReturn(Optional.of(testProfile));
        doNothing().when(artistRepository).delete(testProfile);
        assertDoesNotThrow(() -> artistService.deleteArtist(1L));
    }

    @Test
    void getTotalFavorites() {
        when(favoriteRepository.countFavoritesByArtist(1L)).thenReturn(10L);
        assertEquals(10L, artistService.getTotalFavorites(1L));
    }

    @Test
    void getTopListeners() {
        List<TopListenerDTO> listeners = new ArrayList<>();
        when(historyRepository.findTopListenersForArtist(eq(1L), any())).thenReturn(listeners);

        List<TopListenerDTO> result = artistService.getTopListeners(1L);
        assertNotNull(result);
    }

    @Test
    void getPlayTrends() {
        List<Object[]> trends = new ArrayList<>();
        when(historyRepository.findPlayTrendsByArtist(1L)).thenReturn(trends);

        List<Object[]> result = artistService.getPlayTrends(1L);
        assertNotNull(result);
    }
}
