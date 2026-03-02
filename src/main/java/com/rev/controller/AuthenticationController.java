package com.rev.controller;

import com.rev.dto.UserDTO;
import com.rev.entity.UserAccount;
import com.rev.entity.UserAccount.Role;
import com.rev.service.UserServiceInterface;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserServiceInterface userService;
    // ----- REGISTER PROCESS -----
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDTO") UserDTO userDTO,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            // Only needed if role is ARTIST (already initialized in DTO)
            userService.registerUser(userDTO);

            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/auth/login";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user_register";
        }
    }

    // ----- LOGIN PROCESS -----
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {

        try {
            UserAccount user = userService.login(email, password);

            if (user == null) {
                model.addAttribute("loginError", "Invalid email or password.");
                return "User_Login";
            }

            // Save user in session
            session.setAttribute("loggedUser", user);

            // Single dashboard, content differs based on role
            return "redirect:/dashboard";

        } catch (Exception e) {
            model.addAttribute("loginError", e.getMessage());
            return "User_Login";
        }
    }}