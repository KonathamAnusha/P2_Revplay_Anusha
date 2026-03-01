package com.rev.restcontroller;

import com.rev.dto.ArtistAnalyticsDTO;
import com.rev.dto.ArtistDTO;
import com.rev.entity.ArtistProfile;
import com.rev.mapper.ArtistMapper;
import com.rev.service.ArtistAnalyticsService;
import com.rev.service.ArtistServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/artists")
@RequiredArgsConstructor
public class ArtistRestController {

    private final ArtistServiceInterface artistService;
    private final ArtistMapper artistMapper;
    private ArtistAnalyticsService artistAnalyticsService;

    // ------------------- ADD OR UPDATE ARTIST PROFILE -------------------
    @PostMapping("/profile/{userId}")
    public ResponseEntity<ArtistDTO> addOrUpdateArtistProfile(
            @PathVariable Long userId,
            @RequestBody ArtistDTO artistDTO
    ) {
        ArtistProfile profile = artistService.addOrUpdateArtistProfile(userId, artistDTO);
        return ResponseEntity.ok(artistMapper.toDTO(profile));
    }

    // ------------------- GET ARTIST PROFILE BY ARTIST ID -------------------
    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistDTO> getArtistProfileById(@PathVariable Long artistId) {
        ArtistProfile profile = artistService.getArtistProfileById(artistId);
        return ResponseEntity.ok(artistMapper.toDTO(profile));
    }

    // ------------------- GET ARTIST PROFILE BY USER ID -------------------
    @GetMapping("/user/{userId}")
    public ResponseEntity<ArtistDTO> getArtistProfileByUserId(@PathVariable Long userId) {
        ArtistProfile profile = artistService.getArtistProfileByUserId(userId);
        return ResponseEntity.ok(artistMapper.toDTO(profile));
    }

    // ------------------- GET ALL ARTISTS -------------------
    @GetMapping
    public ResponseEntity<List<ArtistDTO>> getAllArtists() {
        List<ArtistDTO> artists = artistService.getAllArtists()
                .stream()
                .map(artistMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(artists);
    }

    // ------------------- DELETE ARTIST PROFILE -------------------
    @DeleteMapping("/{artistId}")
    public ResponseEntity<String> deleteArtist(@PathVariable Long artistId) {
        artistService.deleteArtist(artistId);
        return ResponseEntity.ok("Artist deleted successfully");
    }

    @GetMapping("/{id}/analytics")
    public ResponseEntity<Map<String, Object>> getArtistAnalytics(@PathVariable Long id) {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalFavorites", artistService.getTotalFavorites(id));
        analytics.put("topListeners", artistService.getTopListeners(id));
        analytics.put("playTrends", artistService.getPlayTrends(id));
        return ResponseEntity.ok(analytics);
    }


    @PreAuthorize("hasRole('ARTIST')")
    @GetMapping("/{artistId}/analytics")
    public ResponseEntity<ArtistAnalyticsDTO> getAnalytics(@PathVariable Long artistId) {
        return ResponseEntity.ok(artistAnalyticsService.getArtistAnalytics(artistId));
    }

}