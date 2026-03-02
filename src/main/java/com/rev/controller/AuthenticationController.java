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

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserServiceInterface userService;

    // ----- LOGIN PAGE -----
    @GetMapping("/login")
    public String showLoginPage() {
        return "User_Login";  // Must exactly match src/main/resources/templates/User_Login.html
    }

    // ----- LOGIN PROCESS -----
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            Model model) {

        try {
            // login returns UserAccount (role included)
            UserAccount user = userService.login(email, password);

            // save logged user in session
            session.setAttribute("loggedUser", user);

            // redirect based on role
            if (user.getRole() == Role.LISTENER) {
                return "redirect:/listener/dashboard";
            } else {
                return "redirect:/artist/dashboard";
            }

        } catch (Exception e) {
            model.addAttribute("loginError", e.getMessage());
            return "User_Login";
        }
    }

    // ----- REGISTER PAGE -----
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "User_Register"; // Must match template
    }

    // ----- REGISTER PROCESS -----
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
        try {
            userService.registerUser(userDTO);
            model.addAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/auth/login"; // fixed path
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "User_Register";
        }
    }

    // ----- LOGOUT -----
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}