package com.rev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // ================= ROOT =================
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    // ================= HOME PAGE =================
    @GetMapping("/home")
    public String homePage() {
        return "auth/Home"; // Home.html in auth folder
    }
}