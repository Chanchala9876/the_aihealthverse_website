package com.healthtracker.controller;

import com.healthtracker.model.Reminder;
import com.healthtracker.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reminder")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @GetMapping
    public String viewReminders(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            
            List<Reminder> reminders = reminderService.getUserReminders(userId);
            List<Reminder> activeReminders = reminderService.getActiveReminders(userId);
            
            model.addAttribute("reminders", reminders);
            model.addAttribute("activeReminders", activeReminders);
            model.addAttribute("title", "Reminders - HealthVerse");
            return "reminder";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading reminders: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("title", "Create Reminder - HealthVerse");
        return "reminder-create";
    }

    @PostMapping("/create")
    public String createReminder(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String reminderTime,
            @RequestParam String type,
            Model model) {
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            
            Reminder reminder = new Reminder();
            reminder.setUserId(userId);
            reminder.setTitle(title);
            reminder.setDescription(description);
            reminder.setReminderTime(LocalDateTime.parse(reminderTime));
            reminder.setType(type);
            
            reminderService.createReminder(reminder);
            
            model.addAttribute("reminder", reminder);
            model.addAttribute("title", "Reminder Created - HealthVerse");
            return "reminder-success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating reminder: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}/complete")
    @ResponseBody
    public String markCompleted(@PathVariable String id) {
        Reminder reminder = reminderService.markReminderCompleted(id);
        return reminder != null ? "success" : "error";
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteReminder(@PathVariable String id) {
        reminderService.deleteReminder(id);
        return "success";
    }
} 