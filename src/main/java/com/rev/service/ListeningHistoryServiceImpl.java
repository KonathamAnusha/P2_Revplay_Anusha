package com.rev.service;

import com.rev.dto.ListeningHistoryDTO;
import com.rev.entity.ListeningHistory;
import com.rev.entity.Songs;
import com.rev.entity.UserAccount;
import com.rev.repository.ListeningHistoryRepository;
import com.rev.repository.SongsRepository;
import com.rev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ListeningHistoryServiceImpl implements ListeningHistoryServiceInterface {

    private final ListeningHistoryRepository historyRepo;
    private final UserRepository userRepo;
    private final SongsRepository songsRepo;

    // ---------------- ADD LISTENING HISTORY ----------------
    @Override
    public ListeningHistoryDTO addListeningHistory(Long userId, Long songId) {

        // ✅ Load user safely
        UserAccount user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // ✅ Load song safely
        Songs song = songsRepo.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with ID: " + songId));

        // ✅ Build the history
        ListeningHistory history = ListeningHistory.builder()
                .user(user)
                .song(song)
                .actionType(ListeningHistory.ActionType.PLAY)
                .playedAt(LocalDateTime.now())
                .build();

        // ✅ Save to DB
        ListeningHistory saved = historyRepo.saveAndFlush(history);

        // ✅ Convert to DTO
        return ListeningHistoryDTO.builder()
                .historyId(saved.getHistoryId())
                .userId(user.getUserId())
                .songId(song.getSongId())
                .playedAt(saved.getPlayedAt())
                .actionType(saved.getActionType().name())
                .build();
    }

    // ---------------- GET USER HISTORY ----------------
    @Override
    public List<ListeningHistoryDTO> getUserHistory(Long userId) {
        return historyRepo.findByUser_UserIdOrderByPlayedAtDesc(userId)
                .stream()
                .map(h -> ListeningHistoryDTO.builder()
                        .historyId(h.getHistoryId())
                        .userId(h.getUser().getUserId())
                        .songId(h.getSong().getSongId())
                        .playedAt(h.getPlayedAt())
                        .actionType(h.getActionType().name())
                        .build())
                .collect(Collectors.toList());
    }

    // ---------------- GET RECENT 50 HISTORY ----------------
    @Override
    public List<ListeningHistoryDTO> getRecentUserHistory(Long userId) {
        return historyRepo.findTop50ByUser_UserIdOrderByPlayedAtDesc(userId)
                .stream()
                .map(h -> ListeningHistoryDTO.builder()
                        .historyId(h.getHistoryId())
                        .userId(h.getUser().getUserId())
                        .songId(h.getSong().getSongId())
                        .playedAt(h.getPlayedAt())
                        .actionType(h.getActionType().name())
                        .build())
                .collect(Collectors.toList());
    }

    // ---------------- GET USER PLAY COUNT ----------------
    @Override
    public Long getUserPlayCount(Long userId) {
        return historyRepo.countByUser_UserId(userId);
    }

    // ---------------- GET TOP PLAYED SONGS ----------------
    @Override
    public List<Object[]> getTopPlayedSongs() {
        return historyRepo.findTopPlayedSongs();
    }

    // ---------------- CLEAR USER HISTORY ----------------
    @Override
    public void clearUserHistory(Long userId) {
        historyRepo.deleteByUser_UserId(userId);
    }
}