# SmartTask Manager

**Version:** 1.0.0  
**Status:** Production Ready

A modern, full-stack task management application built with Quarkus, Angular, and MongoDB.

## Tech Stack

**Backend:** Java 21, Quarkus 3.15.1, MongoDB, JWT, Swagger (OpenAPI)  
**Frontend:** Angular 17, TypeScript, RxJS, Standalone Components  
**Security:** JWT Authentication with SHA-256 password hashing  
**Containerization:** Docker, Docker Compose

---

## Quick Start

### Using Docker Compose (Recommended)

```bash
# Clone the repository
git clone <repository-url>
cd smart-task-manager

# Start all services (MongoDB, Backend, Frontend)
docker-compose up --build

# Access the application
# Frontend: http://localhost:8081
# Backend API: http://localhost:8080
# Swagger UI: http://localhost:8080/q/swagger-ui
```

### Local Development

#### Prerequisites
- Java 21+
- Node.js 20+
- MongoDB 7+
- Maven 3.9+ (or use included wrapper)

#### Start Backend
```bash
cd smarttask-backend
mvn quarkus:dev
# Running on http://localhost:8080
```

#### Start Frontend
```bash
cd smarttask-frontend
npm install
npm start
# Running on http://localhost:8081
```

---

---

## Project Structure

```
smart-task-manager/
├── smarttask-backend/          # Quarkus REST API
│   ├── src/
│   │   ├── main/java/com/smarttask/
│   │   │   ├── dto/           # Data Transfer Objects
│   │   │   ├── exception/     # Exception handlers
│   │   │   ├── model/         # MongoDB entities
│   │   │   ├── repository/    # Data access layer
│   │   │   ├── resource/      # REST endpoints
│   │   │   ├── service/       # Business logic
│   │   │   └── util/          # Utilities
│   │   └── resources/
│   ├── Dockerfile
│   └── pom.xml
│
├── smarttask-frontend/         # Angular SPA
│   ├── src/
│   │   └── app/
│   │       ├── components/    # UI components
│   │       ├── services/      # API services
│   │       ├── guards/        # Route guards
│   │       ├── interceptors/  # HTTP interceptors
│   │       └── models/        # TypeScript interfaces
│   ├── Dockerfile
│   └── package.json
│
└── docker-compose.yml          # Multi-container orchestration
```

---

## Features

### Core Functionality
✅ User authentication (register, login, JWT tokens)  
✅ Full CRUD operations for tasks  
✅ Task filtering (by status, category)  
✅ Task categorization  
✅ Due date management  
✅ Mark tasks as complete/pending  

### Technical Features
✅ RESTful API with OpenAPI/Swagger documentation  
✅ JWT-based authentication  
✅ Password hashing with SHA-256  
✅ CORS configuration for cross-origin requests  
✅ Route guards for protected pages  
✅ HTTP interceptors for error handling  
✅ Responsive UI design  
✅ Docker containerization  
✅ Health checks and monitoring  

---

## API Endpoints

### Authentication
- `POST /api/auth/register` - Create new user account
- `POST /api/auth/login` - Authenticate and receive JWT token

### Tasks (Protected)
- `GET /api/tasks` - List all tasks (with optional filters)
- `GET /api/tasks/{id}` - Get specific task
- `POST /api/tasks` - Create new task
- `PUT /api/tasks/{id}` - Update existing task
- `DELETE /api/tasks/{id}` - Delete task

Query Parameters:
- `completed` - Filter by completion status (true/false)
- `category` - Filter by category name

---

## Configuration

### Backend Configuration
File: `smarttask-backend/src/main/resources/application.properties`

```properties
# MongoDB
quarkus.mongodb.connection-string=${MONGODB_URI:mongodb://localhost:27017}
quarkus.mongodb.database=smarttask

# JWT
app.jwt.secret=${JWT_SECRET:changeme-secret-key}
app.jwt.duration=86400

# CORS
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:8081
```

### Frontend Configuration
File: `smarttask-frontend/src/environments/environment.ts`

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

### Docker Environment Variables

```yaml
# docker-compose.yml
environment:
  MONGODB_URI: mongodb://mongodb:27017
  JWT_SECRET: your-production-secret-key
```

---

## Testing

### Backend Tests

```bash
cd smarttask-backend

# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report

# Run integration tests
mvn verify
```

Test Coverage: Unit tests for services, repositories, and utilities.

### Frontend Tests

```bash
cd smarttask-frontend

# Run unit tests
npm test

# Run with coverage
npm test -- --code-coverage
```

Test Coverage: Service tests and component tests with Jasmine/Karma.

---

## Deployment

### Production Docker Build

```bash
# Build all images
docker-compose build

# Start in production mode
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Individual Container Deployment

```bash
# Backend
cd smarttask-backend
docker build -t smarttask-backend .
docker run -p 8080:8080 \
  -e MONGODB_URI=mongodb://your-mongo-host:27017 \
  -e JWT_SECRET=your-secret-key \
  smarttask-backend

# Frontend
cd smarttask-frontend
docker build -t smarttask-frontend .
docker run -p 8081:8081 smarttask-frontend
```

---

## Security Considerations

### Implemented
- ✅ JWT token-based authentication
- ✅ Password hashing with salt (SHA-256)
- ✅ CORS configuration
- ✅ Protected API endpoints
- ✅ Token expiration (24 hours default)
- ✅ HTTP-only communication in dev, HTTPS in production

### Production Recommendations
- 🔒 Use strong JWT secret keys
- 🔒 Enable HTTPS/TLS
- 🔒 Configure rate limiting
- 🔒 Implement refresh tokens
- 🔒 Add request validation
- 🔒 Enable MongoDB authentication
- 🔒 Use environment-specific secrets

---

## Monitoring and Health Checks

### Backend Health Endpoints
- `GET /q/health` - Application health status
- `GET /q/metrics` - Application metrics
- `GET /q/health/live` - Liveness probe
- `GET /q/health/ready` - Readiness probe

### Docker Health Checks
Both backend and frontend containers include health checks:
- Backend: HTTP GET on `/q/health`
- MongoDB: Ping database

---

## Troubleshooting

### Backend Issues

**MongoDB Connection Failed**
```bash
# Check if MongoDB is running
docker ps | grep mongo

# Check connection string
echo $MONGODB_URI
```

**Port Already in Use**
```bash
# Find process using port 8080
lsof -i :8080

# Kill process or change port in application.properties
```

**API Connection Refused**
- Ensure backend is running on port 8080
- Check CORS configuration in backend
- Verify `apiUrl` in environment.ts

**Dependencies Installation Failed**
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

### Docker Issues

**Build Failures**
```bash
# Clean Docker cache
docker-compose down -v
docker system prune -a

# Rebuild from scratch
docker-compose build --no-cache
```

---

## Development Workflow

1. **Start MongoDB**
   ```bash
   docker run -d -p 27017:27017 mongo:7
   ```

2. **Run Backend in Dev Mode**
   ```bash
   cd smarttask-backend
   mvn quarkus:dev
   ```
   Features:
   - Hot reload
   - Dev UI: http://localhost:8080/q/dev
   - Swagger UI: http://localhost:8080/q/swagger-ui

3. **Run Frontend in Dev Mode**
   ```bash
   cd smarttask-frontend
   npm start
   ```
   Features:
   - Live reload
   - Source maps
   - Fast incremental builds

4. **Make Changes**
   - Backend: Changes auto-reload with Quarkus Dev Mode
   - Frontend: Changes auto-reload with Angular CLI

5. **Test Changes**
   ```bash
   # Backend
   mvn test
   
   # Frontend
   npm test
   ```

---

## License

This project is licensed under the MIT License.

---

## Contributors

Built as a demonstration of modern full-stack Java development with:
- Clean architecture
- RESTful API design
- JWT authentication
- Containerization
- Comprehensive testing

---

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review component-specific READMEs:
   - `smarttask-backend/README.md`
   - `smarttask-frontend/README.md`
3. Check Swagger documentation at `/q/swagger-ui`
