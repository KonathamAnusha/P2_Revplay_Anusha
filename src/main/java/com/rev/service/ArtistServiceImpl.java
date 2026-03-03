package com.rev.service;

import com.rev.dto.ArtistDTO;
import com.rev.dto.TopListenerDTO;
import com.rev.entity.ArtistProfile;
import com.rev.entity.UserAccount;
import com.rev.mapper.ArtistMapper;
import com.rev.repository.ArtistRepository;
import com.rev.repository.FavoriteRepository;
import com.rev.repository.ListeningHistoryRepository;
import com.rev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(ArtistServiceImpl.class);

    private final ArtistRepository artistRepository;

    private final UserRepository userRepository;

    private final ArtistMapper artistMapper;

    private final FavoriteRepository favoriteRepository;

    private final ListeningHistoryRepository historyRepository;

    @Override
    public ArtistProfile addOrUpdateArtistProfile(Long userId, ArtistDTO dto) {
        log.info("Adding/updating artist profile for userId: {}", userId);

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ArtistProfile profile = user.getArtistProfile();

        if (profile == null) {
            profile = artistMapper.toEntity(dto, user);
            log.info("Created new artist profile for userId: {}", userId);
        } else {
            profile.setStageName(dto.getStageName());
            profile.setGenre(dto.getGenre());
            profile.setBio(dto.getBio());
            profile.setBannerImage(dto.getBannerImage());
            profile.setInstagram(dto.getInstagram());
            profile.setTwitter(dto.getTwitter());
            profile.setYoutube(dto.getYoutube());
            log.info("Updated existing artist profile for userId: {}", userId);
        }

        user.setArtistProfile(profile);
        return artistRepository.save(profile);
    }

    @Override
    public ArtistProfile getArtistProfileById(Long artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));
    }

    @Override
    public ArtistProfile getArtistProfileByUserId(Long userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getArtistProfile() == null) {
            throw new RuntimeException("Artist profile not found for this user");
        }

        return user.getArtistProfile();
    }

    @Override
    public List<ArtistProfile> getAllArtists() {
        return artistRepository.findAll();
    }

    @Override
    public void deleteArtist(Long artistId) {
        ArtistProfile profile = artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));

        artistRepository.delete(profile);
    }

    @Override
    public long getTotalFavorites(Long artistId) {
        return favoriteRepository.countFavoritesByArtist(artistId);
    }

    // ✅ FIXED: Pass Pageable argument
    @Override
    public List<TopListenerDTO> getTopListeners(Long artistId) {
        return historyRepository.findTopListenersForArtist(
                artistId,
                PageRequest.of(0, 10) // Top 10 listeners
        );
    }

    @Override
    public List<Object[]> getPlayTrends(Long artistId) {
        return historyRepository.findPlayTrendsByArtist(artistId);
    }
}