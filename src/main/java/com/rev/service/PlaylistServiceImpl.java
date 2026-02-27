package com.rev.service;

import com.rev.dto.PlaylistDTO;
import com.rev.entity.Playlist;
import com.rev.entity.Songs;
import com.rev.entity.UserAccount;
import com.rev.mapper.PlaylistMapper;
import com.rev.repository.PlaylistRepository;
import com.rev.repository.SongsRepository;
import com.rev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistServiceInterface {

    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
    private final UserRepository userRepository;
    private final SongsRepository songsRepository;

    @Override
    public PlaylistDTO createPlaylist(PlaylistDTO dto) {
        UserAccount user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Songs> songs = songsRepository.findAllById(dto.getSongIds() != null ? dto.getSongIds() : List.of());

        Playlist playlist = playlistMapper.toEntity(dto, user, songs); // <-- pass actual entities
        Playlist saved = playlistRepository.save(playlist);
        return playlistMapper.toDTO(saved);
    }
    @Override
    public PlaylistDTO updatePlaylist(Long id, PlaylistDTO dto) {
        Playlist existing = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + id));

        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getPrivacy() != null) existing.setPrivacy(dto.getPrivacy());
        if (dto.getSongIds() != null) {
            List<Songs> songs = songsRepository.findAllById(dto.getSongIds());
            existing.setSongs(songs);
        }

        Playlist updated = playlistRepository.save(existing);
        return playlistMapper.toDTO(updated);
    }

    @Override
    public void deletePlaylist(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + id));
        playlistRepository.delete(playlist);
    }

    @Override
    public PlaylistDTO getPlaylistById(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found with id: " + id));
        return playlistMapper.toDTO(playlist);
    }

    @Override
    public List<PlaylistDTO> getAllPlaylists() {
        return playlistRepository.findAll()
                .stream()
                .map(playlistMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaylistDTO> getPlaylistsByUserId(Long userId) {
        return playlistRepository.findByUser_UserId(userId)
                .stream()
                .map(playlistMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaylistDTO> getPlaylistsByPrivacy(String privacy) {
        return playlistRepository.findByPrivacy(privacy)
                .stream()
                .map(playlistMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaylistDTO> searchPlaylistsByName(String name) {
        return playlistRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(playlistMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaylistDTO> getUserPlaylistsByPrivacy(Long userId, String privacy) {
        return playlistRepository.findByUser_UserIdAndPrivacy(userId, privacy)
                .stream()
                .map(playlistMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaylistDTO> getPlaylistsBySongId(Long songId) {
        return playlistRepository.findBySongs_SongId(songId)
                .stream()
                .map(playlistMapper::toDTO)
                .collect(Collectors.toList());
    }
}