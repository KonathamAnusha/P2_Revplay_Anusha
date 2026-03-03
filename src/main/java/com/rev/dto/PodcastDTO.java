package com.rev.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PodcastDTO {
    private Long podcastId;
    private String title;
    private String description;
    private String coverImage;
    private Long hostId;
    private String hostName;
    private String genreName;
    private LocalDateTime createdAt;
    private List<PodcastEpisodeDTO> episodes;
}
