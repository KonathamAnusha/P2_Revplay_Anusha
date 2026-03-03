package com.rev.service;

import com.rev.dto.FavoriteDTO;
import com.rev.entity.Favorite;
import com.rev.entity.Songs;
import com.rev.entity.UserAccount;
import com.rev.mapper.FavoriteMapper;
import com.rev.repository.FavoriteRepository;
import com.rev.repository.SongsRepository;
import com.rev.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SongsRepository songsRepository;
    @Mock
    private FavoriteMapper favoriteMapper;
    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private UserAccount testUser;
    private Songs testSong;
    private Favorite testFavorite;
    private FavoriteDTO testDTO;

    @BeforeEach
    void setUp() {
        testUser = new UserAccount();
        testUser.setUserId(1L);
        testUser.setEmail("test@example.com");

        testSong = Songs.builder().songId(1L).title("Test Song").build();

        testFavorite = Favorite.builder()
                .favoriteId(1L)
                .user(testUser)
                .song(testSong)
                .build();

        testDTO = FavoriteDTO.builder()
                .favoriteId(1L).userId(1L).songId(1L)
                .createdAt(LocalDateTime.now()).build();
    }

    @Test
    void addFavorite_Success() {
        // actual impl checks existsBy FIRST, then finds user, then finds song
        when(favoriteRepository.existsByUser_UserIdAndSong_SongId(1L, 1L)).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(songsRepository.findById(1L)).thenReturn(Optional.of(testSong));
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(testFavorite);
        when(favoriteMapper.toDTO(testFavorite)).thenReturn(testDTO);

        FavoriteDTO result = favoriteService.addFavorite(1L, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getSongId());
    }

    @Test
    void addFavorite_AlreadyExists() {
        when(favoriteRepository.existsByUser_UserIdAndSong_SongId(1L, 1L)).thenReturn(true);
        assertThrows(RuntimeException.class, () -> favoriteService.addFavorite(1L, 1L));
    }

    @Test
    void getUserFavorites() {
        when(favoriteRepository.findByUser_UserId(1L)).thenReturn(Arrays.asList(testFavorite));
        when(favoriteMapper.toDTO(testFavorite)).thenReturn(testDTO);

        List<FavoriteDTO> result = favoriteService.getUserFavorites(1L);
        assertEquals(1, result.size());
    }

    @Test
    void removeFavorite_Success() {
        when(favoriteRepository.existsByUser_UserIdAndSong_SongId(1L, 1L)).thenReturn(true);
        doNothing().when(favoriteRepository).deleteByUser_UserIdAndSong_SongId(1L, 1L);
        assertDoesNotThrow(() -> favoriteService.removeFavorite(1L, 1L));
    }

    @Test
    void removeFavorite_NotFound() {
        when(favoriteRepository.existsByUser_UserIdAndSong_SongId(1L, 1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> favoriteService.removeFavorite(1L, 1L));
    }

    @Test
    void getArtistTotalFavorites() {
        when(favoriteRepository.countFavoritesByArtist(1L)).thenReturn(5L);
        long count = favoriteService.getArtistTotalFavorites(1L);
        assertEquals(5, count);
    }
}
