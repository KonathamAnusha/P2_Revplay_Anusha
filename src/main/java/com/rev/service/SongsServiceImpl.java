package com.rev.service;

import com.rev.dto.SongsDTO;
import com.rev.entity.Album;
import com.rev.entity.ArtistProfile;
import com.rev.entity.Songs;
import com.rev.mapper.SongsMapper;
import com.rev.repository.AlbumRepository;
import com.rev.repository.ArtistRepository;
import com.rev.repository.GenreRepository;
import com.rev.repository.SongsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rev.repository.FavoriteRepository;
import com.rev.repository.PlaylistSongRepository;
import com.rev.repository.ListeningHistoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongsServiceImpl implements SongsServiceInterface {

    @Autowired
    private SongsRepository songsRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    @Autowired
    private ListeningHistoryRepository listeningHistoryRepository;

    @Autowired
    private SongsMapper songsMapper;

    @Override
    public SongsDTO createSong(SongsDTO songDTO) {
        ArtistProfile artist = artistRepository.findById(songDTO.getArtistId())
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        Album album = null;
        if (songDTO.getAlbumId() != null) {
            album = albumRepository.findById(songDTO.getAlbumId())
                    .orElseThrow(() -> new RuntimeException("Album not found"));
        }

        // Handle Genre
        com.rev.entity.Genre genre = genreRepository.findByNameIgnoreCase(songDTO.getGenre())
                .orElseGet(() -> {
                    com.rev.entity.Genre newGenre = com.rev.entity.Genre.builder()
                            .name(songDTO.getGenre())
                            .build();
                    return genreRepository.save(newGenre);
                });

        Songs song = songsMapper.toEntity(songDTO, artist, album, genre);
        return songsMapper.toDTO(songsRepository.save(song));
    }

    @Override
    public SongsDTO updateSong(Long id, SongsDTO songDTO) {
        Songs song = songsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        song.setTitle(songDTO.getTitle());
        song.setDuration(songDTO.getDuration());
        song.setSongUrl(songDTO.getSongUrl());
        song.setLanguage(songDTO.getLanguage());

        // Handle Genre update
        if (songDTO.getGenre() != null) {
            com.rev.entity.Genre genre = genreRepository.findByNameIgnoreCase(songDTO.getGenre())
                    .orElseGet(() -> {
                        com.rev.entity.Genre newGenre = com.rev.entity.Genre.builder()
                                .name(songDTO.getGenre())
                                .build();
                        return genreRepository.save(newGenre);
                    });
            song.setGenre(genre);
        }

        if (songDTO.getAlbumId() != null) {
            Album album = albumRepository.findById(songDTO.getAlbumId())
                    .orElseThrow(() -> new RuntimeException("Album not found"));
            song.setAlbum(album);
        }

        return songsMapper.toDTO(songsRepository.save(song));
    }

    @Override
    @Transactional
    public void deleteSong(Long id) {
        // Delete related child records to avoid integrity constraint violations
        favoriteRepository.deleteBySong_SongId(id);
        playlistSongRepository.deleteBySong_SongId(id);
        listeningHistoryRepository.deleteBySong_SongId(id);

        songsRepository.deleteById(id);
    }

    @Override
    public SongsDTO getSongById(Long id) {
        return songsRepository.findById(id)
                .map(songsMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Song not found"));
    }

    @Override
    public SongsDTO removeSongFromAlbum(Long songId) {
        Songs song = songsRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        song.setAlbum(null);

        return songsMapper.toDTO(songsRepository.save(song));
    }

    @Override
    public List<SongsDTO> getAllSongs() {
        return songsRepository.findAll().stream()
                .map(songsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongsDTO> getSongsByArtist(Long artistId) {
        return songsRepository.findByArtist_ArtistId(artistId).stream()
                .map(songsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public org.springframework.data.domain.Page<SongsDTO> getSongsByArtistPaginated(Long artistId,
            org.springframework.data.domain.Pageable pageable) {
        return songsRepository.findByArtist_ArtistId(artistId, pageable)
                .map(songsMapper::toDTO);
    }

    @Override
    public List<SongsDTO> getSongsByAlbum(Long albumId) {
        return songsRepository.findByAlbum_AlbumId(albumId).stream()
                .map(songsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongsDTO> searchSongsByTitle(String title) {
        return songsRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(songsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongsDTO> filterSongsByGenre(String genre) {
        return songsRepository.findByGenre_NameContainingIgnoreCase(genre).stream()
                .map(songsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongsDTO> filterSongsByLanguage(String language) {
        return songsRepository.findByLanguageContainingIgnoreCase(language).stream()
                .map(songsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongsDTO> getLatestSongs() {
        return songsRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .map(songsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void incrementPlayCount(Long songId) {
        Songs song = songsRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        song.setPlayCount(song.getPlayCount() + 1);
        songsRepository.save(song);
    }
}