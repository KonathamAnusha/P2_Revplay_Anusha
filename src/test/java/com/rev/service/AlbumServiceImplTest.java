package com.rev.service;

import com.rev.dto.AlbumDTO;
import com.rev.entity.Album;
import com.rev.entity.ArtistProfile;
import com.rev.mapper.AlbumMapper;
import com.rev.mapper.SongsMapper;
import com.rev.repository.AlbumRepository;
import com.rev.repository.ArtistRepository;
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
class AlbumServiceImplTest {

    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private SongsRepository songRepository;
    @Mock
    private AlbumMapper albumMapper;
    @Mock
    private SongsMapper songMapper;
    @InjectMocks
    private AlbumServiceImpl albumService;

    private Album testAlbum;
    private AlbumDTO testDTO;
    private ArtistProfile testArtist;

    @BeforeEach
    void setUp() {
        testArtist = ArtistProfile.builder().artistId(1L).stageName("TestArtist").build();

        testAlbum = Album.builder()
                .albumId(1L)
                .name("Test Album")
                .description("Description")
                .artist(testArtist)
                .songs(new ArrayList<>())
                .build();

        testDTO = AlbumDTO.builder()
                .albumId(1L)
                .name("Test Album")
                .description("Description")
                .artistId(1L)
                .build();
    }

    @Test
    void createAlbum_Success() {
        when(artistRepository.findById(1L)).thenReturn(Optional.of(testArtist));
        when(albumMapper.toEntity(testDTO, testArtist)).thenReturn(testAlbum);
        when(albumRepository.save(testAlbum)).thenReturn(testAlbum);
        when(albumMapper.toDTO(testAlbum)).thenReturn(testDTO);

        AlbumDTO result = albumService.createAlbum(1L, testDTO);
        assertNotNull(result);
        assertEquals("Test Album", result.getName());
    }

    @Test
    void getAlbumById_Success() {
        when(albumRepository.findById(1L)).thenReturn(Optional.of(testAlbum));
        when(albumMapper.toDTO(testAlbum)).thenReturn(testDTO);

        AlbumDTO result = albumService.getAlbumById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getAlbumId());
    }

    @Test
    void getAlbumById_NotFound() {
        when(albumRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> albumService.getAlbumById(999L));
    }

    @Test
    void getAllAlbums() {
        when(albumRepository.findAll()).thenReturn(Arrays.asList(testAlbum));
        when(albumMapper.toDTO(testAlbum)).thenReturn(testDTO);

        List<AlbumDTO> result = albumService.getAllAlbums();
        assertEquals(1, result.size());
    }

    @Test
    void getAlbumsByArtist() {
        when(albumRepository.findByArtist_ArtistId(1L)).thenReturn(Arrays.asList(testAlbum));
        when(albumMapper.toDTO(testAlbum)).thenReturn(testDTO);

        List<AlbumDTO> result = albumService.getAlbumsByArtist(1L);
        assertEquals(1, result.size());
    }

    @Test
    void searchAlbumsByName() {
        when(albumRepository.findByNameContainingIgnoreCase("Test")).thenReturn(Arrays.asList(testAlbum));
        when(albumMapper.toDTO(testAlbum)).thenReturn(testDTO);

        List<AlbumDTO> result = albumService.searchAlbumsByName("Test");
        assertEquals(1, result.size());
    }

    @Test
    void deleteAlbum() {
        doNothing().when(albumRepository).deleteById(1L);
        assertDoesNotThrow(() -> albumService.deleteAlbum(1L));
    }
}
