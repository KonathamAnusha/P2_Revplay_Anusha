package com.rev.service;

import com.rev.dto.SongsDTO;
import com.rev.entity.ArtistProfile;
import com.rev.entity.Genre;
import com.rev.entity.Songs;
import com.rev.mapper.SongsMapper;
import com.rev.repository.AlbumRepository;
import com.rev.repository.ArtistRepository;
import com.rev.repository.GenreRepository;
import com.rev.repository.SongsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SongsServiceImplTest {

    @Mock
    private SongsRepository songsRepository;
    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private GenreRepository genreRepository;
    @Mock
    private SongsMapper songsMapper;
    @InjectMocks
    private SongsServiceImpl songsService;

    private Songs testSong;
    private SongsDTO testDTO;
    private ArtistProfile testArtist;

    @BeforeEach
    void setUp() {
        testArtist = ArtistProfile.builder().artistId(1L).stageName("TestArtist").build();
        Genre popGenre = Genre.builder().name("Pop").build();

        testSong = Songs.builder()
                .songId(1L)
                .title("Test Song")
                .genreName("Pop")
                .duration(240)
                .language("English")
                .artist(testArtist)
                .isPublic(true)
                .playCount(0L)
                .build();

        testDTO = SongsDTO.builder()
                .songId(1L)
                .title("Test Song")
                .genre("Pop")
                .duration(240)
                .language("English")
                .artistId(1L)
                .isPublic(true)
                .build();
    }

    @Test
    void createSong_Success() {
        when(artistRepository.findById(1L)).thenReturn(Optional.of(testArtist));
        when(genreRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(Genre.builder().name("Pop").build()));
        when(songsMapper.toEntity(eq(testDTO), eq(testArtist), isNull(), any(Genre.class))).thenReturn(testSong);
        when(songsRepository.save(testSong)).thenReturn(testSong);
        when(songsMapper.toDTO(testSong)).thenReturn(testDTO);

        SongsDTO result = songsService.createSong(testDTO);
        assertNotNull(result);
        assertEquals("Test Song", result.getTitle());
    }

    @Test
    void getSongById_Success() {
        when(songsRepository.findById(1L)).thenReturn(Optional.of(testSong));
        when(songsMapper.toDTO(testSong)).thenReturn(testDTO);

        SongsDTO result = songsService.getSongById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getSongId());
    }

    @Test
    void getSongById_NotFound() {
        when(songsRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> songsService.getSongById(999L));
    }

    @Test
    void getAllSongs() {
        when(songsRepository.findAll()).thenReturn(Arrays.asList(testSong));
        when(songsMapper.toDTO(testSong)).thenReturn(testDTO);

        List<SongsDTO> result = songsService.getAllSongs();
        assertEquals(1, result.size());
    }

    @Test
    void searchSongsByTitle() {
        when(songsRepository.findByTitleContainingIgnoreCase("Test")).thenReturn(Arrays.asList(testSong));
        when(songsMapper.toDTO(testSong)).thenReturn(testDTO);

        List<SongsDTO> result = songsService.searchSongsByTitle("Test");
        assertEquals(1, result.size());
    }

    @Test
    void filterSongsByGenre() {
        when(songsRepository.findByGenre_NameContainingIgnoreCase("Pop")).thenReturn(Arrays.asList(testSong));
        when(songsMapper.toDTO(testSong)).thenReturn(testDTO);

        List<SongsDTO> result = songsService.filterSongsByGenre("Pop");
        assertEquals(1, result.size());
    }

    @Test
    void filterSongsByLanguage() {
        when(songsRepository.findByLanguageContainingIgnoreCase("English")).thenReturn(Arrays.asList(testSong));
        when(songsMapper.toDTO(testSong)).thenReturn(testDTO);

        List<SongsDTO> result = songsService.filterSongsByLanguage("English");
        assertEquals(1, result.size());
    }

    @Test
    void getSongsByArtist() {
        when(songsRepository.findByArtist_ArtistId(1L)).thenReturn(Arrays.asList(testSong));
        when(songsMapper.toDTO(testSong)).thenReturn(testDTO);

        List<SongsDTO> result = songsService.getSongsByArtist(1L);
        assertEquals(1, result.size());
    }

    @Test
    void deleteSong() {
        doNothing().when(songsRepository).deleteById(1L);
        assertDoesNotThrow(() -> songsService.deleteSong(1L));
    }

    @Test
    void incrementPlayCount() {
        when(songsRepository.findById(1L)).thenReturn(Optional.of(testSong));
        when(songsRepository.save(any(Songs.class))).thenReturn(testSong);
        assertDoesNotThrow(() -> songsService.incrementPlayCount(1L));
    }

    @Test
    void getLatestSongs() {
        when(songsRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(testSong));
        when(songsMapper.toDTO(testSong)).thenReturn(testDTO);

        List<SongsDTO> result = songsService.getLatestSongs();
        assertEquals(1, result.size());
    }
}
