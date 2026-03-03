package com.rev.service;

import com.rev.dto.PlaylistSongDTO;
import com.rev.entity.Playlist;
import com.rev.entity.PlaylistSong;
import com.rev.entity.Songs;
import com.rev.mapper.PlaylistSongMapper;
import com.rev.repository.PlaylistRepository;
import com.rev.repository.PlaylistSongRepository;
import com.rev.repository.SongsRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistSongServiceImplTest {

    @Mock
    private PlaylistSongRepository playlistSongRepository;
    @Mock
    private PlaylistRepository playlistRepository;
    @Mock
    private SongsRepository songsRepository;
    @Mock
    private PlaylistSongMapper playlistSongMapper;

    @InjectMocks
    private PlaylistSongServiceImpl playlistSongService;

    private Playlist testPlaylist;
    private Songs testSong;
    private PlaylistSong testPlaylistSong;
    private PlaylistSongDTO testDTO;

    @BeforeEach
    void setUp() {
        testPlaylist = new Playlist();
        testPlaylist.setPlaylistId(1L);

        testSong = new Songs();
        testSong.setSongId(1L);

        testPlaylistSong = PlaylistSong.builder()
                .playlist(testPlaylist)
                .song(testSong)
                .orderIndex(1)
                .build();

        testDTO = new PlaylistSongDTO();
        testDTO.setPlaylistId(1L);
        testDTO.setSongId(1L);
        testDTO.setOrderIndex(1);
    }

    @Test
    void addSongToPlaylist_Success() {
        when(playlistRepository.findById(1L)).thenReturn(Optional.of(testPlaylist));
        when(songsRepository.findById(1L)).thenReturn(Optional.of(testSong));
        when(playlistSongRepository.existsByPlaylist_PlaylistIdAndSong_SongId(1L, 1L)).thenReturn(false);
        when(playlistSongRepository.findMaxOrderIndexByPlaylistId(1L)).thenReturn(0);
        when(playlistSongRepository.save(any(PlaylistSong.class))).thenReturn(testPlaylistSong);
        when(playlistSongMapper.toDTO(any(PlaylistSong.class))).thenReturn(testDTO);

        PlaylistSongDTO result = playlistSongService.addSongToPlaylist(1L, 1L);

        assertNotNull(result);
        assertEquals(1, result.getOrderIndex());
        verify(playlistSongRepository).save(any(PlaylistSong.class));
    }

    @Test
    void addSongToPlaylist_AlreadyExists() {
        when(playlistRepository.findById(1L)).thenReturn(Optional.of(testPlaylist));
        when(songsRepository.findById(1L)).thenReturn(Optional.of(testSong));
        when(playlistSongRepository.existsByPlaylist_PlaylistIdAndSong_SongId(1L, 1L)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> playlistSongService.addSongToPlaylist(1L, 1L));
    }

    @Test
    void removeSongFromPlaylist_Success() {
        when(playlistSongRepository.existsByPlaylist_PlaylistIdAndSong_SongId(1L, 1L)).thenReturn(true);
        doNothing().when(playlistSongRepository).deleteByPlaylist_PlaylistIdAndSong_SongId(1L, 1L);

        assertDoesNotThrow(() -> playlistSongService.removeSongFromPlaylist(1L, 1L));
    }

    @Test
    void removeSongFromPlaylist_NotFound() {
        when(playlistSongRepository.existsByPlaylist_PlaylistIdAndSong_SongId(1L, 1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> playlistSongService.removeSongFromPlaylist(1L, 1L));
    }

    @Test
    void getSongsByPlaylist() {
        when(playlistSongRepository.findByPlaylist_PlaylistIdOrderByOrderIndex(1L)).thenReturn(Arrays.asList(testPlaylistSong));
        when(playlistSongMapper.toDTO(testPlaylistSong)).thenReturn(testDTO);

        List<PlaylistSongDTO> result = playlistSongService.getSongsByPlaylist(1L);
        assertEquals(1, result.size());
    }

    @Test
    void reorderPlaylist_Success() {
        List<PlaylistSong> existingSongs = new ArrayList<>();
        existingSongs.add(testPlaylistSong);

        when(playlistSongRepository.findByPlaylist_PlaylistIdOrderByOrderIndex(1L)).thenReturn(existingSongs);
        when(playlistSongRepository.save(any(PlaylistSong.class))).thenReturn(testPlaylistSong);

        assertDoesNotThrow(() -> playlistSongService.reorderPlaylist(1L, Arrays.asList(1L)));
        verify(playlistSongRepository).save(any(PlaylistSong.class));
    }
}
