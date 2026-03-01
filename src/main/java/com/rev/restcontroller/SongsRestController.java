package com.rev.restcontroller;

import com.rev.dto.SongsDTO;
import com.rev.dto.SongsDTO;
import com.rev.service.SongsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongsRestController {

    @Autowired
    private SongsServiceInterface songsService;

    // Create song
    @PostMapping
    public SongsDTO createSong(@RequestBody SongsDTO songDTO) {
        return songsService.createSong(songDTO);
    }

    // Update song
    @PutMapping("/{id}")
    public SongsDTO updateSong(@PathVariable Long id, @RequestBody SongsDTO songDTO) {
        return songsService.updateSong(id, songDTO);
    }

    // Delete song
    @DeleteMapping("/{id}")
    public void deleteSong(@PathVariable Long id) {
        songsService.deleteSong(id);
    }

    // Get song by ID
    @GetMapping("/{id}")
    public SongsDTO getSongById(@PathVariable Long id) {
        return songsService.getSongById(id);
    }

    // Get all songs
    @GetMapping
    public List<SongsDTO> getAllSongs() {
        return songsService.getAllSongs();
    }

    // Get songs by artist
    @GetMapping("/artist/{artistId}")
    public List<SongsDTO> getSongsByArtist(@PathVariable Long artistId) {
        return songsService.getSongsByArtist(artistId);
    }

    @PutMapping("/{id}/remove-from-album")
    public ResponseEntity<SongsDTO> removeFromAlbum(@PathVariable Long id) {
        return ResponseEntity.ok(songsService.removeSongFromAlbum(id));
    }

    // Get songs by album
    @GetMapping("/album/{albumId}")
    public List<SongsDTO> getSongsByAlbum(@PathVariable Long albumId) {
        return songsService.getSongsByAlbum(albumId);
    }

    // Search songs by title
    @GetMapping("/search")
    public List<SongsDTO> searchSongs(@RequestParam String title) {
        return songsService.searchSongsByTitle(title);
    }

    // Filter songs by genre
    @GetMapping("/filter/genre")
    public List<SongsDTO> filterByGenre(@RequestParam String genre) {
        return songsService.filterSongsByGenre(genre);
    }

    // Filter songs by language
    @GetMapping("/filter/language")
    public List<SongsDTO> filterByLanguage(@RequestParam String language) {
        return songsService.filterSongsByLanguage(language);
    }

    // Get latest 10 songs
    @GetMapping("/latest")
    public List<SongsDTO> latestSongs() {
        return songsService.getLatestSongs();
    }



    @PostMapping("/{id}/play")
    public ResponseEntity<Void> playSong(@PathVariable Long id) {
        songsService.incrementPlayCount(id);
        return ResponseEntity.ok().build();
    }
}