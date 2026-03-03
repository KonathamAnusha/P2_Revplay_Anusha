package com.rev.service;

import com.rev.dto.ArtistDTO;
import com.rev.dto.TopListenerDTO;
import com.rev.entity.ArtistProfile;

import java.util.List;

public interface ArtistServiceInterface {

    ArtistProfile addOrUpdateArtistProfile(Long userId, ArtistDTO dto);

    ArtistProfile getArtistProfileById(Long artistId);

    ArtistProfile getArtistProfileByUserId(Long userId);

    List<ArtistProfile> getAllArtists();

    void deleteArtist(Long artistId);

    long getTotalFavorites(Long artistId);

    // ✅ FIXED: return DTO instead of Object[]
    List<TopListenerDTO> getTopListeners(Long artistId);

    List<Object[]> getPlayTrends(Long artistId);
}