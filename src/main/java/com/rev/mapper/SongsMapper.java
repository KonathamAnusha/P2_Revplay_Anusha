package com.rev.mapper;

import com.rev.dto.SongsDTO;
import com.rev.entity.Album;
import com.rev.entity.ArtistProfile;
import com.rev.entity.Songs;
import org.springframework.stereotype.Component;

@Component
public class SongsMapper {

    public SongsDTO toDTO(Songs songs) {
        if (songs == null)
            return null;

        return SongsDTO.builder()
                .songId(songs.getSongId())
                .title(songs.getTitle())
                .genre(songs.getGenre() != null ? songs.getGenre().getName() : null)
                .duration(songs.getDuration())
                .songUrl(songs.getSongUrl())
                .language(songs.getLanguage())
                .isPublic(songs.getIsPublic())
                .artistId(songs.getArtist() != null ? songs.getArtist().getArtistId() : null)
                .artistName(songs.getArtist() != null ? songs.getArtist().getStageName() : null)
                .albumId(songs.getAlbum() != null ? songs.getAlbum().getAlbumId() : null)
                .albumName(songs.getAlbum() != null ? songs.getAlbum().getName() : null)
                .coverArt(songs.getCoverImage() != null ? songs.getCoverImage()
                        : (songs.getAlbum() != null ? songs.getAlbum().getCoverArt() : null))
                .createdAt(songs.getCreatedAt())
                .playCount(songs.getPlayCount())
                .build();
    }

    public Songs toEntity(SongsDTO dto, ArtistProfile artist, Album album, com.rev.entity.Genre genre) {
        Songs song = Songs.builder()
                .title(dto.getTitle())
                .genre(genre)
                .genreName(genre != null ? genre.getName() : dto.getGenre())
                .duration(dto.getDuration())
                .songUrl(dto.getSongUrl())
                .language(dto.getLanguage())
                .artist(artist)
                .album(album)
                .coverImage(dto.getCoverArt())
                .isPublic(dto.getIsPublic() != null ? dto.getIsPublic() : true)
                .build();

        // Now song exists, safe to use
        if (song.getPlayCount() == null) {
            song.setPlayCount(0L);
        }

        return song;
    }
}