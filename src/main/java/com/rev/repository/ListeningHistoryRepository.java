package com.rev.repository;

import com.rev.entity.ListeningHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {

    // All history for a user
    List<ListeningHistory> findByUser_UserIdOrderByPlayedAtDesc(Long userId);

    // Recent 50 songs
    List<ListeningHistory> findTop50ByUser_UserIdOrderByPlayedAtDesc(Long userId);

    // Total plays for a user
    long countByUser_UserId(Long userId);

    // Top played songs overall
    @Query("""
           SELECT lh.song.songId, COUNT(lh) as playCount
           FROM ListeningHistory lh
           GROUP BY lh.song.songId
           ORDER BY playCount DESC
           """)
    List<Object[]> findTopPlayedSongs();

    // Clear history
    void deleteByUser_UserId(Long userId);

    @Query("SELECT COALESCE(SUM(l.song.duration), 0) FROM ListeningHistory l WHERE l.user.userId = :userId")
    Long sumDurationByUserId(@Param("userId") Long userId);

}