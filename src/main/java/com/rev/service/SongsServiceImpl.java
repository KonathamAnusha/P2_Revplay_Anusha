package com.rev.service;

import com.rev.dto.SongsDTO;
import com.rev.entity.Album;
import com.rev.entity.ArtistProfile;
import com.rev.entity.Songs;
import com.rev.mapper.SongsMapper;
import com.rev.repository.AlbumRepository;
import com.rev.repository.ArtistRepository;
import com.rev.repository.SongsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Songs song = songsMapper.toEntity(songDTO, artist, album);
        return songsMapper.toDTO(songsRepository.save(song));
    }

    @Override
    public SongsDTO updateSong(Long id, SongsDTO songDTO) {
        Songs song = songsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        song.setTitle(songDTO.getTitle());
        song.setGenre(songDTO.getGenre());
        song.setDuration(songDTO.getDuration());
        song.setSongUrl(songDTO.getSongUrl());
        song.setLanguage(songDTO.getLanguage());

        if (songDTO.getAlbumId() != null) {
            Album album = albumRepository.findById(songDTO.getAlbumId())
                    .orElseThrow(() -> new RuntimeException("Album not found"));
            song.setAlbum(album);
        }

        return songsMapper.toDTO(songsRepository.save(song));
    }

    @Override
    public void deleteSong(Long id) {
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
        return songsRepository.findByGenreContainingIgnoreCase(genre).stream()
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