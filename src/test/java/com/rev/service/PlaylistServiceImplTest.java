package com.rev.service;

import com.rev.dto.PlaylistDTO;
import com.rev.entity.Playlist;
import com.rev.entity.Songs;
import com.rev.entity.UserAccount;
import com.rev.mapper.PlaylistMapper;
import com.rev.repository.PlaylistRepository;
import com.rev.repository.SongsRepository;
import com.rev.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceImplTest {

    @Mock
    private PlaylistRepository playlistRepository;
    @Mock
    private PlaylistMapper playlistMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SongsRepository songsRepository;
    @InjectMocks
    private PlaylistServiceImpl playlistService;

    private Playlist testPlaylist;
    private PlaylistDTO testDTO;
    private UserAccount testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserAccount();
        testUser.setUserId(1L);
        testUser.setEmail("test@example.com");

        testPlaylist = new Playlist();
        testPlaylist.setPlaylistId(1L);
        testPlaylist.setName("My Playlist");
        testPlaylist.setDescription("Test playlist");
        testPlaylist.setPrivacy("PUBLIC");
        testPlaylist.setUser(testUser);
        testPlaylist.setPlaylistSongs(new ArrayList<>());

        testDTO = PlaylistDTO.builder()
                .playlistId(1L)
                .name("My Playlist")
                .description("Test playlist")
                .privacy("PUBLIC")
                .userId(1L)
                .songIds(new ArrayList<>())
                .build();
    }

    @Test
    void createPlaylist_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(songsRepository.findAllById(anyList())).thenReturn(new ArrayList<>());
        when(playlistMapper.toEntity(eq(testDTO), eq(testUser), anyList())).thenReturn(testPlaylist);
        when(playlistRepository.save(testPlaylist)).thenReturn(testPlaylist);
        when(playlistMapper.toDTO(testPlaylist)).thenReturn(testDTO);

        PlaylistDTO result = playlistService.createPlaylist(testDTO);
        assertNotNull(result);
        assertEquals("My Playlist", result.getName());
    }

    @Test
    void getPlaylistById_Success() {
        when(playlistRepository.findById(1L)).thenReturn(Optional.of(testPlaylist));
        when(playlistMapper.toDTO(testPlaylist)).thenReturn(testDTO);

        PlaylistDTO result = playlistService.getPlaylistById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getPlaylistId());
    }

    @Test
    void getPlaylistById_NotFound() {
        when(playlistRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> playlistService.getPlaylistById(999L));
    }

    @Test
    void getPlaylistsByUserId() {
        when(playlistRepository.findByUser_UserId(1L)).thenReturn(Arrays.asList(testPlaylist));
        when(playlistMapper.toDTO(testPlaylist)).thenReturn(testDTO);

        List<PlaylistDTO> result = playlistService.getPlaylistsByUserId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void searchPlaylistsByName() {
        when(playlistRepository.findByNameContainingIgnoreCase("My")).thenReturn(Arrays.asList(testPlaylist));
        when(playlistMapper.toDTO(testPlaylist)).thenReturn(testDTO);

        List<PlaylistDTO> result = playlistService.searchPlaylistsByName("My");
        assertEquals(1, result.size());
    }

    @Test
    void deletePlaylist() {
        when(playlistRepository.findById(1L)).thenReturn(Optional.of(testPlaylist));
        doNothing().when(playlistRepository).delete(testPlaylist);
        assertDoesNotThrow(() -> playlistService.deletePlaylist(1L));
    }

    @Test
    void getPlaylistsByPrivacy() {
        when(playlistRepository.findByPrivacy("PUBLIC")).thenReturn(Arrays.asList(testPlaylist));
        when(playlistMapper.toDTO(testPlaylist)).thenReturn(testDTO);

        List<PlaylistDTO> result = playlistService.getPlaylistsByPrivacy("PUBLIC");
        assertEquals(1, result.size());
    }
}
