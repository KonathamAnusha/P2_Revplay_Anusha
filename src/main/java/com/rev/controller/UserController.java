package com.rev.controller;

import com.rev.dto.FavoriteDTO;
import com.rev.dto.ListeningHistoryDTO;
import com.rev.dto.SongsDTO;
import com.rev.dto.UserStatsDTO;
import com.rev.entity.UserAccount;
import com.rev.service.FavoriteServiceInterface;
import com.rev.service.ListeningHistoryServiceInterface;
import com.rev.service.SongsServiceInterface;
import com.rev.service.UserServiceInterface;
import com.rev.service.UserStatsServiceInterface;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class UserController {

    private final FavoriteServiceInterface favoriteService;
    private final ListeningHistoryServiceInterface historyService;
    private final UserStatsServiceInterface statsService;
    private final UserServiceInterface userService;
    private final SongsServiceInterface songsService;

    // ================= FAVORITES =================
    @GetMapping("/favorites")
    public String showFavorites(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/auth/login";

        List<FavoriteDTO> favorites = favoriteService.getUserFavorites(user.getUserId());
        java.util.List<SongsDTO> favoriteSongs = new java.util.ArrayList<>();

        for (FavoriteDTO fav : favorites) {
            try {
                SongsDTO song = songsService.getSongById(fav.getSongId());
                favoriteSongs.add(song);
            } catch (Exception e) {
                // ignore
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("favoriteSongs", favoriteSongs);
        model.addAttribute("role", user.getRole());
        return "auth/favorites";
    }

    @PostMapping("/favorites/add/{songId}")
    @ResponseBody
    public String addFavorite(@PathVariable Long songId, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null)
            return "Unauthorized";
        try {
            List<FavoriteDTO> existing = favoriteService.getUserFavorites(user.getUserId());
            boolean isFav = existing.stream().anyMatch(f -> f.getSongId().equals(songId));

            if (isFav) {
                favoriteService.removeFavorite(user.getUserId(), songId);
                return "Removed from favorites";
            } else {
                favoriteService.addFavorite(user.getUserId(), songId);
                return "Success";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @PostMapping("/favorites/remove/{songId}")
    @ResponseBody
    public String removeFavorite(@PathVariable Long songId, HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null)
            return "Unauthorized";
        favoriteService.removeFavorite(user.getUserId(), songId);
        return "Removed";
    }

    // ================= HISTORY =================
    @GetMapping("/history")
    public String showHistory(HttpSession session, Model model) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user == null)
            return "redirect:/auth/login";

        List<ListeningHistoryDTO> history = historyService.getUserHistory(user.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("history", history);
        model.addAttribute("role", user.getRole());
        return "auth/history";
    }

    @PostMapping("/history/clear")
    public String clearHistory(HttpSession session) {
        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
        if (user != null) {
            historyService.clearUserHistory(user.getUserId());
        }
        return "redirect:/dashboard/history";
    }

    // ================= PROFILE =================
    @GetMapping("/profile/{id}")
    public String showProfile(@PathVariable Long id, HttpSession session, Model model) {
        UserAccount loggedInUser = (UserAccount) session.getAttribute("loggedUser");
        if (loggedInUser == null)
            return "redirect:/auth/login";

        // Logic to fetch user by ID if viewing someone else's profile
        // For now, redirect to self if IDs match or just show self
        model.addAttribute("user", loggedInUser);
        model.addAttribute("role", loggedInUser.getRole());

        UserStatsDTO stats = statsService.getUserStats(loggedInUser.getUserId());
        model.addAttribute("stats", stats);

        return "auth/Profile";
    }

    @PostMapping("/profile/{id}")
    public String updateProfile(@PathVariable Long id,
            @RequestParam String displayName,
            @RequestParam String bio,
            @RequestParam String profilePicture,
            @RequestParam(required = false) String password,
            HttpSession session) {
        UserAccount loggedInUser = (UserAccount) session.getAttribute("loggedUser");
        if (loggedInUser == null || !loggedInUser.getUserId().equals(id)) {
            return "redirect:/auth/login";
        }

        com.rev.dto.UserDTO dto = new com.rev.dto.UserDTO();
        dto.setDisplayName(displayName);
        dto.setBio(bio);
        dto.setProfilePicture(profilePicture);
        if (password != null && !password.trim().isEmpty()) {
            dto.setPassword(password);
        }

        UserAccount updatedUser = userService.updateUser(id, dto);
        session.setAttribute("loggedUser", updatedUser);

        return "redirect:/dashboard/profile/" + id;
    }
}
