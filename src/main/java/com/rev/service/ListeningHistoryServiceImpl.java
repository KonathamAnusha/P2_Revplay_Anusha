package com.rev.service;

import com.rev.dto.ListeningHistoryDTO;
import com.rev.entity.ListeningHistory;
import com.rev.entity.Songs;
import com.rev.entity.UserAccount;
import com.rev.mapper.ListeningHistoryMapper;
import com.rev.repository.ListeningHistoryRepository;
import com.rev.repository.SongsRepository;
import com.rev.repository.UserRepository;
import com.rev.service.ListeningHistoryServiceInterface;
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

    private final ListeningHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final SongsRepository songRepository;
    private final ListeningHistoryMapper mapper;

    @Override
    public ListeningHistoryDTO addListeningHistory(Long userId, Long songId) {

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Songs song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + songId));

        ListeningHistory history = ListeningHistory.builder()
                .user(user)
                .song(song)
                .actionType("PLAY")
                .playedAt(LocalDateTime.now())
                .build();

        ListeningHistory saved = historyRepository.save(history);

        return mapper.toDTO(saved);
    }

    @Override
    public List<ListeningHistoryDTO> getUserHistory(Long userId) {
        return historyRepository.findByUser_UserIdOrderByPlayedAtDesc(userId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListeningHistoryDTO> getRecentUserHistory(Long userId) {
        return historyRepository.findTop10ByUser_UserIdOrderByPlayedAtDesc(userId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public long getUserPlayCount(Long userId) {
        return historyRepository.countByUser_UserId(userId);
    }

    @Override
    public List<Object[]> getTopPlayedSongs() {
        return historyRepository.findTopPlayedSongs();
    }

    @Override
    public void clearUserHistory(Long userId) {
        historyRepository.deleteByUser_UserId(userId);
    }
}