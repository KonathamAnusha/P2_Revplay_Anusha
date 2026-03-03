package com.rev.controller;

import com.rev.dto.AlbumDTO;
import com.rev.dto.ArtistDTO;
import com.rev.dto.SongsDTO;
import com.rev.entity.UserAccount;
import com.rev.mapper.ArtistMapper;
import com.rev.service.AlbumServiceInterface;
import com.rev.service.ArtistServiceInterface;
import com.rev.service.SongsServiceInterface;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard/browse")
@RequiredArgsConstructor
public class BrowseController {

    private final SongsServiceInterface songsService;
    private final AlbumServiceInterface albumService;
    private final ArtistServiceInterface artistService;
    private final ArtistMapper artistMapper;

    @GetMapping
    public String browse(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String language,
            HttpSession session,
            Model model) {

        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/auth/login";

        List<SongsDTO> songs;
        List<AlbumDTO> albums = Collections.emptyList();
        List<ArtistDTO> artists = Collections.emptyList();

        if (search != null && !search.isBlank()) {
            // Search across songs, albums, and artists
            songs = songsService.searchSongsByTitle(search);
            albums = albumService.searchAlbumsByName(search);
            artists = artistService.getAllArtists().stream()
                    .filter(a -> a.getStageName() != null &&
                            a.getStageName().toLowerCase().contains(search.toLowerCase()))
                    .map(artistMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (genre != null && !genre.isBlank()) {
            songs = songsService.filterSongsByGenre(genre);
        } else if (language != null && !language.isBlank()) {
            songs = songsService.filterSongsByLanguage(language);
        } else {
            songs = songsService.getAllSongs();
        }

        model.addAttribute("user", user);
        model.addAttribute("songs", songs);
        model.addAttribute("albums", albums);
        model.addAttribute("artists", artists);
        model.addAttribute("role", user.getRole());
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedLanguage", language);

        return "auth/browse";
    }
}
