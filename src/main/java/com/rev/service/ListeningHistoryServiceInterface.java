package com.rev.service;

import com.rev.dto.ArtistAnalyticsDTO;
import com.rev.dto.ListeningHistoryDTO;
import com.rev.dto.TopListenerDTO;
import com.rev.dto.TopSongsDTO;

import java.util.List;

public interface ListeningHistoryServiceInterface {

    ListeningHistoryDTO addListeningHistory(Long userId, Long songId);

    List<ListeningHistoryDTO> getUserHistory(Long userId);

    List<ListeningHistoryDTO> getRecentUserHistory(Long userId);

    Long getUserPlayCount(Long userId);

    List<Object[]> getTopPlayedSongs();

    void clearUserHistory(Long userId);


    long getTotalPlaysForArtist(Long artistId);
    List<TopSongsDTO> getTopSongsForArtist(Long artistId);
    List<TopListenerDTO> getTopListenersForArtist(Long artistId);
}