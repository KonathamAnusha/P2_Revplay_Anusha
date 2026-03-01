package com.rev.service;

import com.rev.dto.AlbumDTO;
import com.rev.dto.SongsDTO;
import com.rev.entity.Album;
import com.rev.entity.ArtistProfile;
import com.rev.entity.Songs;
import com.rev.exception.AlbumNotFoundException;
import com.rev.mapper.AlbumMapper;
import com.rev.mapper.SongsMapper;
import com.rev.repository.AlbumRepository;
import com.rev.repository.ArtistRepository;
import com.rev.repository.SongsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements AlbumServiceInterface {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private SongsRepository songRepository;

    @Autowired
    private SongsMapper songMapper;


    @Override
    public AlbumDTO createAlbum(Long artistId, AlbumDTO albumDTO) {

        // 1️⃣ Fetch artist from database
        ArtistProfile artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + artistId));

        // 2️⃣ Convert DTO to Entity
        Album album = albumMapper.toEntity(albumDTO);

        // 3️⃣ Set artist to album
        album.setArtist(artist);

        // 4️⃣ Save album
        Album savedAlbum = albumRepository.save(album);

        // 5️⃣ Convert back to DTO
        return albumMapper.toDTO(savedAlbum);
    }

    @Override
    public AlbumDTO updateAlbum(Long id, AlbumDTO albumDTO) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        album.setName(albumDTO.getName());
        album.setDescription(albumDTO.getDescription());
        album.setReleaseDate(albumDTO.getReleaseDate()); // official release date from artist
        album.setCoverArt(albumDTO.getCoverArt());

        return albumMapper.toDTO(albumRepository.save(album));
    }

    @Override
    public void deleteAlbum(Long id) {
        albumRepository.deleteById(id);
    }

    @Override
    public AlbumDTO getAlbumById(Long id) {
        return albumRepository.findById(id)
                .map(albumMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Album not found"));
    }

    @Override
    public List<AlbumDTO> getAllAlbums() {
        return albumRepository.findAll().stream()
                .map(albumMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlbumDTO> getAlbumsByArtist(Long artistId) {
        return albumRepository.findByArtist_ArtistId(artistId).stream()
                .map(albumMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlbumDTO> searchAlbumsByName(String name) {
        return albumRepository.findByNameContainingIgnoreCase(name).stream()
                .map(albumMapper::toDTO)
                .collect(Collectors.toList());
    }



    @Override
    public void addSongToAlbum(Long albumId, Long songId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        Songs song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));
        song.setAlbum(album);  // set album for the song
        songRepository.save(song);
    }

    @Override
    public void removeSongFromAlbum(Long albumId, Long songId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        Songs song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));
        if (!song.getAlbum().getAlbumId().equals(albumId)) {
            throw new RuntimeException("Song does not belong to this album");
        }
        song.setAlbum(null);  // remove song from album
        songRepository.save(song);
    }

    @Override
    public List<SongsDTO> getAlbumSongs(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        return album.getSongs().stream()
                .map(songMapper::toDTO)
                .collect(Collectors.toList());
    }
}