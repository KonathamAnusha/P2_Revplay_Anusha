package com.rev.repository;

import com.rev.entity.ListeningHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {

    // ---------------- USER HISTORY ----------------
    List<ListeningHistory> findByUser_UserIdOrderByPlayedAtDesc(Long userId);

    List<ListeningHistory> findTop50ByUser_UserIdOrderByPlayedAtDesc(Long userId);

    long countByUser_UserId(Long userId);

    void deleteByUser_UserId(Long userId);

    @Query("SELECT COALESCE(SUM(l.song.duration), 0) FROM ListeningHistory l WHERE l.user.userId = :userId")
    Long sumDurationByUserId(@Param("userId") Long userId);

    @Query("""

            SELECT lh.song.songId, COUNT(lh) as playCount
           FROM ListeningHistory lh
           GROUP BY lh.song.songId
           ORDER BY playCount DESC
           """)
    List<Object[]> findTopPlayedSongs();

    // ---------------- ARTIST DASHBOARD ANALYTICS ----------------

    // Top listeners for an artist: returns [userId, playCount]
    @Query("""
           SELECT lh.user.userId, COUNT(lh) as playCount
           FROM ListeningHistory lh
           WHERE lh.song.artist.artistId = :artistId
           GROUP BY lh.user.userId
           ORDER BY playCount DESC
           """)
    List<Object[]> findTopListenersByArtist(@Param("artistId") Long artistId);

    // Play trends grouped by day: returns [date, playCount]
    @Query("""
           SELECT FUNCTION('DATE', lh.playedAt) as playDate, COUNT(lh) as playCount
           FROM ListeningHistory lh
           WHERE lh.song.artist.artistId = :artistId
           GROUP BY FUNCTION('DATE', lh.playedAt)
           ORDER BY playDate
           """)
    List<Object[]> findPlayTrendsByArtist(@Param("artistId") Long artistId);


        // Total plays for all songs of an artist
        @Query("""
           SELECT COUNT(lh)
           FROM ListeningHistory lh
           WHERE lh.song.artist.artistId = :artistId
           """)
        long countPlaysByArtist(@Param("artistId") Long artistId);

        // Top songs by play count
        @Query("""
           SELECT lh.song.songId, lh.song.title, COUNT(lh) AS playCount
           FROM ListeningHistory lh
           WHERE lh.song.artist.artistId = :artistId
           GROUP BY lh.song.songId, lh.song.title
           ORDER BY playCount DESC
           """)
        List<Object[]> findTopSongsByArtist(@Param("artistId") Long artistId);

    }
