package com.rev.mapper;

import com.rev.dto.PlaylistSongDTO;
import com.rev.entity.PlaylistSong;
import org.springframework.stereotype.Component;

@Component
public class PlaylistSongMapper {

    public PlaylistSongDTO toDTO(PlaylistSong ps) {
        return PlaylistSongDTO.builder()
                .id(ps.getId())
                .playlistId(ps.getPlaylist().getPlaylistId())
                .songId(ps.getSong().getSongId())
                .orderIndex(ps.getOrderIndex())
                .build();
    }
}