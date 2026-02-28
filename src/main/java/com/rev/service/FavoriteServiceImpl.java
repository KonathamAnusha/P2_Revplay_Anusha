package com.rev.service;

import com.rev.dto.FavoriteDTO;
import com.rev.entity.Favorite;
import com.rev.entity.Songs;
import com.rev.entity.UserAccount;
import com.rev.mapper.FavoriteMapper;
import com.rev.repository.FavoriteRepository;
import com.rev.repository.SongsRepository;
import com.rev.repository.UserRepository;
import com.rev.service.FavoriteServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImpl implements FavoriteServiceInterface {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final SongsRepository songsRepository;
    private final FavoriteMapper favoriteMapper;

    @Override
    public FavoriteDTO addFavorite(Long userId, Long songId) {

        if (favoriteRepository.existsByUser_UserIdAndSong_SongId(userId, songId)) {
            throw new RuntimeException("Song already added to favorites");
        }

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Songs song = songsRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        Favorite favorite = Favorite.builder()
                .user(user)
                .song(song)
                .build();

        return favoriteMapper.toDTO(favoriteRepository.save(favorite));
    }

    @Override
    public void removeFavorite(Long userId, Long songId) {
        if (!favoriteRepository.existsByUser_UserIdAndSong_SongId(userId, songId)) {
            throw new RuntimeException("Favorite not found");
        }
        favoriteRepository.deleteByUser_UserIdAndSong_SongId(userId, songId);
    }

    @Override
    public List<FavoriteDTO> getUserFavorites(Long userId) {
        return favoriteRepository.findByUser_UserId(userId)
                .stream()
                .map(favoriteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FavoriteDTO> getFavoritesBySong(Long songId) {
        return favoriteRepository.findBySong_SongId(songId)
                .stream()
                .map(favoriteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getUsersWhoFavoritedArtist(Long artistId) {
        return favoriteRepository.findUserIdsWhoFavoritedArtistSongs(artistId);
    }

    @Override
    public long getArtistTotalFavorites(Long artistId) {
        return favoriteRepository.countFavoritesByArtist(artistId);
    }
}