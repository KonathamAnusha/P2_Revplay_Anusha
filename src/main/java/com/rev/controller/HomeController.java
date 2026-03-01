package com.rev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // ================= HOME PAGE =================
    @GetMapping("/home")
    public String homePage() {
        return "Home";  // Home.html in templates
    }


}