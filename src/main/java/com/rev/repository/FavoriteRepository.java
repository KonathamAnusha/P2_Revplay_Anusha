package com.rev.repository;

import com.rev.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // Get all favorites of a user
    List<Favorite> findByUser_UserId(Long userId);

    // Check if song already favorited
    boolean existsByUser_UserIdAndSong_SongId(Long userId, Long songId);

    // Remove favorite
    void deleteByUser_UserIdAndSong_SongId(Long userId, Long songId);

    // -------------------------
    // 🔥 CUSTOM METHODS BELOW
    // -------------------------



    // Get all favorites of a specific song
    List<Favorite> findBySong_SongId(Long songId);

    // Get list of user IDs who favorited a specific artist's songs
    @Query("""
           SELECT f.user.userId
           FROM Favorite f
           WHERE f.song.artist.artistId = :artistId
           """)
    List<Long> findUserIdsWhoFavoritedArtistSongs(Long artistId);

    // Count total favorites for all songs of an artist
    @Query("""
           SELECT COUNT(f)
           FROM Favorite f
           WHERE f.song.artist.artistId = :artistId
           """)
    long countFavoritesByArtist(Long artistId);
}