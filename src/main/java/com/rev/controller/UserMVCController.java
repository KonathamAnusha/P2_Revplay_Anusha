//package com.rev.controller;
//
//import com.rev.dto.UserDTO;
//import com.rev.entity.Playlist;
//import com.rev.entity.Songs;
//import com.rev.entity.UserAccount;
//import com.rev.service.FavoriteServiceInterface;
//import com.rev.service.ListeningHistoryServiceInterface;
//import com.rev.service.PlaylistServiceInterface;
//import com.rev.service.UserServiceInterface;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.servlet.http.HttpSession;
//import java.util.List;
//
//@Controller
//@RequiredArgsConstructor
//public class UserMVCController {
//
//    private final UserServiceInterface userService;
//    private final ListeningHistoryServiceInterface listeningHistoryService;
//    private final FavoriteServiceInterface favoritesService;
//    private final PlaylistServiceInterface playlistService;
//
//    // ================= HOME =================
//    @GetMapping("/")
//    public String homePage() {
//        return "Home";  // Home.html must exist in templates
//    }
//
//    // ================= REGISTER =================
//    @GetMapping("/register")
//    public String showRegisterForm(Model model) {
//        model.addAttribute("userDTO", new UserDTO());
//        return "user_register";   // user_register.html
//    }
//
//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
//        try {
//            userService.registerUser(userDTO);
//            model.addAttribute("successMessage", "Registration successful! Please login.");
//            return "redirect:/login"; // Redirect to GET /login
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "user_register";
//        }
//    }
//
//    // ================= LOGIN =================
//    @GetMapping("/login")
//    public String showLoginForm(Model model) {
//        return "User_Login"; // Must match Thymeleaf file: User_Login.html
//    }
//
//    @PostMapping("/login")
//    public String loginUser(@RequestParam String email,
//                            @RequestParam String password,
//                            HttpSession session,
//                            Model model) {
//        try {
//            // Attempt login
//            UserAccount user = userService.login(email, password);
//
//            // Store logged-in user in session
//            session.setAttribute("loggedUser", user);
//
//            return "redirect:/dashboard"; // Redirect to dashboard
//        } catch (Exception e) {
//            model.addAttribute("loginError", e.getMessage());
//            return "User_Login";
//        }
//    }
//
//    // ================= DASHBOARD =================
//    @GetMapping("/dashboard")
//    public String showDashboard(HttpSession session, Model model) {
//
//        UserAccount user = (UserAccount) session.getAttribute("loggedUser");
//        if (user == null) {
//            return "redirect:/login"; // Not logged in
//        }
//
//        model.addAttribute("user", user);
//
//        // Fetch actual data
//        List<Songs> recentlyPlayed = listeningHistoryService.getRecentUserHistory(user.getUserId());
//        List<Songs> favorites = favoritesService.getUserFavorites(user.getUserId());
//        List<Playlist> playlists = playlistService.getPlaylistsByUserId(zuser.getUserId());
//
//        model.addAttribute("recentlyPlayed", recentlyPlayed);
//        model.addAttribute("favorites", favorites);
//        model.addAttribute("playlists", playlists);
//
//        return "UserDashboard"; // Thymeleaf template
//    }
//
//    // ================= LOGOUT =================
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "redirect:/login";
//    }
//}