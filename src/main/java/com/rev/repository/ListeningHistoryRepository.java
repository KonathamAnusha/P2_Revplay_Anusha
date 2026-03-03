package com.rev.repository;

import com.rev.dto.TopListenerDTO;
import com.rev.dto.TopSongsDTO;
import com.rev.entity.ListeningHistory;
import org.springframework.data.domain.Pageable;
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

        void deleteBySong_SongId(Long songId);

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

        // Top listeners for an artist using DTO and pagination
        @Query("""
                        SELECT new com.rev.dto.TopListenerDTO(u.userId, u.fullName, COUNT(lh))
                        FROM ListeningHistory lh JOIN lh.user u
                        WHERE lh.song.artist.artistId = :artistId
                        GROUP BY u.userId, u.fullName
                        ORDER BY COUNT(lh) DESC
                        """)
        List<TopListenerDTO> findTopListenersForArtist(@Param("artistId") Long artistId, Pageable pageable);

        // Top songs for an artist using DTO and pagination
        @Query("""
                        SELECT new com.rev.dto.TopSongsDTO(s.songId, s.title, COUNT(lh))
                        FROM ListeningHistory lh JOIN lh.song s
                        WHERE s.artist.artistId = :artistId
                        GROUP BY s.songId, s.title
                        ORDER BY COUNT(lh) DESC
                        """)
        List<TopSongsDTO> findTopSongsForArtist(@Param("artistId") Long artistId, Pageable pageable);

        // Play trends grouped by day (native query)
        @Query(value = """
                        SELECT TRUNC(lh.played_at) AS play_date, COUNT(*) AS play_count
                        FROM listening_history lh
                        JOIN songs s ON s.song_id = lh.song_id
                        WHERE s.artist_id = :artistId
                        GROUP BY TRUNC(lh.played_at)
                        ORDER BY TRUNC(lh.played_at)
                        """, nativeQuery = true)
        List<Object[]> findPlayTrendsByArtist(@Param("artistId") Long artistId);

        // Total plays for all songs of an artist
        @Query("""
                            SELECT COUNT(lh)
                            FROM ListeningHistory lh
                            WHERE lh.song.artist.artistId = :artistId
                        """)
        Long countPlaysByArtist(@Param("artistId") Long artistId);

        // Fetch all listening history for songs of a specific artist
        @Query("SELECT lh FROM ListeningHistory lh WHERE lh.song.artist.artistId = :artistId")
        List<ListeningHistory> findByArtistId(@Param("artistId") Long artistId);

}