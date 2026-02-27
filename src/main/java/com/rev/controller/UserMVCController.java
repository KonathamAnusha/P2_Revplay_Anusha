
package com.rev.controller;

import com.rev.dto.UserDTO;
import com.rev.entity.UserAccount;
import com.rev.mapper.UserMapper;
import com.rev.service.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserMVCController {

    private final UserServiceInterface userService;
    private final UserMapper userMapper;

    // Home Page
    @GetMapping("/")
    public String homePage() {
        return "home";   // ❌ remove slash
    }

    // Registration Page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user_register";   // ❌ remove slash
    }

    // Handle Registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
        try {
            UserAccount savedUser = userService.registerUser(userDTO);
            model.addAttribute("successMessage", "Registration successful! Please login.");
            return "userLogin";   // correct
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user_register";
        }
    }

    // Login Page
    @GetMapping("/login")
    public String showLoginPage() {
        return "userLogin";   // ❌ remove slash
    }

    // Handle Login
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            Model model) {
        try {
            UserAccount user = userService.login(email, password);
            model.addAttribute("user", user);
            return "dashboard";
        } catch (Exception e) {
            model.addAttribute("loginError", e.getMessage());
            return "userLogin";
        }
    }
}