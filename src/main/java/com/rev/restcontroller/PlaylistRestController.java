package com.rev.restcontroller;

import com.rev.dto.PlaylistDTO;
import com.rev.service.PlaylistServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistRestController {

    private final PlaylistServiceInterface playlistService;

    @PostMapping
    public PlaylistDTO createPlaylist(@RequestBody PlaylistDTO dto) {
        return playlistService.createPlaylist(dto);
    }

    @PutMapping("/{id}")
    public PlaylistDTO updatePlaylist(@PathVariable Long id, @RequestBody PlaylistDTO dto) {
        return playlistService.updatePlaylist(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
    }

    @GetMapping("/{id}")
    public PlaylistDTO getPlaylistById(@PathVariable Long id) {
        return playlistService.getPlaylistById(id);
    }

    @GetMapping
    public List<PlaylistDTO> getAllPlaylists() {
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/user/{userId}")
    public List<PlaylistDTO> getPlaylistsByUserId(@PathVariable Long userId) {
        return playlistService.getPlaylistsByUserId(userId);
    }

    @GetMapping("/privacy/{privacy}")
    public List<PlaylistDTO> getPlaylistsByPrivacy(@PathVariable String privacy) {
        return playlistService.getPlaylistsByPrivacy(privacy);
    }

    @GetMapping("/search")
    public List<PlaylistDTO> searchPlaylists(@RequestParam String name) {
        return playlistService.searchPlaylistsByName(name);
    }

    @GetMapping("/user/{userId}/privacy/{privacy}")
    public List<PlaylistDTO> getUserPlaylistsByPrivacy(@PathVariable Long userId,
                                                       @PathVariable String privacy) {
        return playlistService.getUserPlaylistsByPrivacy(userId, privacy);
    }

    @GetMapping("/song/{songId}")
    public List<PlaylistDTO> getPlaylistsBySongId(@PathVariable Long songId) {
        return playlistService.getPlaylistsBySongId(songId);
    }
}