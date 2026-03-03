package com.rev.mapper;

import com.rev.dto.PodcastDTO;
import com.rev.dto.PodcastEpisodeDTO;
import com.rev.entity.Podcast;
import com.rev.entity.PodcastEpisode;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PodcastMapper {

    public PodcastDTO toDTO(Podcast podcast) {
        if (podcast == null)
            return null;

        return PodcastDTO.builder()
                .podcastId(podcast.getPodcastId())
                .title(podcast.getTitle())
                .description(podcast.getDescription())
                .coverImage(podcast.getCoverImage())
                .hostId(podcast.getHost() != null ? podcast.getHost().getArtistId() : null)
                .hostName(podcast.getHost() != null ? podcast.getHost().getStageName() : null)
                .genreName(podcast.getGenre() != null ? podcast.getGenre().getName() : null)
                .createdAt(podcast.getCreatedAt())
                .episodes(podcast.getEpisodes() != null
                        ? podcast.getEpisodes().stream().map(this::toEpisodeDTO).collect(Collectors.toList())
                        : null)
                .build();
    }

    public PodcastEpisodeDTO toEpisodeDTO(PodcastEpisode episode) {
        if (episode == null)
            return null;

        return PodcastEpisodeDTO.builder()
                .episodeId(episode.getEpisodeId())
                .podcastId(episode.getPodcast() != null ? episode.getPodcast().getPodcastId() : null)
                .title(episode.getTitle())
                .description(episode.getDescription())
                .audioUrl(episode.getAudioUrl())
                .duration(episode.getDuration())
                .releaseDate(episode.getReleaseDate())
                .build();
    }

    public Podcast toEntity(PodcastDTO dto, com.rev.entity.ArtistProfile host, com.rev.entity.Genre genre) {
        return Podcast.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .coverImage(dto.getCoverImage())
                .host(host)
                .genre(genre)
                .build();
    }
}
