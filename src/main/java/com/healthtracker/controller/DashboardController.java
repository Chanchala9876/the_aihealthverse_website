package com.healthtracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.healthtracker.model.User;
import com.healthtracker.service.UserService;
import com.healthtracker.service.HealthService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private HealthService healthService;

    @GetMapping
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                model.addAttribute("errorMessage", "User not found. Please log in again.");
                return "error";
            }
            model.addAttribute("user", user);
            model.addAttribute("title", "Dashboard - HealthVerse");
            // Add dashboard-specific data
            String role = (user.getRoles() != null && !user.getRoles().isEmpty()) ? user.getRoles().get(0) : null;
            if (role == null) {
                model.addAttribute("errorMessage", "User role not set. Please contact support.");
                return "error";
            }
            if ("PATIENT".equals(role)) {
                // Get recent health entries for patients
                java.util.List<com.healthtracker.model.HealthEntry> allEntries = healthService.getUserEntries(user.getId());
                java.util.List<com.healthtracker.model.HealthEntry> recentEntries = allEntries.stream().limit(5).toList();
                model.addAttribute("recentEntries", recentEntries);
                return "dashboard/patient-dashboard";
            } else if ("DOCTOR".equals(role)) {
                // Get pending consultations for doctors
                return "dashboard/doctor-dashboard";
            }
            return "dashboard/dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading dashboard: " + e.getMessage());
            return "error";
        }
    }
}
