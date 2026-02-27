package com.rev.service;

import com.rev.dto.AlbumDTO;

import java.util.List;

public interface AlbumServiceInterface {

    AlbumDTO createAlbum(AlbumDTO albumDTO);

    AlbumDTO updateAlbum(Long id, AlbumDTO albumDTO);

    void deleteAlbum(Long id);

    AlbumDTO getAlbumById(Long id);

    List<AlbumDTO> getAllAlbums();

    List<AlbumDTO> getAlbumsByArtist(Long artistId);

    List<AlbumDTO> searchAlbumsByName(String name);
}