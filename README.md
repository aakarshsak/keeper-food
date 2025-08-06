# 🍎 Food Keeper - Personal Food Tracking Application

A modern, full-stack web application built with **Spring Boot** (backend) and **React** (frontend) that helps you track your food consumption, monitor calorie intake, and maintain a detailed personal food log with secure user authentication.

## ✨ Features

### 🔐 User Authentication & Security
- **Multi-authentication support**: Email/Password, Google OAuth2
- **Email verification** with OTP (One-Time Password)
- **Password reset** functionality with email OTP
- **JWT-based authentication** for secure API access
- **User-specific data isolation** - each user sees only their own food entries
- **Session management** with automatic token refresh

### 📊 Food Tracking & Management
- **Add food items** with names, descriptions, quantities, and calories
- **Track consumption dates** with precise timestamps
- **Edit existing entries** with modern, intuitive modal interface
- **Delete unwanted entries** with confirmation prompts
- **Real-time search** by food name or description
- **Advanced filtering**: All items, Recent (7 days), Consumed items, Items with calories
- **Statistics dashboard** with total items, consumed items, and calorie tracking

### 📈 Data Export & Analytics
- **CSV export functionality** with date range filtering
- **Calorie consumption tracking** and summaries
- **Visual statistics** with modern card-based dashboard
- **Time-based filtering** for historical analysis

### 🎨 Modern User Interface
- **Responsive design** optimized for mobile and desktop
- **Modern Material Design** inspired interface
- **Dark theme accents** with gradient elements
- **Smooth animations** and micro-interactions
- **Accessible design** with proper ARIA labels and keyboard navigation
- **Professional styling** with consistent branding

## 🛠 Technology Stack

### Backend
- **Spring Boot 3.2.0** - Java framework with auto-configuration
- **Spring Security 6** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **MySQL 8** - Production-ready relational database
- **JWT (JSON Web Tokens)** - Stateless authentication
- **OAuth2 Client** - Google authentication integration
- **Spring Mail** - Email service for OTPs and notifications
- **BCrypt** - Password encryption
- **Maven** - Dependency management
- **Java 17** - Modern Java features

### Frontend
- **React 18** - Modern JavaScript library with hooks
- **React Router DOM 6** - Client-side routing and navigation
- **Axios** - HTTP client with request/response interceptors
- **Context API** - Global state management for authentication
- **date-fns** - Date manipulation and formatting
- **Modern CSS3** - Advanced styling with Grid, Flexbox, and animations
- **Responsive Design** - Mobile-first approach

### Database & Infrastructure
- **MySQL 8** - Primary database with auto-creation support
- **Environment-based configuration** - Secure credential management
- **CORS configuration** - Cross-origin resource sharing
- **Scheduled tasks** - Automatic OTP cleanup

## 📁 Project Structure

```
food-keeper/
├── backend/                           # Spring Boot backend
│   ├── src/main/java/com/foodkeeper/
│   │   ├── FoodKeeperApplication.java # Main application class
│   │   ├── config/                    # Security & OAuth2 configuration
│   │   │   ├── SecurityConfig.java
│   │   │   ├── OAuth2AuthenticationSuccessHandler.java
│   │   │   └── OAuth2AuthenticationFailureHandler.java
│   │   ├── controller/                # REST API controllers
│   │   │   ├── AuthController.java    # Authentication endpoints
│   │   │   └── FoodItemController.java # Food item CRUD operations
│   │   ├── dto/                       # Data Transfer Objects
│   │   │   ├── AuthRequest.java       # Authentication request DTOs
│   │   │   └── AuthResponse.java      # Authentication response DTOs
│   │   ├── model/                     # JPA Entity models
│   │   │   ├── User.java              # User entity with UserDetails
│   │   │   ├── FoodItem.java          # Food item entity
│   │   │   ├── OtpVerification.java   # OTP verification entity
│   │   │   ├── AuthProvider.java      # Authentication provider enum
│   │   │   ├── Role.java              # User roles enum
│   │   │   └── OtpType.java           # OTP type enum
│   │   ├── repository/                # JPA Repositories
│   │   │   ├── UserRepository.java
│   │   │   ├── FoodItemRepository.java
│   │   │   └── OtpVerificationRepository.java
│   │   ├── security/                  # Security components
│   │   │   ├── JwtUtils.java          # JWT token utilities
│   │   │   └── AuthTokenFilter.java   # JWT authentication filter
│   │   └── service/                   # Business logic services
│   │       ├── AuthService.java       # Authentication service
│   │       ├── UserDetailsServiceImpl.java # Spring Security integration
│   │       ├── FoodItemService.java   # Food item business logic
│   │       ├── EmailService.java      # Email sending service
│   │       └── OtpService.java        # OTP generation and validation
│   ├── src/main/resources/
│   │   └── application.properties     # Application configuration
│   ├── .env                          # Environment variables (not in git)
│   └── pom.xml                       # Maven dependencies
└── frontend/                         # React frontend
    ├── public/
    │   ├── index.html
    │   └── manifest.json
    ├── src/
    │   ├── components/
    │   │   ├── auth/                  # Authentication components
    │   │   │   ├── Login.js           # Login form
    │   │   │   ├── Register.js        # Registration form
    │   │   │   ├── OTPVerification.js # Email verification
    │   │   │   ├── ForgotPassword.js  # Password reset
    │   │   │   ├── OAuth2RedirectHandler.js # OAuth2 callback
    │   │   │   └── Auth.css           # Authentication styles
    │   │   ├── Dashboard.js           # Main dashboard with user info
    │   │   ├── FoodTracker.js         # Core food tracking functionality
    │   │   ├── FoodItemForm.js        # Add new food item form
    │   │   ├── FoodItemList.js        # Display food items list
    │   │   ├── EditFoodItemModal.js   # Edit food item modal
    │   │   ├── SearchBar.js           # Real-time search component
    │   │   ├── CSVExport.js           # Data export functionality
    │   │   ├── ProtectedRoute.js      # Route protection wrapper
    │   │   ├── Dashboard.css          # Dashboard styles
    │   │   └── EditFoodItemModal.css  # Modal styles
    │   ├── context/
    │   │   └── AuthContext.js         # Global authentication state
    │   ├── services/
    │   │   └── api.js                 # API client with interceptors
    │   ├── App.js                     # Main app with routing
    │   ├── App.css                    # Global styles
    │   ├── index.js                   # React entry point
    │   └── index.css                  # Base styles
    └── package.json                   # Frontend dependencies
```

## 🚀 Getting Started

### Prerequisites

- **Java 17 or higher** - For Spring Boot backend
- **Node.js 16 or higher** - For React frontend
- **MySQL 8** - Database server
- **npm or yarn** - Package manager
- **Maven** - Build tool (or use included wrapper)

### Database Setup

1. **Install MySQL 8** and ensure it's running on port 3306
2. **Create a user** (optional - app will use hbstudent/hbstudent by default):
   ```sql
   CREATE USER 'hbstudent'@'localhost' IDENTIFIED BY 'hbstudent';
   GRANT ALL PRIVILEGES ON *.* TO 'hbstudent'@'localhost';
   FLUSH PRIVILEGES;
   ```
3. The application will **automatically create** the `foodkeeper` database on startup

### Backend Setup

1. **Navigate to the backend directory**:
   ```bash
   cd backend
   ```

2. **Create environment file** (`.env`) with your configuration:
   ```env
   # Database Configuration
   DB_URL=jdbc:mysql://localhost:3306/foodkeeper?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   DB_USERNAME=hbstudent
   DB_PASSWORD=hbstudent
   DB_DRIVER=com.mysql.cj.jdbc.Driver

   # JWT Configuration
   JWT_SECRET=your-super-secret-jwt-key-here
   JWT_EXPIRATION=86400000

   # Email Configuration (Gmail SMTP)
   MAIL_HOST=smtp.gmail.com
   MAIL_PORT=587
   MAIL_USERNAME=your-email@gmail.com
   MAIL_PASSWORD=your-gmail-app-password

   # Google OAuth2 Configuration
   GOOGLE_CLIENT_ID=your-google-client-id
   GOOGLE_CLIENT_SECRET=your-google-client-secret
   GOOGLE_REDIRECT_URI=http://localhost:8080/oauth2/callback/google
   ```

3. **Start the backend**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Backend will be available** at `http://localhost:8080`

### Frontend Setup

1. **Navigate to the frontend directory**:
   ```bash
   cd frontend
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Start the development server**:
   ```bash
   npm start
   ```

4. **Frontend will be available** at `http://localhost:3000`

### Google OAuth2 Setup (Optional)

1. **Go to [Google Cloud Console](https://console.cloud.google.com/)**
2. **Create a new project** or select existing one
3. **Enable Google+ API** and **OAuth2**
4. **Create OAuth2 credentials**:
   - Application type: Web Application
   - Authorized JavaScript origins: `http://localhost:3000`
   - Authorized redirect URIs: `http://localhost:8080/oauth2/callback/google`
5. **Copy Client ID and Secret** to your `.env` file

## 🔌 API Endpoints

### Authentication Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | User login | No |
| POST | `/api/auth/verify-email` | Verify email with OTP | No |
| POST | `/api/auth/resend-verification` | Resend verification OTP | No |
| POST | `/api/auth/forgot-password` | Send password reset OTP | No |
| POST | `/api/auth/reset-password` | Reset password with OTP | No |
| GET | `/api/auth/me` | Get current user info | Yes |
| POST | `/api/auth/logout` | User logout | Yes |

### Food Item Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/food-items` | Get user's food items | Yes |
| GET | `/api/food-items/{id}` | Get food item by ID | Yes |
| POST | `/api/food-items` | Create new food item | Yes |
| PUT | `/api/food-items/{id}` | Update food item | Yes |
| DELETE | `/api/food-items/{id}` | Delete food item | Yes |
| GET | `/api/food-items/search?name={name}` | Search food items | Yes |
| GET | `/api/food-items/recent` | Get recent items | Yes |
| GET | `/api/food-items/count` | Get statistics | Yes |
| GET | `/api/food-items/export` | Export to CSV | Yes |

### OAuth2 Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/oauth2/authorize/google` | Initiate Google OAuth2 |
| GET | `/oauth2/callback/google` | Google OAuth2 callback |

## 📝 Data Models

### User Model
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "provider": "LOCAL",
  "emailVerified": true,
  "profilePicture": "https://example.com/avatar.jpg",
  "role": "USER",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Food Item Model
```json
{
  "id": 1,
  "name": "Grilled Chicken Breast",
  "description": "Seasoned with herbs and spices",
  "calorie": 250,
  "quantity": "150g",
  "consumedDate": "2024-01-15T12:30:00",
  "createdAt": "2024-01-15T10:30:00"
}
```

## 🎯 Key Features in Detail

### 🔐 Authentication System
- **Multi-provider support**: Local email/password and Google OAuth2
- **Email verification**: Required for new accounts with OTP
- **Secure password reset**: OTP-based password recovery
- **JWT tokens**: Stateless authentication with automatic refresh
- **Session management**: Persistent login with secure token storage

### 📊 Food Tracking
- **Personal food log**: Each user has their own isolated data
- **Rich food entries**: Name, description, calories, quantity, timestamps
- **Smart search**: Real-time filtering by name or description
- **Category filters**: All, Recent (7 days), Consumed, With Calories
- **Statistics dashboard**: Visual summary of your food tracking data

### 🎨 Modern User Experience
- **Responsive design**: Optimized for all screen sizes
- **Intuitive interface**: Clean, modern design with smooth animations
- **Accessibility**: Keyboard navigation and screen reader support
- **Real-time feedback**: Instant validation and error messages
- **Progressive enhancement**: Works on all modern browsers

### 📈 Data Export
- **CSV export**: Download your food data for external analysis
- **Date filtering**: Export specific time ranges
- **Comprehensive data**: All food item details included

## 🔧 Development

### Backend Development
- **Environment-based config**: Secure credential management with `.env`
- **Auto-database creation**: MySQL database created automatically
- **JWT security**: Stateless authentication with role-based access
- **Email integration**: SMTP configuration for OTP delivery
- **Scheduled tasks**: Automatic cleanup of expired OTPs

### Frontend Development
- **Hot reloading**: Instant updates during development
- **Context API**: Global state management for authentication
- **Axios interceptors**: Automatic token injection and error handling
- **Protected routes**: Automatic redirection for unauthenticated users
- **Modern CSS**: Grid, Flexbox, and CSS custom properties

## 🏗 Building for Production

### Backend
```bash
cd backend
./mvnw clean package
java -jar target/food-keeper-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
cd frontend
npm run build
# Deploy the build folder to your web server
```

### Environment Variables for Production
Update your `.env` file with production values:
- Use strong JWT secrets
- Configure production email service
- Set up production OAuth2 credentials
- Use production database connection

## 🐛 Troubleshooting

### Common Issues

**Backend won't start:**
- Verify Java 17+ is installed
- Check MySQL is running on port 3306
- Ensure database credentials are correct in `.env`
- Verify all environment variables are set

**Authentication not working:**
- Check JWT secret is set in `.env`
- Verify email service configuration
- Ensure Google OAuth2 credentials are correct
- Check CORS configuration for frontend domain

**Frontend API errors:**
- Verify backend is running on port 8080
- Check browser network tab for CORS errors
- Ensure JWT token is being sent in requests
- Verify API endpoints are accessible

**Database issues:**
- Ensure MySQL 8 is installed and running
- Check user has proper database permissions
- Verify connection string in `.env`
- Check for port conflicts (default: 3306)

## 🚀 Deployment

### Docker Deployment (Recommended)
```dockerfile
# Backend Dockerfile
FROM openjdk:17-jdk-slim
COPY target/food-keeper-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Setup
- Set up production MySQL database
- Configure email service (Gmail/SendGrid)
- Set up Google OAuth2 for production domain
- Use strong JWT secrets and passwords

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Make your changes** with proper commit messages
4. **Add tests** for new functionality
5. **Ensure all tests pass**: `mvn test` and `npm test`
6. **Submit a pull request** with detailed description

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spring Boot** team for the excellent framework
- **React** team for the powerful frontend library
- **MySQL** for reliable database solution
- **Google** for OAuth2 integration
- **Bootstrap** and **Material Design** for UI inspiration

---

**Built with ❤️ by the Food Keeper team**

*Start tracking your food journey today! 🍎*