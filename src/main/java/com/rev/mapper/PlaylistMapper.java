package com.rev.mapper;

import com.rev.dto.PlaylistDTO;
import com.rev.entity.Playlist;
import com.rev.entity.Songs;
import com.rev.entity.UserAccount;
import org.springframework.stereotype.Component;

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
        if (playlist.getSongs() != null) {
            dto.setSongIds(
                    playlist.getSongs().stream()
                            .map(Songs::getSongId)
                            .collect(Collectors.toList())
            );
        }
        dto.setCreatedAt(playlist.getCreatedAt());
        return dto;
    }

    // Convert DTO → Entity
    public Playlist toEntity(PlaylistDTO dto, UserAccount user, java.util.List<Songs> songs) {
        Playlist playlist = new Playlist();
        playlist.setPlaylistId(dto.getPlaylistId());
        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());
        playlist.setPrivacy(dto.getPrivacy());
        playlist.setUser(user);
        playlist.setSongs(songs);
        return playlist;
    }
}