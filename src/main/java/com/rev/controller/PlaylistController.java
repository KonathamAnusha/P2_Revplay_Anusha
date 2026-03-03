package com.rev.controller;

import com.rev.dto.PlaylistDTO;
import com.rev.dto.SongsDTO;
import com.rev.entity.UserAccount;
import com.rev.service.PlaylistServiceInterface;
import com.rev.service.SongsServiceInterface;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistServiceInterface playlistService;
    private final SongsServiceInterface songsService;

    @GetMapping
    public String listPlaylists(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/auth/login";

        List<PlaylistDTO> playlists = playlistService.getPlaylistsByUserId(user.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("playlists", playlists);
        model.addAttribute("role", user.getRole());
        return "auth/my-playlists";
    }

    @GetMapping("/{id}")
    public String viewPlaylist(@PathVariable Long id, HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/auth/login";

        PlaylistDTO playlist = playlistService.getPlaylistById(id);

        // Resolve song IDs to full SongsDTO objects
        java.util.List<SongsDTO> playlistSongs = new java.util.ArrayList<>();
        if (playlist.getSongIds() != null) {
            for (Long songId : playlist.getSongIds()) {
                try {
                    SongsDTO song = songsService.getSongById(songId);
                    playlistSongs.add(song);
                } catch (Exception e) {
                    // skip missing songs
                }
            }
        }

        // Get all songs for the "Add to Playlist" dropdown
        java.util.List<SongsDTO> allSongs = songsService.getAllSongs();

        model.addAttribute("user", user);
        model.addAttribute("playlist", playlist);
        model.addAttribute("playlistSongs", playlistSongs);
        model.addAttribute("allSongs", allSongs);
        model.addAttribute("role", user.getRole());
        return "auth/playlist-detail";
    }

    @PostMapping("/create")
    public String createPlaylist(@ModelAttribute PlaylistDTO playlistDTO, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user != null) {
            playlistDTO.setUserId(user.getUserId());
            playlistService.createPlaylist(playlistDTO);
        }
        return "redirect:/dashboard/playlists";
    }

    @PostMapping("/delete/{id}")
    public String deletePlaylist(@PathVariable Long id, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user != null) {
            playlistService.deletePlaylist(id);
        }
        return "redirect:/dashboard/playlists";
    }
}
