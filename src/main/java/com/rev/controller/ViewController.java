package com.rev.controller;

import com.rev.dto.AlbumDTO;
import com.rev.dto.ArtistDTO;
import com.rev.dto.SongsDTO;
import com.rev.entity.ArtistProfile;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class ViewController {

    private final SongsServiceInterface songsService;
    private final AlbumServiceInterface albumService;
    private final ArtistServiceInterface artistService;
    private final ArtistMapper artistMapper;

    // ==================== SONG DETAILS ====================
    @GetMapping("/song/{id}")
    public String songDetails(@PathVariable Long id,
            HttpSession session,
            Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/auth/login";

        SongsDTO song = songsService.getSongById(id);
        model.addAttribute("song", song);
        model.addAttribute("user", user);
        return "auth/song-details";
    }

    // ==================== ARTIST VIEW (for listeners) ====================
    @GetMapping("/artist/{id}")
    public String artistView(@PathVariable Long id,
            HttpSession session,
            Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/auth/login";

        ArtistProfile profile = artistService.getArtistProfileById(id);
        ArtistDTO artist = artistMapper.toDTO(profile);
        List<SongsDTO> artistSongs = songsService.getSongsByArtist(id);
        List<AlbumDTO> artistAlbums = albumService.getAlbumsByArtist(id);

        model.addAttribute("artist", artist);
        model.addAttribute("songs", artistSongs);
        model.addAttribute("albums", artistAlbums);
        model.addAttribute("user", user);
        return "auth/artist-view";
    }

    // ==================== ALBUM VIEW ====================
    @GetMapping("/album/{id}")
    public String albumView(@PathVariable Long id,
            HttpSession session,
            Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/auth/login";

        AlbumDTO album = albumService.getAlbumById(id);
        List<SongsDTO> tracks = albumService.getAlbumSongs(id);

        // Resolve the artist name for display
        if (album.getArtistId() != null) {
            try {
                ArtistProfile ap = artistService.getArtistProfileById(album.getArtistId());
                model.addAttribute("artistName", ap.getStageName());
                model.addAttribute("artistId", ap.getArtistId());
            } catch (Exception e) {
                model.addAttribute("artistName", "Unknown Artist");
            }
        }

        model.addAttribute("album", album);
        model.addAttribute("tracks", tracks);
        model.addAttribute("user", user);
        return "auth/album-view";
    }
}
