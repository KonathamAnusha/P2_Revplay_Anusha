package com.rev.repository;

import com.rev.entity.PlaylistSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, Long> {

    // Get all songs in a playlist ordered by orderIndex
    List<PlaylistSong> findByPlaylist_PlaylistIdOrderByOrderIndex(Long playlistId);

    // Check if a song exists in a playlist
    boolean existsByPlaylist_PlaylistIdAndSong_SongId(Long playlistId, Long songId);

    // Delete a specific song from a playlist
    void deleteByPlaylist_PlaylistIdAndSong_SongId(Long playlistId, Long songId);

    // Count songs in a playlist
    int countByPlaylist_PlaylistId(Long playlistId);

    // Get max orderIndex for a playlist (to append new song at end)
    @Query("SELECT COALESCE(MAX(ps.orderIndex), 0) FROM PlaylistSong ps WHERE ps.playlist.playlistId = :playlistId")
    int findMaxOrderIndexByPlaylistId(Long playlistId);

    // Optional: Get all playlist songs for multiple playlists
    List<PlaylistSong> findByPlaylist_PlaylistIdInOrderByOrderIndex(List<Long> playlistIds);
}