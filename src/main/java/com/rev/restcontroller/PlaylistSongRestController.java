package com.rev.restcontroller;

import com.rev.dto.PlaylistSongDTO;
import com.rev.service.PlaylistSongServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlist-songs")
@RequiredArgsConstructor
public class PlaylistSongRestController {

    private final PlaylistSongServiceInterface playlistSongService;

    // Add song to playlist
    @PostMapping
    public ResponseEntity<PlaylistSongDTO> addSong(@RequestParam Long playlistId,
                                                   @RequestParam Long songId) {
        return ResponseEntity.ok(playlistSongService.addSongToPlaylist(playlistId, songId));
    }

    // Remove song from playlist
    @DeleteMapping
    public ResponseEntity<Void> removeSong(@RequestParam Long playlistId,
                                           @RequestParam Long songId) {
        playlistSongService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.noContent().build();
    }

    // Get all songs in a playlist
    @GetMapping("/playlist/{playlistId}")
    public ResponseEntity<List<PlaylistSongDTO>> getPlaylistSongs(@PathVariable Long playlistId) {
        return ResponseEntity.ok(playlistSongService.getSongsByPlaylist(playlistId));
    }

    // Reorder playlist
    @PutMapping("/playlist/{playlistId}/reorder")
    public ResponseEntity<Void> reorderPlaylist(@PathVariable Long playlistId,
                                                @RequestBody List<Long> songIdsInOrder) {
        playlistSongService.reorderPlaylist(playlistId, songIdsInOrder);
        return ResponseEntity.ok().build();
    }
}