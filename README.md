# Food Tracker Application

A full-stack web application built with Spring Boot (backend) and React (frontend) to help you track your food consumption, monitor calorie intake, and maintain a detailed food log.

## Features

- ✅ Log food items with names, descriptions, and consumption dates
- ✅ Track calorie intake with optional calorie information per food item
- ✅ Record food quantities (e.g., "1 piece", "200g", "1 cup")
- ✅ View all consumed food items with timestamps
- ✅ Search food items by name or description
- ✅ Filter by categories (All, Recent, Consumed, With Calories)
- ✅ View total calorie consumption summary
- ✅ Delete food entries when needed
- ✅ Responsive design for mobile and desktop
- ✅ Real-time search functionality

## Technology Stack

### Backend
- **Spring Boot 3.2.0** - Java framework
- **Spring Data JPA** - Database abstraction
- **H2 Database** - In-memory database for development
- **Maven** - Dependency management
- **Java 17** - Programming language

### Frontend
- **React 18** - JavaScript library for UI
- **Axios** - HTTP client for API calls
- **date-fns** - Date manipulation library
- **CSS3** - Styling with modern features

## Project Structure

```
food-keeper/
├── backend/                     # Spring Boot backend
│   ├── src/main/java/com/foodkeeper/
│   │   ├── FoodKeeperApplication.java
│   │   ├── controller/
│   │   │   └── FoodItemController.java
│   │   ├── model/
│   │   │   └── FoodItem.java
│   │   ├── repository/
│   │   │   └── FoodItemRepository.java
│   │   └── service/
│   │       └── FoodItemService.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
└── frontend/                    # React frontend
    ├── public/
    ├── src/
    │   ├── components/
    │   │   ├── FoodItemForm.js
    │   │   ├── FoodItemList.js
    │   │   └── SearchBar.js
    │   ├── services/
    │   │   └── api.js
    │   ├── App.js
    │   ├── App.css
    │   ├── index.js
    │   └── index.css
    └── package.json
```

## Getting Started

### Prerequisites

- **Java 17 or higher**
- **Node.js 16 or higher**
- **npm or yarn**
- **Maven** (or use the Maven wrapper included)

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Install dependencies and run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   
   Or if you have Maven installed:
   ```bash
   mvn spring-boot:run
   ```

3. The backend will start on `http://localhost:8080`

4. You can access the H2 database console at: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:foodkeeper`
   - Username: `sa`
   - Password: `password`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. The frontend will start on `http://localhost:3000`

### Running Both Applications

1. **Start the backend first** (on port 8080)
2. **Then start the frontend** (on port 3000)
3. The React app will automatically proxy API requests to the Spring Boot backend

## API Endpoints

The backend provides the following REST API endpoints:

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/food-items` | Get all food items |
| GET | `/api/food-items/{id}` | Get food item by ID |
| POST | `/api/food-items` | Create new food item |
| PUT | `/api/food-items/{id}` | Update food item |
| DELETE | `/api/food-items/{id}` | Delete food item |
| GET | `/api/food-items/search?name={name}` | Search food items |
| GET | `/api/food-items/recent` | Get recent items (last 7 days) |
| GET | `/api/food-items/expiring-soon` | Get items expiring in 3 days |
| GET | `/api/food-items/count` | Get total count of items |

## Food Item Model

```json
{
  "id": 1,
  "name": "Milk",
  "description": "Organic whole milk",
  "createdAt": "2024-01-15T10:30:00",
  "expiryDate": "2024-01-22T00:00:00"
}
```

## Features in Detail

### Adding Food Items
- Required: Food name (max 100 characters)
- Optional: Description (max 500 characters)
- Optional: Expiry date (future dates only)
- Automatic timestamp when created

### Viewing History
- All items are sorted by creation date (newest first)
- Shows when each item was added
- Visual indicators for expiry status:
  - 🟢 Fresh (more than 3 days until expiry)
  - 🟡 Expiring Soon (within 3 days)
  - 🔴 Expired

### Search & Filter
- Real-time search by name or description
- Filter tabs:
  - **All Items**: Shows all food items
  - **Recent**: Items added in the last 7 days
  - **Expiring Soon**: Items expiring within 3 days

## Development

### Backend Development
- The application uses Spring Boot's auto-configuration
- H2 database is configured for development (data is reset on restart)
- JPA will automatically create tables based on the entity models
- CORS is configured to allow requests from `http://localhost:3000`

### Frontend Development
- React development server provides hot reloading
- Proxy configuration automatically forwards API requests to the backend
- Responsive design works on mobile and desktop
- Form validation ensures data integrity

## Building for Production

### Backend
```bash
cd backend
./mvnw clean package
java -jar target/food-keeper-backend-0.0.1-SNAPSHOT.jar
```

### Frontend
```bash
cd frontend
npm run build
# Serve the build folder with your preferred web server
```

## Troubleshooting

### Backend Issues
- **Port 8080 already in use**: Stop other applications using port 8080
- **Java version issues**: Ensure Java 17+ is installed and set as default
- **Database errors**: Check if H2 console shows the database is accessible

### Frontend Issues
- **CORS errors**: Ensure backend is running and CORS is properly configured
- **API connection errors**: Verify backend is running on port 8080
- **Build errors**: Clear node_modules and reinstall dependencies

### Common Issues
- **Backend not starting**: Check if Java 17 is installed
- **Frontend not connecting**: Ensure backend is running first
- **Data not persisting**: H2 is in-memory, data resets on backend restart

## Future Enhancements

- [ ] User authentication and authorization
- [ ] Categories for food items
- [ ] Photo upload for food items
- [ ] Email/SMS notifications for expiring items
- [ ] Barcode scanning for quick entry
- [ ] Export data functionality
- [ ] Mobile app version
- [ ] Production database (PostgreSQL/MySQL)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is open source and available under the [MIT License](LICENSE).