package com.rev.controller;

import com.rev.dto.ArtistAnalyticsDTO;
import com.rev.dto.ArtistDTO;
import com.rev.dto.SongsDTO;
import com.rev.dto.AlbumDTO;
import com.rev.entity.ArtistProfile;
import com.rev.entity.Songs;
import com.rev.entity.UserAccount;
import com.rev.entity.UserAccount.Role;
import com.rev.mapper.ArtistMapper;
import com.rev.repository.SongsRepository;
import com.rev.service.*;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    private final ArtistServiceInterface artistService;
    private final ArtistMapper artistMapper;
    private final SongsServiceInterface songsService;
    private final AlbumServiceInterface albumService;
    private final UserStatsServiceInterface statsService;
    private final ListeningHistoryServiceInterface historyService;
    private final FavoriteServiceInterface favoriteService;
    private final PlaylistServiceInterface playlistService;
    private final ArtistAnalyticsService analyticsService;
    private final SongsRepository songsRepository;
    private final com.rev.service.PodcastService podcastService;

    // ================= DASHBOARD (role-based) =================
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("role", user.getRole());

        if (user.getRole() == Role.ARTIST) {
            log.info("Loading artist dashboard for userId: {}", user.getUserId());
            try {
                ArtistProfile artist = artistService.getArtistProfileByUserId(user.getUserId());

                // Pagination parameters from request (handled by @RequestParam in method
                // signature below)
                // Defaulting to page 0, size 3
                int songPage = (session.getAttribute("songPage") != null) ? (int) session.getAttribute("songPage") : 0;
                int albumPage = (session.getAttribute("albumPage") != null) ? (int) session.getAttribute("albumPage")
                        : 0;

                org.springframework.data.domain.Pageable songPageable = org.springframework.data.domain.PageRequest
                        .of(songPage, 3);
                org.springframework.data.domain.Pageable albumPageable = org.springframework.data.domain.PageRequest
                        .of(albumPage, 3);

                org.springframework.data.domain.Page<SongsDTO> songsPage = songsService
                        .getSongsByArtistPaginated(artist.getArtistId(), songPageable);
                org.springframework.data.domain.Page<AlbumDTO> albumsPage = albumService
                        .getAlbumsByArtistPaginated(artist.getArtistId(), albumPageable);

                // Analytics
                ArtistAnalyticsDTO analytics = analyticsService.getArtistAnalytics(artist.getArtistId());

                model.addAttribute("artist", artist);
                model.addAttribute("mySongs", songsPage.getContent());
                model.addAttribute("songsPage", songsPage);
                model.addAttribute("myAlbums", albumsPage.getContent());
                model.addAttribute("albumsPage", albumsPage);
                model.addAttribute("myPodcasts", podcastService.getPodcastsByHost(artist.getArtistId()));
                model.addAttribute("analytics", analytics);
            } catch (Exception e) {
                log.warn("Artist profile not found for userId: {}, showing empty dashboard", user.getUserId());
                model.addAttribute("artist", null);
                model.addAttribute("mySongs", Collections.emptyList());
                model.addAttribute("myAlbums", Collections.emptyList());
                model.addAttribute("analytics", null);
            }
            return "auth/artist-dashboard";
        } else {
            log.info("Loading listener dashboard for userId: {}", user.getUserId());

            // Add Listener stats and history
            model.addAttribute("userStats", statsService.getUserStats(user.getUserId()));
            model.addAttribute("recentlyPlayed", historyService.getRecentUserHistory(user.getUserId()));
            model.addAttribute("favorites", favoriteService.getUserFavorites(user.getUserId()));
            model.addAttribute("playlists", playlistService.getPlaylistsByUserId(user.getUserId()));

            // Added for featured songs and albums on dashboard
            model.addAttribute("allSongs", songsService.getAllSongs());
            model.addAttribute("allAlbums", albumService.getAllAlbums());
            model.addAttribute("allPodcasts", podcastService.getAllPodcasts());

            return "auth/UserDashboard";
        }
    }

    // ================= ARTIST PROFILE EDIT PAGE =================
    @GetMapping("/dashboard/artist/profile")
    public String showEditArtistProfile(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }

        log.info("Loading artist profile edit page for userId: {}", user.getUserId());

        ArtistDTO artistDTO;
        try {
            ArtistProfile profile = artistService.getArtistProfileByUserId(user.getUserId());
            artistDTO = artistMapper.toDTO(profile);
        } catch (Exception e) {
            log.info("No existing artist profile for userId: {}, showing empty form", user.getUserId());
            artistDTO = new ArtistDTO();
        }

        model.addAttribute("user", user);
        model.addAttribute("artistDTO", artistDTO);
        return "auth/edit-artist-profile";
    }

    // ================= SAVE ARTIST PROFILE =================
    @PostMapping("/dashboard/artist/profile")
    public String saveArtistProfile(@ModelAttribute("artistDTO") ArtistDTO artistDTO,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }

        log.info("Saving artist profile for userId: {}", user.getUserId());
        artistService.addOrUpdateArtistProfile(user.getUserId(), artistDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/dashboard/artist/profile";
    }

    // ================= PUBLIC ARTIST PROFILE VIEW =================
    @GetMapping("/artist/{artistId}")
    public String viewArtistProfile(@PathVariable Long artistId, Model model, HttpSession session) {
        log.info("Viewing public artist profile for artistId: {}", artistId);

        ArtistProfile artist = artistService.getArtistProfileById(artistId);
        ArtistDTO artistDTO = artistMapper.toDTO(artist);

        List<SongsDTO> artistSongs = songsService.getSongsByArtist(artistId);
        List<AlbumDTO> artistAlbums = albumService.getAlbumsByArtist(artistId);

        model.addAttribute("artist", artistDTO);
        model.addAttribute("artistSongs", artistSongs);
        model.addAttribute("artistAlbums", artistAlbums);

        // Check if logged-in user is viewing their own profile
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        model.addAttribute("isOwnProfile", user != null && artist.getUserAccount() != null
                && user.getUserId().equals(artist.getUserAccount().getUserId()));

        return "auth/artist-profile";
    }

    // ================= SONG CRUD FROM DASHBOARD =================
    @GetMapping("/dashboard/song/add")
    public String showAddSongForm(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }
        ArtistProfile artist = artistService.getArtistProfileByUserId(user.getUserId());
        if (artist == null) {
            return "redirect:/dashboard/artist/profile";
        }
        // Need to pass albums to the add song page for the select dropdown
        List<AlbumDTO> myAlbums = albumService.getAlbumsByArtist(artist.getArtistId());
        model.addAttribute("user", user);
        model.addAttribute("myAlbums", myAlbums);
        return "auth/add-song";
    }

    @PostMapping("/dashboard/song/add")
    public String addSong(@ModelAttribute SongsDTO songDTO,
            @RequestParam(value = "audioFile", required = false) org.springframework.web.multipart.MultipartFile audioFile,
            HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }
        ArtistProfile artist = artistService.getArtistProfileByUserId(user.getUserId());
        if (artist == null) {
            return "redirect:/dashboard/artist/profile";
        }
        songDTO.setArtistId(artist.getArtistId());

        // Handle File Upload
        if (audioFile != null && !audioFile.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + audioFile.getOriginalFilename();
                java.nio.file.Path path = java.nio.file.Paths.get("C:/revplay/music/" + fileName);
                java.nio.file.Files.createDirectories(path.getParent());
                java.nio.file.Files.write(path, audioFile.getBytes());
                songDTO.setSongUrl("/music/" + fileName);
                log.info("File uploaded successfully: {}", fileName);

                // Extract MP3 metadata and Cover Image
                try {
                    com.mpatric.mp3agic.Mp3File mp3file = new com.mpatric.mp3agic.Mp3File(path.toString());
                    if (mp3file.hasId3v2Tag()) {
                        byte[] imageData = mp3file.getId3v2Tag().getAlbumImage();
                        if (imageData != null) {
                            String mimeType = mp3file.getId3v2Tag().getAlbumImageMimeType();
                            String ext = ".jpg";
                            if (mimeType != null && mimeType.contains("png"))
                                ext = ".png";
                            String imgName = "cov_" + System.currentTimeMillis() + ext;
                            java.nio.file.Path imgPath = java.nio.file.Paths.get("C:/revplay/images/" + imgName);
                            java.nio.file.Files.createDirectories(imgPath.getParent());
                            java.nio.file.Files.write(imgPath, imageData);
                            songDTO.setCoverArt("/images/" + imgName); // stored in DTO so SongsMapper can map to
                                                                       // coverImage
                            log.info("Extracted cover art to {}", imgName);
                        }
                    }
                } catch (Exception e) {
                    log.warn("Failed to extract ID3 tags", e);
                }

            } catch (java.io.IOException e) {
                log.error("Failed to upload file", e);
            }
        }

        songsService.createSong(songDTO);
        log.info("Song added by artist userId: {}", user.getUserId());
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard/song/delete/{songId}")
    public String deleteSong(@PathVariable Long songId, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }
        songsService.deleteSong(songId);
        log.info("Song {} deleted by userId: {}", songId, user.getUserId());
        return "redirect:/dashboard";
    }

    // ================= SONG VISIBILITY TOGGLE =================
    @GetMapping("/dashboard/song/toggle-visibility/{songId}")
    public String toggleSongVisibility(@PathVariable Long songId, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }
        Songs song = songsRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));
        song.setIsPublic(!song.getIsPublic());
        songsRepository.save(song);
        log.info("Song {} visibility toggled to {} by userId: {}", songId, song.getIsPublic(), user.getUserId());
        return "redirect:/dashboard";
    }

    // ================= ALBUM CRUD FROM DASHBOARD =================
    @PostMapping("/dashboard/album/add")
    public String addAlbum(@ModelAttribute AlbumDTO albumDTO, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }
        ArtistProfile artist = artistService.getArtistProfileByUserId(user.getUserId());
        albumService.createAlbum(artist.getArtistId(), albumDTO);
        log.info("Album created by artist userId: {}", user.getUserId());
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard/album/delete/{albumId}")
    public String deleteAlbum(@PathVariable Long albumId, HttpSession session, RedirectAttributes redirectAttributes) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }
        try {
            albumService.deleteAlbum(albumId);
            log.info("Album {} deleted by userId: {}", albumId, user.getUserId());
        } catch (Exception e) {
            log.error("Failed to delete album: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete album that contains songs.");
        }
        return "redirect:/dashboard";
    }

    // ================= ALBUM CONTENT MANAGEMENT =================
    @GetMapping("/dashboard/artist/album/{albumId}")
    public String viewAlbumDetail(@PathVariable Long albumId, HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }

        AlbumDTO album = albumService.getAlbumById(albumId);
        List<SongsDTO> albumSongs = albumService.getSongsByAlbum(albumId);

        // Artist's other songs so they can add them to the album
        ArtistProfile artist = artistService.getArtistProfileByUserId(user.getUserId());
        if (artist == null) {
            return "redirect:/dashboard/artist/profile";
        }
        List<SongsDTO> artistSongs = songsService.getSongsByArtist(artist.getArtistId());

        // Filter out songs already in the album
        java.util.List<SongsDTO> availableSongs = artistSongs.stream()
                .filter(s -> albumSongs.stream().noneMatch(as -> as.getSongId().equals(s.getSongId())))
                .collect(java.util.stream.Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("album", album);
        model.addAttribute("albumSongs", albumSongs);
        model.addAttribute("availableSongs", availableSongs);

        return "auth/album-detail";
    }

    @PostMapping("/dashboard/album/{albumId}/add-song")
    public String addSongToAlbum(@PathVariable Long albumId, @RequestParam Long songId, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }
        albumService.addSongToAlbum(albumId, songId);
        log.info("Song {} added to album {} by userId: {}", songId, albumId, user.getUserId());
        return "redirect:/dashboard/album/" + albumId;
    }

    @PostMapping("/dashboard/album/{albumId}/remove-song")
    public String removeSongFromAlbum(@PathVariable Long albumId, @RequestParam Long songId, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }
        albumService.removeSongFromAlbum(albumId, songId);
        log.info("Song {} removed from album {} by userId: {}", songId, albumId, user.getUserId());
        return "redirect:/dashboard/album/" + albumId;
    }

    // ================= MY SONGS PAGE =================
    @GetMapping("/dashboard/my-songs")
    public String showMySongs(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }
        ArtistProfile artist = artistService.getArtistProfileByUserId(user.getUserId());
        if (artist == null) {
            return "redirect:/dashboard/artist/profile";
        }
        List<SongsDTO> mySongs = songsService.getSongsByArtist(artist.getArtistId());
        model.addAttribute("user", user);
        model.addAttribute("role", user.getRole());
        model.addAttribute("mySongs", mySongs);
        return "auth/my-songs";
    }

    // ================= MY ALBUMS PAGE =================
    @GetMapping("/dashboard/my-albums")
    public String showMyAlbums(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST) {
            return "redirect:/auth/login";
        }
        ArtistProfile artist = artistService.getArtistProfileByUserId(user.getUserId());
        List<AlbumDTO> myAlbums = albumService.getAlbumsByArtist(artist.getArtistId());
        model.addAttribute("user", user);
        model.addAttribute("role", user.getRole());
        model.addAttribute("myAlbums", myAlbums);
        return "auth/my-albums";
    }

    // ================= LISTENER DASHBOARD =================
    @GetMapping("/listener-dashboard")
    public String showListenerDashboard(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("role", user.getRole());

        // Add Listener stats and history
        model.addAttribute("userStats", statsService.getUserStats(user.getUserId()));
        model.addAttribute("recentlyPlayed", historyService.getRecentUserHistory(user.getUserId()));
        model.addAttribute("favorites", favoriteService.getUserFavorites(user.getUserId()));
        model.addAttribute("playlists", playlistService.getPlaylistsByUserId(user.getUserId()));

        // Add featured songs, albums, and podcasts
        model.addAttribute("allSongs", songsService.getAllSongs());
        model.addAttribute("allAlbums", albumService.getAllAlbums());
        model.addAttribute("allPodcasts", podcastService.getAllPodcasts());

        return "auth/UserDashboard";
    }

    @GetMapping("/dashboard/podcasts")
    public String managePodcasts(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST)
            return "redirect:/auth/login";
        ArtistProfile artist = artistService.getArtistProfileByUserId(user.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("role", user.getRole());
        model.addAttribute("myPodcasts", podcastService.getPodcastsByHost(artist.getArtistId()));
        return "auth/manage-podcasts";
    }

    @PostMapping("/dashboard/podcasts/add")
    public String addPodcast(@RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String coverImage,
            @RequestParam(required = false) String genreName,
            HttpSession session, RedirectAttributes redirectAttributes) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST)
            return "redirect:/auth/login";
        try {
            ArtistProfile artist = artistService.getArtistProfileByUserId(user.getUserId());
            com.rev.dto.PodcastDTO dto = com.rev.dto.PodcastDTO.builder()
                    .title(title)
                    .description(description)
                    .coverImage(coverImage)
                    .hostId(artist.getArtistId())
                    .genreName(genreName != null && !genreName.isBlank() ? genreName : "General")
                    .build();
            podcastService.createPodcast(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Podcast created successfully!");
        } catch (Exception e) {
            log.error("Error creating podcast", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/dashboard/podcasts";
    }

    @PostMapping("/dashboard/podcasts/{podcastId}/episodes/add")
    public String addEpisode(@PathVariable Long podcastId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(value = "audioFile", required = true) org.springframework.web.multipart.MultipartFile audioFile,
            @RequestParam(defaultValue = "0") int duration,
            HttpSession session, RedirectAttributes redirectAttributes) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST)
            return "redirect:/auth/login";
        try {
            String fileUrl = "";
            if (audioFile != null && !audioFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_ep_" + audioFile.getOriginalFilename();
                java.nio.file.Path path = java.nio.file.Paths.get("C:/revplay/music/" + fileName);
                java.nio.file.Files.createDirectories(path.getParent());
                java.nio.file.Files.write(path, audioFile.getBytes());
                fileUrl = "/music/" + fileName;
                log.info("Episode file uploaded successfully: {}", fileName);
            }

            com.rev.dto.PodcastEpisodeDTO dto = com.rev.dto.PodcastEpisodeDTO.builder()
                    .podcastId(podcastId)
                    .title(title)
                    .description(description)
                    .audioUrl(fileUrl)
                    .duration(duration)
                    .build();
            podcastService.addEpisode(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Episode added successfully!");
        } catch (Exception e) {
            log.error("Error adding episode", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/dashboard/podcasts";
    }

    @GetMapping("/dashboard/albums")
    public String manageAlbums(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null || user.getRole() != Role.ARTIST)
            return "redirect:/auth/login";
        ArtistProfile artist = artistService.getArtistProfileByUserId(user.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("role", user.getRole());
        model.addAttribute("myAlbums", albumService.getAlbumsByArtist(artist.getArtistId()));
        return "auth/manage-albums";
    }
}
