package com.rev.mapper;

import com.rev.dto.AlbumDTO;
import com.rev.entity.Album;
import com.rev.entity.ArtistProfile;
import org.springframework.stereotype.Component;

@Component
public class AlbumMapper {

    public Album toEntity(AlbumDTO dto, ArtistProfile artist) {
        return Album.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .coverArt(dto.getCoverArt())
                .releaseDate(dto.getReleaseDate()) // official date
                .artist(artist)
                .build();
    }

    public AlbumDTO toDTO(Album entity) {
        return AlbumDTO.builder()
                .albumId(entity.getAlbumId())
                .artistId(entity.getArtist().getArtistId())
                .name(entity.getName())
                .description(entity.getDescription())
                .coverArt(entity.getCoverArt())
                .releaseDate(entity.getReleaseDate()) // official date
                .build();
    }
}