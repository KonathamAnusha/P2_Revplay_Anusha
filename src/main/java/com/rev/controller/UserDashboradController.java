package com.rev.controller;

import com.rev.dto.FavoriteDTO;
import com.rev.dto.ListeningHistoryDTO;
import com.rev.dto.PlaylistDTO;
import com.rev.entity.UserAccount;
import com.rev.service.FavoriteServiceInterface;
import com.rev.service.ListeningHistoryServiceInterface;
import com.rev.service.PlaylistServiceInterface;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserDashboradController {

    private final ListeningHistoryServiceInterface listeningHistoryService;
    private final FavoriteServiceInterface favoritesService;
    private final PlaylistServiceInterface playlistService;

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {

        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";  // Not logged in
        }

        model.addAttribute("user", user);

        // ✅ Fetch dashboard data as DTOs
        List<ListeningHistoryDTO> recentlyPlayed = listeningHistoryService.getRecentUserHistory(user.getUserId());
        List<FavoriteDTO> favorites = favoritesService.getUserFavorites(user.getUserId());
        List<PlaylistDTO> playlists = playlistService.getPlaylistsByUserId(user.getUserId());

        // ✅ Add to model
        model.addAttribute("recentlyPlayed", recentlyPlayed);
        model.addAttribute("favorites", favorites);
        model.addAttribute("playlists", playlists);

        return "UserDashboard";  // Thymeleaf template
    }
}