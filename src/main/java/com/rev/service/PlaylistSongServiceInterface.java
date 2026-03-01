package com.rev.service;

import com.rev.dto.PlaylistSongDTO;

import java.util.List;

public interface PlaylistSongServiceInterface {

    PlaylistSongDTO addSongToPlaylist(Long playlistId, Long songId);

    void removeSongFromPlaylist(Long playlistId, Long songId);

    List<PlaylistSongDTO> getSongsByPlaylist(Long playlistId);

    void reorderPlaylist(Long playlistId, List<Long> songIdsInOrder);
}