package com.healthtracker.service;

import com.healthtracker.model.HealthEntry;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class AiService {

    private static final Logger logger = LoggerFactory.getLogger(AiService.class);

    private final Optional<OllamaChatModel> chatClient;
    private final MockAiService mockAiService;

    @Autowired
    public AiService(Optional<OllamaChatModel> chatClient, MockAiService mockAiService) {
        this.chatClient = chatClient;
        this.mockAiService = mockAiService;
    }

    @PostConstruct
    private void checkAiConfiguration() {
        if (chatClient.isEmpty()) {
            logger.warn("*********************************************************************************");
            logger.warn("*** OllamaChatModel bean not found. AI service will use fallback responses.   ***");
            logger.warn("*** Please ensure Ollama is running and configured in application.properties. ***");
            logger.warn("*********************************************************************************");
        } else {
            logger.info("✅ OllamaChatModel bean found. AI service is active.");
        }
    }

    // ✅ Enhanced method with longer, more detailed suggestions
    public String generateWellnessTip(String mood, String meals, String symptoms, int sleepHours) {
        if (chatClient.isEmpty()) {
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

            return chatClient.get().call(prompt);
        } catch (Exception e) {
            logger.error("Error calling Ollama for wellness tip: {}", e.getMessage());
            return generateFallbackWellnessTip(mood, meals, symptoms, sleepHours);
        }
    }

    private String generateFallbackWellnessTip(String mood, String meals, String symptoms, int sleepHours) {
        return mockAiService.generateWellnessTip(mood, meals, symptoms, sleepHours);
    }

    // ✅ NEW: Overloaded method accepting HealthEntry directly
    public String generateWellnessTip(String mood, List<String> meals, List<String> symptoms, int sleepHours) {
        if (chatClient.isEmpty()) {
            return generateFallbackWellnessTip(mood, String.join(", ", meals), String.join(", ", symptoms), sleepHours);
        }
        
        try {
            String prompt = "You are a wellness assistant. Give a short, helpful tip based on:\n" +
                            "Mood: " + mood + "\n" +
                            "Meals: " + String.join(", ", meals) + "\n" +
                            "Symptoms: " + String.join(", ", symptoms) + "\n" +
                            "Sleep Hours: " + sleepHours + "\n" +
                            "Provide specific, actionable advice in 2-3 sentences.";

            return chatClient.get().call(prompt);
        } catch (Exception e) {
            logger.error("Error calling Ollama for wellness tip (list overload): {}", e.getMessage());
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
        if (chatClient.isPresent()) {
            try {
                String aiPrompt = "Based on this health data analysis:\n" + analysis.toString() +
                                 "\nProvide 2-3 specific, actionable wellness recommendations.";
                return analysis.toString() + "\n\nAI Recommendations:\n" + chatClient.get().call(aiPrompt);
            } catch (Exception e) {
                logger.error("Error calling Ollama for health trend analysis: {}", e.getMessage());
                return analysis.toString() + "\n\nAI Recommendations: Unable to generate AI recommendations at this time.";
            }
        } else {
            return analysis.toString() + "\n\nAI Recommendations: AI service not available.";
        }
    }

    public String generateNutritionAdvice(String meals) {
        if (chatClient.isEmpty()) {
            return mockAiService.generateNutritionAdvice(meals);
        }
        
        try {
            String prompt = "You are a nutrition expert. Analyze this meal description and provide specific nutrition advice:\n" +
                            "Meals: " + meals + "\n" +
                            "Provide 2-3 specific nutrition tips or suggestions for improvement.";

            return chatClient.get().call(prompt);
        } catch (Exception e) {
            logger.error("Error calling Ollama for nutrition advice: {}", e.getMessage());
            return mockAiService.generateNutritionAdvice(meals);
        }
    }

    public String generateMoodAdvice(String mood, String symptoms) {
        if (chatClient.isEmpty()) {
            return mockAiService.generateMoodAdvice(mood, symptoms);
        }
        
        try {
            String prompt = "You are a mental health and wellness expert. Based on this mood and symptoms, provide supportive advice:\n" +
                            "Mood: " + mood + "\n" +
                            "Symptoms: " + symptoms + "\n" +
                            "Provide 2-3 supportive, actionable suggestions for improving mood and well-being.";

            return chatClient.get().call(prompt);
        } catch (Exception e) {
            logger.error("Error calling Ollama for mood advice: {}", e.getMessage());
            return mockAiService.generateMoodAdvice(mood, symptoms);
        }
    }

    public String generateMentalHealthAdvice(String concern, String mood, String symptoms) {
        if (chatClient.isEmpty()) {
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

            return chatClient.get().call(prompt);
        } catch (Exception e) {
            logger.error("Error calling Ollama for mental health advice: {}", e.getMessage());
            return "Unable to generate mental health advice at this time. Please consider contacting a mental health professional.";
        }
    }

    public String generateChatResponse(String userMessage) {
        if (chatClient.isEmpty()) {
            return generateMockChatResponse(userMessage);
        }
        
        try {
            String prompt = "You are a helpful AI health assistant for HealthVerse. " +
                            "Respond to this user message in a friendly, supportive, and informative way: " +
                            userMessage + "\n\n" +
                            "Provide a helpful response that's relevant to health and wellness. " +
                            "Keep it conversational and under 100 words.";

            return chatClient.get().call(prompt);
        } catch (Exception e) {
            logger.error("Error calling Ollama for chat response: {}", e.getMessage());
            return generateMockChatResponse(userMessage);
        }
    }

    private String generateMockChatResponse(String userMessage) {
        String message = userMessage.toLowerCase();
        
        if (message.contains("hello") || message.contains("hi") || message.contains("hey")) {
            return "Hello! I'm your AI health assistant. How can I help you with your wellness journey today?";
        } else if (message.contains("sleep") || message.contains("tired")) {
            return "Sleep is crucial for good health! Aim for 7-9 hours nightly. Try establishing a bedtime routine and avoiding screens before bed.";
        } else if (message.contains("stress") || message.contains("anxious")) {
            return "I understand stress can be challenging. Try deep breathing exercises, take short walks, or practice mindfulness. What's causing you stress?";
        } else if (message.contains("diet") || message.contains("food") || message.contains("eat")) {
            return "Good nutrition is key to wellness! Focus on whole foods, plenty of vegetables, lean proteins, and stay hydrated. What are your dietary goals?";
        } else if (message.contains("exercise") || message.contains("workout")) {
            return "Regular exercise is fantastic for both physical and mental health! Even 15-30 minutes of daily activity can make a big difference. What activities do you enjoy?";
        } else if (message.contains("pain") || message.contains("hurt")) {
            return "I'm sorry you're experiencing pain. While I can offer general wellness tips, please consult a healthcare provider for persistent pain or concerning symptoms.";
        } else if (message.contains("thank")) {
            return "You're very welcome! I'm here to support your health journey. Feel free to ask me anything about wellness, nutrition, sleep, or general health tips.";
        } else {
            return "That's an interesting question! I'm here to help with health and wellness topics. Could you tell me more about what specific health area you'd like to discuss?";
        }
    }
}
