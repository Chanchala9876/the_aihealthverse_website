package com.healthtracker.service;

import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.Arrays;
import java.util.List;

@Service
public class MockAiService {
    
    private final Random random = new Random();
    
    public String generateWellnessTip(String mood, String meals, String symptoms, int sleepHours) {
        StringBuilder tip = new StringBuilder();
        
        // Mood-based advice
        if (mood.toLowerCase().contains("stressed") || mood.toLowerCase().contains("anxious")) {
            tip.append("🧘 **Stress Management**: Try deep breathing exercises or a 10-minute meditation. ");
        } else if (mood.toLowerCase().contains("tired") || mood.toLowerCase().contains("exhausted")) {
            tip.append("😴 **Energy Boost**: Consider a short walk or light stretching to naturally boost energy. ");
        } else if (mood.toLowerCase().contains("happy") || mood.toLowerCase().contains("good")) {
            tip.append("😊 **Maintain Positivity**: Great to see you're feeling well! Keep up the good habits. ");
        }
        
        // Sleep-based advice
        if (sleepHours < 6) {
            tip.append("💤 **Sleep Priority**: Your sleep is below recommended levels. Aim for 7-9 hours tonight. ");
        } else if (sleepHours > 9) {
            tip.append("⏰ **Sleep Balance**: You might be oversleeping. Try maintaining a consistent 7-8 hour schedule. ");
        } else {
            tip.append("✅ **Good Sleep**: Your sleep duration looks healthy! ");
        }
        
        // Meal-based advice
        if (meals.toLowerCase().contains("fast food") || meals.toLowerCase().contains("junk")) {
            tip.append("🥗 **Nutrition Focus**: Try incorporating more whole foods like fruits, vegetables, and lean proteins. ");
        } else if (meals.toLowerCase().contains("vegetable") || meals.toLowerCase().contains("fruit")) {
            tip.append("🌟 **Great Nutrition**: Excellent food choices! Your body is getting good nutrients. ");
        }
        
        // Symptom-based advice
        if (!symptoms.trim().isEmpty() && !symptoms.toLowerCase().contains("none")) {
            tip.append("⚕️ **Health Monitoring**: Keep tracking your symptoms. If they persist, consider consulting a healthcare provider. ");
        }
        
        // General wellness tips
        List<String> generalTips = Arrays.asList(
            "💧 Stay hydrated with 8 glasses of water daily.",
            "🚶 Take a 15-minute walk to boost circulation and mood.",
            "🧘 Practice mindfulness for 5 minutes to reduce stress.",
            "📱 Limit screen time before bed for better sleep quality.",
            "🥗 Include colorful vegetables in your next meal.",
            "☀️ Get some natural sunlight for vitamin D and mood enhancement."
        );
        
        tip.append(generalTips.get(random.nextInt(generalTips.size())));
        
        return tip.toString();
    }
    
    public String generateNutritionAdvice(String meals) {
        if (meals.toLowerCase().contains("vegetable") && meals.toLowerCase().contains("protein")) {
            return "🌟 **Excellent Balance**: Your meals show a good mix of vegetables and protein. " +
                   "Consider adding healthy fats like avocado or nuts for complete nutrition.";
        } else if (meals.toLowerCase().contains("fast food")) {
            return "🥗 **Nutrition Improvement**: Try replacing one fast food item with a homemade alternative. " +
                   "Add leafy greens and lean protein to boost nutritional value.";
        } else {
            return "💡 **Nutrition Tip**: Aim for colorful plates with vegetables, lean protein, and whole grains. " +
                   "Stay hydrated and consider meal prep for consistent healthy eating.";
        }
    }
    
    public String generateMoodAdvice(String mood, String symptoms) {
        if (mood.toLowerCase().contains("stressed")) {
            return "🧘 **Stress Relief**: Try the 4-7-8 breathing technique: inhale for 4, hold for 7, exhale for 8. " +
                   "Consider journaling or talking to someone you trust about what's causing stress.";
        } else if (mood.toLowerCase().contains("sad") || mood.toLowerCase().contains("down")) {
            return "💙 **Mood Support**: It's okay to feel down sometimes. Try gentle exercise, listen to uplifting music, " +
                   "or engage in activities you enjoy. Reach out to friends or family for support.";
        } else {
            return "😊 **Mood Maintenance**: Keep doing what makes you feel good! " +
                   "Regular exercise, good sleep, and social connections help maintain positive mood.";
        }
    }
}
