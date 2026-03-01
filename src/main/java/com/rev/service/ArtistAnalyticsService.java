package com.rev.service;

import com.rev.dto.ArtistAnalyticsDTO;

public interface ArtistAnalyticsService {
    ArtistAnalyticsDTO getArtistAnalytics(Long artistId);
}