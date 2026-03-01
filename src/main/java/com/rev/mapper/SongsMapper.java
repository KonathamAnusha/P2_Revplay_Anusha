package com.rev.mapper;

import com.rev.dto.SongsDTO;
import com.rev.entity.Album;
import com.rev.entity.ArtistProfile;
import com.rev.entity.Songs;
import org.springframework.stereotype.Component;

@Component
public class SongsMapper {

    public SongsDTO toDTO(Songs songs) {
        if (songs == null) return null;

        return SongsDTO.builder()
                .songId(songs.getSongId())
                .title(songs.getTitle())
                .genre(songs.getGenre())
                .duration(songs.getDuration())
                .songUrl(songs.getSongUrl())
                .language(songs.getLanguage())
                .isPublic(songs.getIsPublic())   //
                .artistId(songs.getArtist() != null ? songs.getArtist().getArtistId() : null)
                .albumId(songs.getAlbum() != null ? songs.getAlbum().getAlbumId() : null)
                .build();
    }

    public Songs toEntity(SongsDTO dto, ArtistProfile artist, Album album) {
        if (dto == null) return null;

        return Songs.builder()
                .title(dto.getTitle())
                .genre(dto.getGenre())
                .duration(dto.getDuration())
                .songUrl(dto.getSongUrl())
                .language(dto.getLanguage())
                .artist(artist)
                .album(album)
                .isPublic(dto.getIsPublic() != null ? dto.getIsPublic() : true) // ✅ FIXED
                .build();
    }
}