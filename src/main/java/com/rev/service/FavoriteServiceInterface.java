package com.rev.service;

import com.rev.dto.FavoriteDTO;

import java.util.List;

public interface FavoriteServiceInterface {

    // Add song to favorites
    FavoriteDTO addFavorite(Long userId, Long songId);

    // Remove song from favorites
    void removeFavorite(Long userId, Long songId);

    // Get all favorite songs of a user
    List<FavoriteDTO> getUserFavorites(Long userId);

    // Get all favorites of a song
    List<FavoriteDTO> getFavoritesBySong(Long songId);

    // Get user IDs who favorited artist songs
    List<Long> getUsersWhoFavoritedArtist(Long artistId);

    // Count total favorites for an artist
    long getArtistTotalFavorites(Long artistId);
}