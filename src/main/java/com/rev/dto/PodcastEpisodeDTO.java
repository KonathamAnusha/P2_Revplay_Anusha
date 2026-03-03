package com.rev.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PodcastEpisodeDTO {
    private Long episodeId;
    private Long podcastId;
    private String title;
    private String description;
    private String audioUrl;
    private int duration;
    private LocalDateTime releaseDate;
}
