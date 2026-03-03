package com.rev.service;

import com.rev.dto.ArtistAnalyticsDTO;
import com.rev.dto.TopListenerDTO;
import com.rev.dto.TopSongsDTO;
import com.rev.repository.FavoriteRepository;
import com.rev.repository.ListeningHistoryRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistAnalyticsServiceImpl implements ArtistAnalyticsService {


    private final FavoriteRepository favoriteRepository;

    //@Autowired
    private final ListeningHistoryRepository historyRepository;

    @Override
    public ArtistAnalyticsDTO getArtistAnalytics(Long artistId) {

        // Total favorites and plays
        long totalFavorites = favoriteRepository.countFavoritesByArtist(artistId);
        long totalPlays = historyRepository.countPlaysByArtist(artistId);

        // Top 10 Songs (no need to map manually since repository returns DTOs)
        List<TopSongsDTO> topSongs = historyRepository.findTopSongsForArtist(artistId, PageRequest.of(0, 10));

        // Top 10 Listeners
        List<TopListenerDTO> topListeners = historyRepository.findTopListenersForArtist(artistId, PageRequest.of(0, 10));

        return ArtistAnalyticsDTO.builder()
                .totalFavorites(totalFavorites)
                .totalPlays(totalPlays)
                .topSongs(topSongs)
                .topListeners(topListeners)
                .build();
    }
}