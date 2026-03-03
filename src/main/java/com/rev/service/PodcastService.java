package com.rev.service;

import com.rev.dto.PodcastDTO;
import com.rev.dto.PodcastEpisodeDTO;
import java.util.List;

public interface PodcastService {
    PodcastDTO createPodcast(PodcastDTO dto);

    PodcastDTO getPodcastById(Long id);

    List<PodcastDTO> getAllPodcasts();

    List<PodcastDTO> getPodcastsByHost(Long hostId);

    PodcastEpisodeDTO addEpisode(PodcastEpisodeDTO episodeDTO);
}
