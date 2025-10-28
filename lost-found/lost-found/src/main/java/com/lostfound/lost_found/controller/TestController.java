// src/main/java/com/lostfound/controller/TestController.java
package com.lostfound.lost_found.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    
    @GetMapping("/test")
    public String testPage(Model model) {
        model.addAttribute("message", "Spring Boot is working!");
        return "test";
    }
}