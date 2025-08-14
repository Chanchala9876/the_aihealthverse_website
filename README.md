# HealthTracker - Personal Health Journal with AI Tips

HealthTracker is a comprehensive health tracking application that combines daily health logging with AI-powered insights and recommendations. Built with Spring Boot, MongoDB, and Ollama integration (via Spring AI), it provides users with personalized wellness advice based on their health data.

## 🌟 Features

### Core Features
- **Daily Health Logging**: Track mood, meals, sleep, and symptoms
- **AI-Powered Insights**: Get personalized wellness recommendations using Ollama (via Spring AI)
- **Health Analytics**: Beautiful charts and trend analysis
- **Smart Reminders**: Set medication and health appointment reminders
- **PDF Export**: Generate comprehensive health reports for doctors
- **Responsive Design**: Works seamlessly on all devices
- **Doctor Portal**: Connect with real doctors for real-time chat and consultations
- **Mental Health Support**: Access AI-driven and doctor-led mental health sessions
- **Real-Time Chat**: Communicate instantly using WebSocket-based chat

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- MongoDB (local or cloud)
- [Ollama](https://ollama.com/) (local LLM server)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd HealthTracker
   ```

2. **Configure MongoDB**
   - Install MongoDB locally or use MongoDB Atlas
   - Update `application.properties` with your MongoDB connection string

3. **Set up Ollama**
   - Download and install Ollama from [Ollama’s website](https://ollama.com/)
   - Start the Ollama server locally (default: `http://localhost:11434`)
   - Pull your desired model (e.g., llama2):
     ```bash
     ollama pull llama2
     ```
   - Update the Ollama config in `application.properties` (see below)

4. **Configure Application Properties**
   - Copy `src/main/resources/application-sample.properties` to `src/main/resources/application.properties`.
   - Fill in your MongoDB and Ollama credentials.

5. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

6. **Access the application**
   - Open your browser and go to `http://localhost:8090`

## 📁 Project Structure

```
HealthTracker/
├── src/main/java/com/healthtracker/
│   ├── config/
│   │   └── SecurityConfig.java          # Security configuration
│   ├── controller/
│   │   ├── HomeController.java          # Main navigation
│   │   ├── HealthController.java        # Health logging & analysis
│   │   ├── ReminderController.java      # Reminder management
│   │   └── ExportController.java        # PDF export
│   ├── model/
│   │   ├── User.java                    # User entity
│   │   ├── HealthEntry.java             # Health log entries
│   │   └── Reminder.java                # Reminder entity
│   ├── repository/
│   │   ├── UserRepository.java          # User data access
│   │   ├── HealthEntryRepository.java   # Health entries data access
│   │   └── ReminderRepository.java      # Reminder data access
│   └── service/
│       ├── AiService.java               # OpenAI integration
│       ├── HealthService.java           # Health data management
│       ├── ReminderService.java         # Reminder management
│       └── PdfExportService.java        # PDF generation
├── src/main/resources/
│   ├── templates/
│   │   ├── home.html                    # Landing page
│   │   ├── dashboard.html               # Main dashboard
│   │   ├── about.html                   # About page
│   │   └── health/
│   │       ├── log.html                 # Health logging form
│   │       ├── entries.html             # Health entries view
│   │       └── analysis.html            # AI analysis page
│   └── application.properties           # Configuration
└── pom.xml                             # Maven dependencies
```

## 🛠️ Technology Stack

### Backend
- **Spring Boot**: Main framework
- **Spring AI**: Ollama integration
- **Spring Data MongoDB**: Database operations
- **Spring Security**: Authentication & authorization
- **WebSocket**: Real-time chat
- **Thymeleaf**: Server-side templating


### Frontend
- **Bootstrap 5**: Responsive UI framework
- **Chart.js**: Interactive charts and graphs
- **Font Awesome**: Icons
- **Google Fonts**: Typography

### Database
- **MongoDB**: NoSQL database for flexible health data storage

### AI/ML
- **Ollama (via Spring AI)**: Natural language processing and health insights

## 📊 Key Features Explained

### 1. Health Logging
- **Mood Tracking**: Select from predefined moods (Happy, Good, Neutral, Tired, Stressed, Sad)
- **Meal Logging**: Record daily meals and snacks

- **Symptom Monitoring**: Track health symptoms and concerns
- **Notes**: Add additional observations

### 2. AI-Powered Analysis
- **Personalized Recommendations**: AI analyzes your health patterns
- **Nutrition Advice**: Get dietary suggestions based on meal logs
- **Mood Insights**: Receive mental health and wellness tips
- **Trend Analysis**: Identify patterns in your health data

### 3. Dashboard & Analytics
- **Health Metrics**: View key health indicators
- **Interactive Charts**: Visualize mood and sleep trends
- **Quick Actions**: Easy access to common tasks
- **Recent Entries**: Overview of latest health logs

### 4. Reminders System
- **Medication Reminders**: Set medication schedules
- **Exercise Reminders**: Track workout schedules
- **Appointment Reminders**: Health appointment notifications
- **Custom Reminders**: Create personalized health reminders

### 5. PDF Export
- **Comprehensive Reports**: Generate detailed health summaries
- **Doctor-Ready Format**: Professional reports for healthcare providers
- **Historical Data**: Include all health entries in reports

## 🔧 Configuration

### Application Properties
```properties
# Server Configuration
server.port=8080
spring.application.name=HealthTracker

# Ollama AI Configuration
spring.ai.ollama.api-key=your_ollama_api_key
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.model=llama2

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/healthtracker
spring.data.mongodb.database=healthtracker

# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
```

## 🚀 Deployment



### Production Deployment
1. Set up MongoDB (local or cloud)
2. Configure Ollama (see above)
3. Build the application: `mvn clean package`
4. Deploy the JAR file to your server
5. Configure environment variables for production settings

## 🔒 Security Features

- **Spring Security**: Basic authentication and authorization
- **CSRF Protection**: Cross-site request forgery protection
- **Input Validation**: Server-side validation for all inputs
- **Secure Headers**: Security headers for web application

## 📈 Future Enhancements

- **User Authentication**: Full user registration and login system
- **Mobile App**: Native mobile application
- **Advanced Analytics**: More sophisticated health trend analysis
- **Integration APIs**: Connect with fitness trackers and health devices
- **Machine Learning**: Custom ML models for health prediction
- **Social Features**: Share health goals with friends and family