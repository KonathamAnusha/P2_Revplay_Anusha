package com.rev.service;

import com.rev.dto.AlbumDTO;
import com.rev.entity.Album;
import com.rev.entity.ArtistProfile;
import com.rev.mapper.AlbumMapper;
import com.rev.repository.AlbumRepository;
import com.rev.repository.ArtistRepository;
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

    @Override
    public AlbumDTO createAlbum(AlbumDTO albumDTO) {
        ArtistProfile artist = artistRepository.findById(albumDTO.getArtistId())
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        Album album = albumMapper.toEntity(albumDTO, artist);
        return albumMapper.toDTO(albumRepository.save(album));
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
}