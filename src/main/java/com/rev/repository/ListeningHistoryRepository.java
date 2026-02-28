package com.rev.repository;

import com.rev.entity.ListeningHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {

    // Get all listening history of a user
    List<ListeningHistory> findByUser_UserIdOrderByPlayedAtDesc(Long userId);

    // Get listening history of a specific song
    List<ListeningHistory> findBySong_SongId(Long songId);



    // Count total plays by a user
    long countByUser_UserId(Long userId);

    // Get recent 10 songs played by user
    List<ListeningHistory> findTop10ByUser_UserIdOrderByPlayedAtDesc(Long userId);

    // -------------------------------------------------
    // 🔥 CUSTOM METHODS
    // -------------------------------------------------

    // 1️⃣ Top Played Songs (Trending)
    @Query("""
           SELECT lh.song.songId, COUNT(lh) as playCount
           FROM ListeningHistory lh
           GROUP BY lh.song.songId
           ORDER BY playCount DESC
           """)
    List<Object[]> findTopPlayedSongs();



    // 3️⃣ Delete all listening history of a user (Clear History)
    void deleteByUser_UserId(Long userId);
}