package com.rev.repository;

import com.rev.entity.ArtistProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<ArtistProfile, Long> {

    // ================= BASIC FINDERS =================
    Optional<ArtistProfile> findByUserAccountUserId(Long userId);

    Optional<ArtistProfile> findByStageName(String stageName);

    boolean existsByStageName(String stageName);

    // ================= FILTER BY GENRE =================
    List<ArtistProfile> findByGenre(String genre);



    // ================= DELETE =================
    void deleteByArtistId(Long id);
}