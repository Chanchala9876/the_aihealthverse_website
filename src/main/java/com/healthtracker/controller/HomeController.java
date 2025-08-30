package com.healthtracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.healthtracker.service.HealthService;
import com.healthtracker.service.ReminderService;
import com.healthtracker.model.HealthEntry;
import com.healthtracker.model.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Controller
public class HomeController {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private HealthService healthService;
    @Autowired
    private ReminderService reminderService;

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser");
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("title", "HealthVerse - Your Personal Health Journal");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - HealthVerse");
        return "about";
    }
    
    @GetMapping("/test")
    public String test() {
        logger.info("HomeController: test() method called");
        return "Test endpoint working!";
    }
    
    @GetMapping("/error")
    public String error() {
        logger.info("HomeController: error() method called");
        return "error";
    }
} 