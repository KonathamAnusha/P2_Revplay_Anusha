package com.rev.controller;

import com.rev.dto.PodcastDTO;
import com.rev.dto.PodcastEpisodeDTO;
import com.rev.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/podcasts")
public class PodcastController {

    @Autowired
    private PodcastService podcastService;

    @PostMapping
    public ResponseEntity<PodcastDTO> createPodcast(@RequestBody PodcastDTO dto) {
        return ResponseEntity.ok(podcastService.createPodcast(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PodcastDTO> getPodcast(@PathVariable Long id) {
        return ResponseEntity.ok(podcastService.getPodcastById(id));
    }

    @GetMapping
    public ResponseEntity<List<PodcastDTO>> getAllPodcasts() {
        return ResponseEntity.ok(podcastService.getAllPodcasts());
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<PodcastDTO>> getPodcastsByHost(@PathVariable Long hostId) {
        return ResponseEntity.ok(podcastService.getPodcastsByHost(hostId));
    }

    @PostMapping("/episodes")
    public ResponseEntity<PodcastEpisodeDTO> addEpisode(@RequestBody PodcastEpisodeDTO dto) {
        return ResponseEntity.ok(podcastService.addEpisode(dto));
    }
}
