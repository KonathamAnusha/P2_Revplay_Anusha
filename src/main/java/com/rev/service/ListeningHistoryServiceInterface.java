package com.rev.service;

import com.rev.dto.ListeningHistoryDTO;

import java.util.List;

public interface ListeningHistoryServiceInterface {

    // Add listening history
    ListeningHistoryDTO addListeningHistory(Long userId, Long songId);

    // Get full user history
    List<ListeningHistoryDTO> getUserHistory(Long userId);

    // Get recent 10 songs of user
    List<ListeningHistoryDTO> getRecentUserHistory(Long userId);


    // Get user play count
    long getUserPlayCount(Long userId);

    // 🔥 Top Played Songs
    List<Object[]> getTopPlayedSongs();

    // 🔥 Clear user history
    void clearUserHistory(Long userId);
}