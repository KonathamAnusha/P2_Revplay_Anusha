package com.rev.service;

import com.rev.dto.PlaylistSongDTO;
import com.rev.entity.Playlist;
import com.rev.entity.PlaylistSong;
import com.rev.entity.Songs;
import com.rev.mapper.PlaylistSongMapper;
import com.rev.repository.PlaylistRepository;
import com.rev.repository.PlaylistSongRepository;
import com.rev.repository.SongsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaylistSongServiceImpl implements PlaylistSongServiceInterface {

    private final PlaylistSongRepository playlistSongRepository;
    private final PlaylistRepository playlistRepository;
    private final SongsRepository songsRepository;
    private final PlaylistSongMapper playlistSongMapper;

    @Override
    public PlaylistSongDTO addSongToPlaylist(Long playlistId, Long songId) {

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        Songs song = songsRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        if (playlistSongRepository.existsByPlaylist_PlaylistIdAndSong_SongId(playlistId, songId)) {
            throw new RuntimeException("Song already exists in playlist");
        }

        int maxOrder = playlistSongRepository.findMaxOrderIndexByPlaylistId(playlistId);

        PlaylistSong playlistSong = PlaylistSong.builder()
                .playlist(playlist)
                .song(song)
                .orderIndex(maxOrder + 1)
                .build();

        return playlistSongMapper.toDTO(playlistSongRepository.save(playlistSong));
    }

    @Override
    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        if (!playlistSongRepository.existsByPlaylist_PlaylistIdAndSong_SongId(playlistId, songId)) {
            throw new RuntimeException("Song not found in playlist");
        }
        playlistSongRepository.deleteByPlaylist_PlaylistIdAndSong_SongId(playlistId, songId);
    }

    @Override
    public List<PlaylistSongDTO> getSongsByPlaylist(Long playlistId) {
        return playlistSongRepository.findByPlaylist_PlaylistIdOrderByOrderIndex(playlistId)
                .stream()
                .map(playlistSongMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void reorderPlaylist(Long playlistId, List<Long> songIdsInOrder) {
        List<PlaylistSong> songs = playlistSongRepository.findByPlaylist_PlaylistIdOrderByOrderIndex(playlistId);

        if (songs.size() != songIdsInOrder.size()) {
            throw new RuntimeException("Mismatch in playlist size and new order list");
        }

        // Update orderIndex according to new list
        for (int i = 0; i < songIdsInOrder.size(); i++) {
            Long songId = songIdsInOrder.get(i);
            PlaylistSong ps = songs.stream()
                    .filter(s -> s.getSong().getSongId().equals(songId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Song ID not found in playlist: " + songId));
            ps.setOrderIndex(i + 1);
            playlistSongRepository.save(ps);
        }
    }
}