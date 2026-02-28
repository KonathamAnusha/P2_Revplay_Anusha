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

    // Add listening record
    @PostMapping
    public ResponseEntity<ListeningHistoryDTO> addHistory(
            @RequestParam Long userId,
            @RequestParam Long songId) {

        return ResponseEntity.ok(
                historyService.addListeningHistory(userId, songId)
        );
    }

    // Get full user history
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ListeningHistoryDTO>> getUserHistory(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                historyService.getUserHistory(userId)
        );
    }

    // Get recent 10 songs
    @GetMapping("/user/{userId}/recent")
    public ResponseEntity<List<ListeningHistoryDTO>> getRecentHistory(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                historyService.getRecentUserHistory(userId)
        );
    }



    // User total play count
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getUserPlayCount(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                historyService.getUserPlayCount(userId)
        );
    }

    // 🔥 Top Played Songs
    @GetMapping("/top")
    public ResponseEntity<List<Object[]>> getTopPlayedSongs() {

        return ResponseEntity.ok(
                historyService.getTopPlayedSongs()
        );
    }

    // 🔥 Clear user history
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> clearUserHistory(
            @PathVariable Long userId) {

        historyService.clearUserHistory(userId);
        return ResponseEntity.noContent().build();
    }
}