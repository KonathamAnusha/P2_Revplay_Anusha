package com.rev.service;

import com.rev.dto.ArtistDTO;
import com.rev.entity.ArtistProfile;

import java.util.List;

public interface ArtistServiceInterface {

    // Create or update artist profile for a user
    ArtistProfile addOrUpdateArtistProfile(Long userId, ArtistDTO artistDTO);

    // Get artist profile by artist ID
    ArtistProfile getArtistProfileById(Long artistId);

    // Get artist profile by user ID
    ArtistProfile getArtistProfileByUserId(Long userId);

    // Get all artists
    List<ArtistProfile> getAllArtists();

    // Delete artist by artist ID
    void deleteArtist(Long artistId);

    long getTotalFavorites(Long artistId); // Total favorites for all songs of the artist
    List<Object[]> getTopListeners(Long artistId); // List of users with play counts
    List<Object[]> getPlayTrends(Long artistId); // Plays per day/week/month
}