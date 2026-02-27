package com.rev.service;

import com.rev.dto.SongsDTO;

import java.util.List;

public interface SongsServiceInterface {

    SongsDTO createSong(SongsDTO songDTO);

    SongsDTO updateSong(Long id, SongsDTO songDTO);

    void deleteSong(Long id);

    SongsDTO getSongById(Long id);

    List<SongsDTO> getAllSongs();

    List<SongsDTO> getSongsByArtist(Long artistId);

    List<SongsDTO> getSongsByAlbum(Long albumId);

    List<SongsDTO> searchSongsByTitle(String title);

    List<SongsDTO> filterSongsByGenre(String genre);

    List<SongsDTO> filterSongsByLanguage(String language);

    List<SongsDTO> getLatestSongs();

}