package com.rev.service;

import com.rev.dto.AlbumDTO;
import com.rev.dto.SongsDTO;

import java.util.List;

public interface AlbumServiceInterface {

    AlbumDTO createAlbum(Long artistId,AlbumDTO albumDTO);

    AlbumDTO updateAlbum(Long id, AlbumDTO albumDTO);

    void deleteAlbum(Long id);

    AlbumDTO getAlbumById(Long id);

    List<AlbumDTO> getAllAlbums();

    List<AlbumDTO> getAlbumsByArtist(Long artistId);

    List<AlbumDTO> searchAlbumsByName(String name);

    void addSongToAlbum(Long albumId, Long songId);

    void removeSongFromAlbum(Long albumId, Long songId);

    List<SongsDTO> getAlbumSongs(Long albumId);

    void incrementPlayCount(Long songId);
}