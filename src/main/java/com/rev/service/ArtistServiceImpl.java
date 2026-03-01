package com.rev.service;

import com.rev.dto.ArtistDTO;
import com.rev.entity.ArtistProfile;
import com.rev.entity.UserAccount;
import com.rev.mapper.ArtistMapper;
import com.rev.repository.ArtistRepository;
import com.rev.repository.FavoriteRepository;
import com.rev.repository.ListeningHistoryRepository;
import com.rev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistServiceInterface {

    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final ArtistMapper artistMapper;
    private final FavoriteRepository favoriteRepository;
    private final ListeningHistoryRepository historyRepository;


    // ------------------- ADD OR UPDATE ARTIST PROFILE -------------------
    @Override
    public ArtistProfile addOrUpdateArtistProfile(Long userId, ArtistDTO dto) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ArtistProfile profile = user.getArtistProfile();

        if (profile == null) {
            // Create new profile
            profile = artistMapper.toEntity(dto, user);
        } else {
            // Update existing profile
            profile.setStageName(dto.getStageName());
            profile.setGenre(dto.getGenre());
            profile.setBio(dto.getBio());
            profile.setBannerImage(dto.getBannerImage());
            profile.setInstagram(dto.getInstagram());
            profile.setTwitter(dto.getTwitter());
            profile.setYoutube(dto.getYoutube());

        }

        user.setArtistProfile(profile); // maintain bidirectional link
        return artistRepository.save(profile);
    }

    // ------------------- GET ARTIST PROFILE BY ARTIST ID -------------------
    @Override
    public ArtistProfile getArtistProfileById(Long artistId) {
        return artistRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artist profile not found"));
    }

    // ------------------- GET ARTIST PROFILE BY USER ID -------------------
    @Override
    public ArtistProfile getArtistProfileByUserId(Long userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getArtistProfile() == null) {
            throw new RuntimeException("Artist profile not found for this user");
        }
        return user.getArtistProfile();
    }

    // ------------------- GET ALL ARTISTS -------------------
    @Override
    public List<ArtistProfile> getAllArtists() {
        return artistRepository.findAll();
    }

    // ------------------- DELETE ARTIST PROFILE -------------------
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

    @Override
    public List<Object[]> getTopListeners(Long artistId) {
        // Returns [userId, playCount] for top listeners
        return historyRepository.findTopListenersByArtist(artistId);
    }

    @Override
    public List<Object[]> getPlayTrends(Long artistId) {
        // Returns [date, playCount] grouped by day
        return historyRepository.findPlayTrendsByArtist(artistId);
    }
}