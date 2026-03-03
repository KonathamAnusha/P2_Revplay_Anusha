package com.rev.service;

import com.rev.dto.PodcastDTO;
import com.rev.dto.PodcastEpisodeDTO;
import com.rev.entity.ArtistProfile;
import com.rev.entity.Genre;
import com.rev.entity.Podcast;
import com.rev.entity.PodcastEpisode;
import com.rev.mapper.PodcastMapper;
import com.rev.repository.ArtistRepository;
import com.rev.repository.GenreRepository;
import com.rev.repository.PodcastEpisodeRepository;
import com.rev.repository.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PodcastServiceImpl implements PodcastService {

    @Autowired
    private PodcastRepository podcastRepository;

    @Autowired
    private PodcastEpisodeRepository episodeRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PodcastMapper podcastMapper;

    @Override
    public PodcastDTO createPodcast(PodcastDTO dto) {
        ArtistProfile host = artistRepository.findById(dto.getHostId())
                .orElseThrow(() -> new RuntimeException("Host not found"));

        Genre genre = genreRepository.findByNameIgnoreCase(dto.getGenreName())
                .orElseGet(() -> {
                    Genre newGenre = Genre.builder().name(dto.getGenreName()).build();
                    return genreRepository.save(newGenre);
                });

        Podcast podcast = podcastMapper.toEntity(dto, host, genre);
        return podcastMapper.toDTO(podcastRepository.save(podcast));
    }

    @Override
    public PodcastDTO getPodcastById(Long id) {
        return podcastRepository.findById(id)
                .map(podcastMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Podcast not found"));
    }

    @Override
    public List<PodcastDTO> getAllPodcasts() {
        return podcastRepository.findAll().stream()
                .map(podcastMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PodcastDTO> getPodcastsByHost(Long hostId) {
        return podcastRepository.findByHost_ArtistId(hostId).stream()
                .map(podcastMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PodcastEpisodeDTO addEpisode(PodcastEpisodeDTO dto) {
        Podcast podcast = podcastRepository.findById(dto.getPodcastId())
                .orElseThrow(() -> new RuntimeException("Podcast not found"));

        PodcastEpisode episode = PodcastEpisode.builder()
                .podcast(podcast)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .audioUrl(dto.getAudioUrl())
                .duration(dto.getDuration())
                .build();

        return podcastMapper.toEpisodeDTO(episodeRepository.save(episode));
    }
}
