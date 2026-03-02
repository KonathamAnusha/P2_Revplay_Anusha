package com.rev.controller;

import com.rev.dto.FavoriteDTO;
import com.rev.dto.ListeningHistoryDTO;
import com.rev.dto.PlaylistDTO;
import com.rev.entity.UserAccount;
import com.rev.entity.UserAccount.Role;
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

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {

        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null) return "redirect:/auth/login";

        model.addAttribute("user", user);
        model.addAttribute("role", user.getRole());

        // Only fetch Listener-specific data
        if (user.getRole() == Role.LISTENER) {
            List<ListeningHistoryDTO> recentlyPlayed = listeningHistoryService.getRecentUserHistory(user.getUserId());
            List<FavoriteDTO> favorites = favoritesService.getUserFavorites(user.getUserId());
            List<PlaylistDTO> playlists = playlistService.getPlaylistsByUserId(user.getUserId());

            model.addAttribute("recentlyPlayed", recentlyPlayed);
            model.addAttribute("favorites", favorites);
            model.addAttribute("playlists", playlists);

//            // Example: calculate total listening time
//            int listeningTime = recentlyPlayed.stream().mapToInt(ListeningHistoryDTO::getDuration()).sum();
//            model.addAttribute("listeningTime", listeningTime);
//        }

            int listeningTime = recentlyPlayed.stream()
                    .mapToInt(song -> song.getDuration()) // <-- change getDurationSeconds() to your field's getter
                    .sum();
            model.addAttribute("listeningTime", listeningTime);
        }

        // Artist-specific info comes from user.getArtistProfile()
        // You can add analytics or uploaded songs if needed

        return "UserDashboard"; // single Thymeleaf template
    }
}