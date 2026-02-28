package com.rev.restcontroller;

import com.rev.dto.ListeningHistoryDTO;
import com.rev.service.ListeningHistoryServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class ListeningHistoryRestController {

    private final ListeningHistoryServiceInterface historyService;

    // Add history
    @PostMapping
    public ResponseEntity<ListeningHistoryDTO> addHistory(
            @RequestParam Long userId,
            @RequestParam Long songId) {
        return ResponseEntity.ok(historyService.addListeningHistory(userId, songId));
    }

    // Get all history for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ListeningHistoryDTO>> getUserHistory(
            @PathVariable Long userId) {
        return ResponseEntity.ok(historyService.getUserHistory(userId));
    }

    // Get recent 50 songs
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<ListeningHistoryDTO>> getRecentHistory(
            @PathVariable Long userId) {
        return ResponseEntity.ok(historyService.getRecentUserHistory(userId));
    }

    // Get total play count
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getUserPlayCount(
            @PathVariable Long userId) {
        return ResponseEntity.ok(historyService.getUserPlayCount(userId));
    }

    // Get top played songs
    @GetMapping("/top")
    public ResponseEntity<List<Object[]>> getTopPlayedSongs() {
        return ResponseEntity.ok(historyService.getTopPlayedSongs());
    }

    // Clear history
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> clearUserHistory(
            @PathVariable Long userId) {
        historyService.clearUserHistory(userId);
        return ResponseEntity.noContent().build();
    }
}