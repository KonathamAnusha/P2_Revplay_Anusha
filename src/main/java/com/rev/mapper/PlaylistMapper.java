package com.rev.mapper;

import com.rev.dto.PlaylistDTO;
import com.rev.entity.Playlist;
import com.rev.entity.PlaylistSong;
import com.rev.entity.Songs;
import com.rev.entity.UserAccount;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlaylistMapper {

    // Convert Entity → DTO
    public PlaylistDTO toDTO(Playlist playlist) {
        PlaylistDTO dto = new PlaylistDTO();
        dto.setPlaylistId(playlist.getPlaylistId());
        dto.setName(playlist.getName());
        dto.setDescription(playlist.getDescription());
        dto.setPrivacy(playlist.getPrivacy());
        dto.setUserId(playlist.getUser() != null ? playlist.getUser().getUserId() : null);

        if (playlist.getPlaylistSongs() != null) {
            dto.setSongIds(
                    playlist.getPlaylistSongs().stream()
                            .sorted((ps1, ps2) -> Integer.compare(ps1.getOrderIndex(), ps2.getOrderIndex()))
                            .map(ps -> ps.getSong().getSongId())
                            .collect(Collectors.toList()));
        }
        dto.setCreatedAt(playlist.getCreatedAt());
        return dto;
    }

    // Convert DTO → Entity (for create)
    public Playlist toEntity(PlaylistDTO dto, UserAccount user, List<Songs> songs) {
        Playlist playlist = new Playlist();
        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());
        playlist.setPrivacy(dto.getPrivacy());
        playlist.setUser(user);

        // Initialize playlistSongs
        if (songs != null) {
            List<PlaylistSong> psList = new java.util.ArrayList<>();
            for (int i = 0; i < songs.size(); i++) {
                PlaylistSong ps = new PlaylistSong();
                ps.setPlaylist(playlist);
                ps.setSong(songs.get(i));
                ps.setOrderIndex(i + 1);
                psList.add(ps);
            }
            playlist.setPlaylistSongs(psList);
        }

        return playlist;
    }

    // Update existing entity from DTO (for update)
    public void updateEntityFromDTO(PlaylistDTO dto, Playlist playlist, UserAccount user, List<Songs> songs) {
        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());
        playlist.setPrivacy(dto.getPrivacy());
        playlist.setUser(user);

        // Rebuild playlistSongs
        if (songs != null) {
            playlist.getPlaylistSongs().clear();
            for (int i = 0; i < songs.size(); i++) {
                PlaylistSong ps = new PlaylistSong();
                ps.setPlaylist(playlist);
                ps.setSong(songs.get(i));
                ps.setOrderIndex(i + 1);
                playlist.getPlaylistSongs().add(ps);
            }
        }
    }
}