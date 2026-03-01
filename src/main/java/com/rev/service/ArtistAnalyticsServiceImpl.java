package com.rev.service;

import com.rev.dto.ArtistAnalyticsDTO;
import com.rev.repository.FavoriteRepository;
import com.rev.repository.ListeningHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistAnalyticsServiceImpl implements ArtistAnalyticsService {

    private final FavoriteRepository favoriteRepository;
    private final ListeningHistoryRepository historyRepository;

    @Override
    public ArtistAnalyticsDTO getArtistAnalytics(Long artistId) {

        long totalFavorites = favoriteRepository.countFavoritesByArtist(artistId);
        long totalPlays = historyRepository.countPlaysByArtist(artistId);

        // Top Songs
        List<ArtistAnalyticsDTO.TopSongDTO> topSongs = historyRepository.findTopSongsByArtist(artistId)
                .stream()
                .map(obj -> ArtistAnalyticsDTO.TopSongDTO.builder()
                        .songId((Long) obj[0])
                        .songTitle((String) obj[1])
                        .playCount(((Number) obj[2]).longValue())
                        .build())
                .collect(Collectors.toList());

        // Top Listeners
        List<ArtistAnalyticsDTO.TopListenerDTO> topListeners = historyRepository.findTopListenersByArtist(artistId)
                .stream()
                .map(obj -> ArtistAnalyticsDTO.TopListenerDTO.builder()
                        .userId((Long) obj[0])
                        .userName((String) obj[1])
                        .playCount(((Number) obj[2]).longValue())
                        .build())
                .collect(Collectors.toList());

        return ArtistAnalyticsDTO.builder()
                .totalFavorites(totalFavorites)
                .totalPlays(totalPlays)
                .topSongs(topSongs)
                .topListeners(topListeners)
                .build();
    }
}