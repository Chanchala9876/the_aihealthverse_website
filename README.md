# HealthVerse - Personal Health Journal with AI Tips

HealthVerse is a comprehensive health tracking application that combines daily health logging with AI-powered insights and recommendations. Built with Spring Boot, MongoDB, and OpenAI integration, it provides users with personalized wellness advice based on their health data.

## 🌟 Features

### Core Features
- **Daily Health Logging**: Track mood, meals, sleep, and symptoms
- **AI-Powered Insights**: Get personalized wellness recommendations using OpenAI
- **Health Analytics**: Beautiful charts and trend analysis
- **Smart Reminders**: Set medication and health appointment reminders
- **PDF Export**: Generate comprehensive health reports for doctors
- **Responsive Design**: Works seamlessly on all devices

### AI Integration
- **OpenAI GPT-3.5**: Provides intelligent health recommendations
- **Nutrition Analysis**: AI-powered meal suggestions and dietary advice
- **Mood Analysis**: Personalized mental health and wellness tips
- **Trend Analysis**: Identifies patterns in health data

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- MongoDB (local or cloud)
- OpenAI API key

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd HealthTracker
   ```

2. **Configure MongoDB**
   - Install MongoDB locally or use MongoDB Atlas
   - Update `application.properties` with your MongoDB connection string

3. **Set up OpenAI API**
   - Get your API key from [OpenAI Platform](https://platform.openai.com/)
   - Update the API key in `application.properties`

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - Open your browser and go to `http://localhost:8090`
   - Start logging your health data!

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
- **Spring Boot 3.5.3**: Main framework
- **Spring AI**: OpenAI integration
- **Spring Data MongoDB**: Database operations
- **Spring Security**: Authentication & authorization
- **Thymeleaf**: Server-side templating
- **iText PDF**: PDF generation

### Frontend
- **Bootstrap 5**: Responsive UI framework
- **Chart.js**: Interactive charts and graphs
- **Font Awesome**: Icons
- **Google Fonts**: Typography

### Database
- **MongoDB**: NoSQL database for flexible health data storage

### AI/ML
- **OpenAI GPT-3.5**: Natural language processing and health insights

## 📊 Key Features Explained

### 1. Health Logging
- **Mood Tracking**: Select from predefined moods (Happy, Good, Neutral, Tired, Stressed, Sad)
- **Meal Logging**: Record daily meals and snacks
- **Sleep Tracking**: Log sleep duration and quality
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
server.port=8090
spring.application.name=HealthVerse

# OpenAI Configuration
spring.ai.openai.api-key=your-openai-api-key
spring.ai.openai.chat.model=gpt-3.5-turbo

# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/healthverse
spring.data.mongodb.database=healthverse

# Thymeleaf Configuration
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
```

## 🚀 Deployment

### Local Development
```bash
# Run with Maven
mvn spring-boot:run

# Or build and run JAR
mvn clean package
java -jar target/HealthTracker-0.0.1-SNAPSHOT.jar
```

### Production Deployment
1. Set up MongoDB (local or cloud)
2. Configure OpenAI API key
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

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Email: support@healthverse.com
- Website: www.healthverse.com
- Phone: +1 (555) 123-4567

## 🙏 Acknowledgments

- OpenAI for providing the AI capabilities
- Spring Boot team for the excellent framework
- Bootstrap team for the responsive UI components
- Chart.js for the beautiful charts and graphs

---

**HealthVerse** - Your personal health journal with AI-powered insights! 💙 