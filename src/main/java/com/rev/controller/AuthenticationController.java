package com.rev.controller;

import com.rev.dto.UserDTO;
import com.rev.entity.UserAccount;
import com.rev.service.FavoriteServiceInterface;
import com.rev.service.ListeningHistoryServiceInterface;
import com.rev.service.PlaylistServiceInterface;
import com.rev.service.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserServiceInterface userService;
    private final ListeningHistoryServiceInterface listeningHistoryService;
    private final FavoriteServiceInterface favoritesService;
    private final PlaylistServiceInterface playlistService;

    // ================= REGISTER =================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user_register";   // Thymeleaf template
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
        try {
            userService.registerUser(userDTO);
            model.addAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/User_Login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user_register";
        }
    }

    // ================= LOGIN =================
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "User_Login"; // Must match Thymeleaf file
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {
        try {
            UserAccount user = userService.login(email, password);
            session.setAttribute("loggedUser", user);
            return "redirect:/UserDashboard";  // After login go to dashboard
        } catch (Exception e) {
            model.addAttribute("loginError", e.getMessage());
            return "User_Login";
        }
    }
}