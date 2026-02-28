package com.rev.restcontroller;

import com.rev.dto.FavoriteDTO;
import com.rev.service.FavoriteServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteRestController {

    private final FavoriteServiceInterface favoriteService;

    // Add to favorites
    @PostMapping
    public FavoriteDTO addFavorite(@RequestParam Long userId,
                                   @RequestParam Long songId) {
        return favoriteService.addFavorite(userId, songId);
    }

    // Remove from favorites
    @DeleteMapping
    public void removeFavorite(@RequestParam Long userId,
                               @RequestParam Long songId) {
        favoriteService.removeFavorite(userId, songId);
    }

    // Get all favorite songs of a user
    @GetMapping("/user/{userId}")
    public List<FavoriteDTO> getUserFavorites(@PathVariable Long userId) {
        return favoriteService.getUserFavorites(userId);
    }

    // Get all users who favorited a specific song
    @GetMapping("/song/{songId}")
    public List<FavoriteDTO> getFavoritesBySong(@PathVariable Long songId) {
        return favoriteService.getFavoritesBySong(songId);
    }

    // Artist analytics: users who favorited artist songs
    @GetMapping("/artist/{artistId}/users")
    public List<Long> getUsersWhoFavoritedArtist(@PathVariable Long artistId) {
        return favoriteService.getUsersWhoFavoritedArtist(artistId);
    }

    // Artist analytics: total favorites
    @GetMapping("/artist/{artistId}/count")
    public long getArtistTotalFavorites(@PathVariable Long artistId) {
        return favoriteService.getArtistTotalFavorites(artistId);
    }
}