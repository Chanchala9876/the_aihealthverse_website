package com.healthtracker.service;

import com.healthtracker.model.HealthEntry;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiService {

    @Autowired(required = false)
    private OllamaChatModel chatClient;

    // ✅ Enhanced method with longer, more detailed suggestions
    public String generateWellnessTip(String mood, String meals, String symptoms, int sleepHours) {
        if (chatClient == null) {
            return generateFallbackWellnessTip(mood, meals, symptoms, sleepHours);
        }
        
        try {
            String prompt = "You are an expert wellness and health advisor. Provide a comprehensive, detailed health analysis and personalized recommendations based on the following daily health data:\n\n" +
                            "📊 TODAY'S HEALTH DATA:\n" +
                            "• Mood: " + mood + "\n" +
                            "• Meals consumed: " + meals + "\n" +
                            "• Physical symptoms/concerns: " + symptoms + "\n" +
                            "• Sleep duration: " + sleepHours + " hours\n\n" +
                            "Please provide a detailed wellness analysis that includes:\n" +
                            "1. Overall health assessment based on the data\n" +
                            "2. Specific dietary recommendations and meal timing advice\n" +
                            "3. Sleep quality analysis and improvement suggestions\n" +
                            "4. Mood management strategies and mental wellness tips\n" +
                            "5. Physical symptom management advice (if applicable)\n" +
                            "6. Lifestyle modifications for better overall health\n" +
                            "7. Preventive care recommendations\n\n" +
                            "Format your response in a clear, organized manner with specific, actionable advice. " +
                            "Make it comprehensive but easy to understand. Aim for 150-200 words with practical tips the user can implement today.";

            return chatClient.call(prompt);
        } catch (Exception e) {
            return generateFallbackWellnessTip(mood, meals, symptoms, sleepHours);
        }
    }

    private String generateFallbackWellnessTip(String mood, String meals, String symptoms, int sleepHours) {
        return "Due to the current unavailability of the AI service, detailed wellness tips cannot be generated. Review your mood, diet, and symptoms for insights.";
    }

    // ✅ NEW: Overloaded method accepting HealthEntry directly
    public String generateWellnessTip(String mood, List<String> meals, List<String> symptoms, int sleepHours) {
        if (chatClient == null) {
            return generateFallbackWellnessTip(mood, String.join(", ", meals), String.join(", ", symptoms), sleepHours);
        }
        
        try {
            String prompt = "You are a wellness assistant. Give a short, helpful tip based on:\n" +
                            "Mood: " + mood + "\n" +
                            "Meals: " + String.join(", ", meals) + "\n" +
                            "Symptoms: " + String.join(", ", symptoms) + "\n" +
                            "Sleep Hours: " + sleepHours + "\n" +
                            "Provide specific, actionable advice in 2-3 sentences.";

            return chatClient.call(prompt);
        } catch (Exception e) {
            return generateFallbackWellnessTip(mood, String.join(", ", meals), String.join(", ", symptoms), sleepHours);
        }
    }

    public String analyzeHealthTrends(List<HealthEntry> entries) {
        if (entries.isEmpty()) {
            return "No health data available for analysis.";
        }

        StringBuilder analysis = new StringBuilder();
        analysis.append("Based on your recent health entries:\n\n");

        // Analyze mood trends
        long positiveMoods = entries.stream()
                .filter(entry -> entry.getMood() != null &&
                        (entry.getMood().toLowerCase().contains("happy") ||
                         entry.getMood().toLowerCase().contains("good") ||
                         entry.getMood().toLowerCase().contains("great")))
                .count();

        double positiveMoodPercentage = (double) positiveMoods / entries.size() * 100;
        analysis.append("• Mood: ").append(String.format("%.1f", positiveMoodPercentage))
                .append("% of your entries show positive mood\n");

        // Analyze sleep patterns
        double avgSleep = entries.stream()
                .mapToInt(HealthEntry::getSleepHours)
                .average()
                .orElse(0.0);

        analysis.append("• Sleep: Average ").append(String.format("%.1f", avgSleep))
                .append(" hours per night\n");

        if (avgSleep < 7) {
            analysis.append("  Consider improving your sleep routine for better health.\n");
        }

        // Generate AI recommendations
        if (chatClient != null) {
            try {
                String aiPrompt = "Based on this health data analysis:\n" + analysis.toString() +
                                 "\nProvide 2-3 specific, actionable wellness recommendations.";
                return analysis.toString() + "\n\nAI Recommendations:\n" + chatClient.call(aiPrompt);
            } catch (Exception e) {
                return analysis.toString() + "\n\nAI Recommendations: Unable to generate AI recommendations at this time.";
            }
        } else {
            return analysis.toString() + "\n\nAI Recommendations: AI service not available.";
        }
    }

    public String generateNutritionAdvice(String meals) {
        if (chatClient == null) {
            return "AI nutrition advice is currently unavailable. Consider consulting with a registered dietitian for personalized nutrition guidance.";
        }
        
        try {
            String prompt = "You are a nutrition expert. Analyze this meal description and provide specific nutrition advice:\n" +
                            "Meals: " + meals + "\n" +
                            "Provide 2-3 specific nutrition tips or suggestions for improvement.";

            return chatClient.call(prompt);
        } catch (Exception e) {
            return "Unable to generate nutrition advice at this time. Please try again later.";
        }
    }

    public String generateMoodAdvice(String mood, String symptoms) {
        if (chatClient == null) {
            return "AI mood advice is currently unavailable. If you're experiencing persistent mood issues, consider speaking with a mental health professional.";
        }
        
        try {
            String prompt = "You are a mental health and wellness expert. Based on this mood and symptoms, provide supportive advice:\n" +
                            "Mood: " + mood + "\n" +
                            "Symptoms: " + symptoms + "\n" +
                            "Provide 2-3 supportive, actionable suggestions for improving mood and well-being.";

            return chatClient.call(prompt);
        } catch (Exception e) {
            return "Unable to generate mood advice at this time. Please try again later.";
        }
    }

    public String generateMentalHealthAdvice(String concern, String mood, String symptoms) {
        if (chatClient == null) {
            return "AI mental health guidance is currently unavailable. For immediate support, please contact a mental health professional or crisis helpline.";
        }
        
        try {
            String prompt = "You are a professional mental health counselor and therapist. Provide comprehensive, empathetic mental health guidance based on the following information:\n\n" +
                            "🧠 PATIENT INFORMATION:\n" +
                            "• Main Concern: " + concern + "\n" +
                            "• Current Mood: " + mood + "\n" +
                            "• Symptoms/Feelings: " + symptoms + "\n\n" +
                            "Please provide a detailed, supportive response that includes:\n" +
                            "1. Validation and empathy for their current situation\n" +
                            "2. Professional assessment of their mental health state\n" +
                            "3. Specific coping strategies and techniques\n" +
                            "4. Breathing exercises or mindfulness practices\n" +
                            "5. Lifestyle recommendations for mental wellness\n" +
                            "6. When to seek professional help\n" +
                            "7. Positive affirmations and encouragement\n\n" +
                            "Format your response in a warm, professional, and supportive tone. " +
                            "Provide actionable advice that the person can implement immediately. " +
                            "Include disclaimers about professional medical advice when appropriate. " +
                            "Aim for 200-300 words with practical, evidence-based recommendations.";

            return chatClient.call(prompt);
        } catch (Exception e) {
            return "Unable to generate mental health advice at this time. Please consider contacting a mental health professional.";
        }
    }
}
