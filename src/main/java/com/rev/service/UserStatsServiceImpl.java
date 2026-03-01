
package com.rev.service;

import com.rev.dto.UserStatsDTO;
import com.rev.repository.FavoriteRepository;
import com.rev.repository.PlaylistRepository;
import com.rev.repository.ListeningHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatsServiceImpl implements UserStatsServiceInterface {

    private final PlaylistRepository playlistRepo;
    private final FavoriteRepository favoriteRepo;
    private final ListeningHistoryRepository historyRepo;

    @Override
    public UserStatsDTO getUserStats(Long userId) {
        int totalPlaylists = playlistRepo.countByUserId(userId);   // count playlists
        int favoriteSongsCount = favoriteRepo.countByUserId(userId); // count favorites
        long totalListeningTime = historyRepo.sumDurationByUserId(userId); // total listening time in seconds

        return new UserStatsDTO(totalPlaylists, favoriteSongsCount, totalListeningTime);
    }
}