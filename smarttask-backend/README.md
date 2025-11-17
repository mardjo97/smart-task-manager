# SmartTask Manager - Backend

RESTful API backend service built with Quarkus, MongoDB, and JWT authentication.

## Technology Stack

- **Java 21**
- **Quarkus 3.15.1** - Supersonic Subatomic Java Framework
- **MongoDB** - NoSQL database
- **JWT** - JSON Web Token authentication
- **Swagger/OpenAPI** - API documentation
- **Maven** - Build and dependency management

## Project Structure

```
smarttask-backend/
├── src/
│   ├── main/
│   │   ├── java/com/smarttask/
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── exception/        # Exception handlers
│   │   │   ├── model/            # Domain models
│   │   │   ├── repository/       # MongoDB repositories
│   │   │   ├── resource/         # REST endpoints
│   │   │   ├── service/          # Business logic
│   │   │   └── util/             # Utility classes
│   │   └── resources/
│   │       ├── META-INF/resources/  # JWT keys
│   │       └── application.properties
│   └── test/                     # Unit and integration tests
├── pom.xml
└── Dockerfile
```

## Prerequisites

- Java 21 or higher
- Maven 3.9+ (or use included Maven wrapper)
- MongoDB (local or Docker)

## Local Development

### 1. Start MongoDB

```bash
docker run -d -p 27017:27017 --name mongodb mongo:7
```

### 2. Run the application in dev mode

```bash
cd smarttask-backend
./mvnw quarkus:dev
```

The application will start on `http://localhost:8080`

### 3. Access API Documentation

- Swagger UI: http://localhost:8080/q/swagger-ui
- OpenAPI Schema: http://localhost:8080/openapi

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Tasks (requires authentication)

- `GET /api/tasks` - Get all tasks
  - Query params: `completed` (boolean), `category` (string)
- `GET /api/tasks/{id}` - Get task by ID
- `POST /api/tasks` - Create new task
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task

## Configuration

Edit `src/main/resources/application.properties`:

```properties
# MongoDB
quarkus.mongodb.connection-string=mongodb://localhost:27017
quarkus.mongodb.database=smarttask

# JWT
app.jwt.secret=your-secret-key
app.jwt.duration=86400
```

## Testing

```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

## Building

```bash
# Build JAR
./mvnw clean package

# Run the JAR
java -jar target/quarkus-app/quarkus-run.jar
```

## Docker

```bash
# Build image
docker build -t smarttask-backend .

# Run container
docker run -p 8080:8080 \
  -e MONGODB_URI=mongodb://host.docker.internal:27017 \
  smarttask-backend
```

## Security

- Passwords are hashed using SHA-256 with salt
- JWT tokens for authentication
- CORS enabled for frontend integration
- Token expiration after 24 hours (configurable)

## Health Checks

- Health: http://localhost:8080/q/health
- Metrics: http://localhost:8080/q/metrics
