package com.rev.service;

import com.rev.dto.PlaylistDTO;

import java.util.List;

public interface PlaylistServiceInterface {

    PlaylistDTO createPlaylist(PlaylistDTO playlistDTO);

    PlaylistDTO updatePlaylist(Long id, PlaylistDTO playlistDTO);

    void deletePlaylist(Long id);

    PlaylistDTO getPlaylistById(Long id);

    List<PlaylistDTO> getAllPlaylists();

    List<PlaylistDTO> getPlaylistsByUserId(Long userId);

    List<PlaylistDTO> getPlaylistsByPrivacy(String privacy);

    List<PlaylistDTO> searchPlaylistsByName(String name);

    List<PlaylistDTO> getUserPlaylistsByPrivacy(Long userId, String privacy);

    List<PlaylistDTO> getPlaylistsBySongId(Long songId);
}