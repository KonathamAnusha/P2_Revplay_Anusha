package com.rev.controller;

import com.rev.dto.UserDTO;
import com.rev.service.UserServiceInterface;
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

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDTO") UserDTO userDTO,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            userService.registerUser(userDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/register";
        }
    }

    // GET only — Spring Security handles POST /auth/login via formLogin()
    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    // NOTE: Login POST and Logout POST are handled by Spring Security.
    // See SecurityConfig.java formLogin() and logout() configuration.
}
