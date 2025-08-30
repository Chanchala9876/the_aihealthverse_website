package com.healthtracker.controller;

import com.healthtracker.model.HealthEntry;
import com.healthtracker.service.HealthService;
import com.healthtracker.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private HealthService healthService;

    @Autowired
    private AiService aiService;

    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/log")
    public String showLogForm(Model model) {
        model.addAttribute("title", "Log Health Entry - HealthVerse");
        return "health/log";
    }
   
    @PostMapping("/log")
    public String logHealthEntry(
            @RequestParam String mood,
            @RequestParam String meals,
            @RequestParam String symptoms,
            @RequestParam int sleepHours,
            @RequestParam(required = false) String notes,
            Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            
            // Create and save entry (AI suggestion will be generated and saved in HealthService)
            HealthEntry entry = healthService.createEntryFromForm(userId, mood, meals, symptoms, sleepHours, notes);
            
            // Get the AI suggestion that was generated and saved
            String suggestion = entry.getAiSuggestion();

            model.addAttribute("entry", entry);
            model.addAttribute("suggestion", suggestion);
            model.addAttribute("success", true);
            model.addAttribute("title", "Health Entry Logged - HealthVerse");
            return "health/log-success";
        } catch (Exception e) {
            logger.error("Error logging health entry or generating AI suggestion", e);
            model.addAttribute("errorMessage", "Sorry, something went wrong: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/entries")
    public String viewEntries(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        List<HealthEntry> entries = healthService.getUserEntries(userId);
        model.addAttribute("entries", entries);
        model.addAttribute("title", "Health Entries - HealthVerse");
        return "health/entries";
    }

    @GetMapping("/analysis")
    public String showAnalysis(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        String analysis = healthService.analyzeUserHealth(userId);
        List<HealthEntry> recentEntries = healthService.getUserEntries(userId);
        model.addAttribute("analysis", analysis);
        model.addAttribute("recentEntries", recentEntries);
        model.addAttribute("title", "Health Analysis - HealthVerse");
        return "health/analysis";
    }

    @GetMapping("/nutrition-advice")
    public String showNutritionAdvice(@RequestParam String meals, Model model) {
        String advice = aiService.generateNutritionAdvice(meals);
        model.addAttribute("advice", advice);
        model.addAttribute("meals", meals);
        model.addAttribute("title", "Nutrition Advice - HealthVerse");
        return "health/nutrition-advice";
    }

    @GetMapping("/mood-advice")
    public String showMoodAdvice(@RequestParam String mood, @RequestParam String symptoms, Model model) {
        String advice = aiService.generateMoodAdvice(mood, symptoms);
        model.addAttribute("advice", advice);
        model.addAttribute("mood", mood);
        model.addAttribute("symptoms", symptoms);
        model.addAttribute("title", "Mood Advice - HealthVerse");
        return "health/mood-advice";
    }
}
