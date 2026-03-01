package com.rev.restcontroller;

import com.rev.dto.AlbumDTO;
import com.rev.dto.SongsDTO;
import com.rev.service.AlbumServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumRestController {

    @Autowired
    private AlbumServiceInterface albumService;

    // Create album
    @PostMapping("/artists/{artistId}/albums")
    public AlbumDTO createAlbum(
            @PathVariable Long artistId,
            @RequestBody AlbumDTO albumDTO) {

        return albumService.createAlbum(artistId, albumDTO);
    }

    // Update album
    @PutMapping("/{id}")
    public AlbumDTO updateAlbum(@PathVariable Long id, @RequestBody AlbumDTO albumDTO) {
        return albumService.updateAlbum(id, albumDTO);
    }

    // Delete album
    @DeleteMapping("/{id}")
    public void deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
    }

    // Get album by ID
    @GetMapping("/{id}")
    public AlbumDTO getAlbumById(@PathVariable Long id) {
        return albumService.getAlbumById(id);
    }

    // Get all albums
    @GetMapping
    public List<AlbumDTO> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    // Get albums by artist
    @GetMapping("/artist/{artistId}")
    public List<AlbumDTO> getAlbumsByArtist(@PathVariable Long artistId) {
        return albumService.getAlbumsByArtist(artistId);
    }

    // Search albums by name
    @GetMapping("/search")
    public List<AlbumDTO> searchAlbums(@RequestParam String name) {
        return albumService.searchAlbumsByName(name);
    }

    // Add song to album
    @PostMapping("/{albumId}/songs/{songId}")
    public void addSongToAlbum(@PathVariable Long albumId, @PathVariable Long songId) {
        albumService.addSongToAlbum(albumId, songId);
    }

    // Remove song from album
    @DeleteMapping("/{albumId}/songs/{songId}")
    public void removeSongFromAlbum(@PathVariable Long albumId, @PathVariable Long songId) {
        albumService.removeSongFromAlbum(albumId, songId);
    }

    // Get all songs in album
    @GetMapping("/{albumId}/songs")
    public List<SongsDTO> getAlbumSongs(@PathVariable Long albumId) {
        return albumService.getAlbumSongs(albumId);
    }
}