package com.rev.service;

import com.rev.dto.ListeningHistoryDTO;
import java.util.List;

public interface ListeningHistoryServiceInterface {

    ListeningHistoryDTO addListeningHistory(Long userId, Long songId);

    List<ListeningHistoryDTO> getUserHistory(Long userId);

    List<ListeningHistoryDTO> getRecentUserHistory(Long userId);

    Long getUserPlayCount(Long userId);

    List<Object[]> getTopPlayedSongs();

    void clearUserHistory(Long userId);
}