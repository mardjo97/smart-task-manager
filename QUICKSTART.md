# SmartTask Manager - Quick Reference

## 🚀 Quick Start Commands

### Option 1: Docker (Easiest)
```bash
docker-compose up --build
```
Access: http://localhost:8081

### Option 2: Local Development
```bash
./start-dev.sh
```

### Option 3: Manual Start
```bash
# Terminal 1: MongoDB
docker run -d -p 27017:27017 mongo:7

# Terminal 2: Backend
cd smarttask-backend && ./mvnw quarkus:dev

# Terminal 3: Frontend
cd smarttask-frontend && npm install && npm start
```

---

## 📍 Important URLs

| Service | URL | Description |
|---------|-----|-------------|
| **Frontend** | http://localhost:8081 | Main application |
| **Backend** | http://localhost:8080 | REST API |
| **Swagger** | http://localhost:8080/q/swagger-ui | API documentation |
| **Health** | http://localhost:8080/q/health | Health check |
| **Dev UI** | http://localhost:8080/q/dev | Quarkus Dev UI |

---

## 🔑 Default Credentials

No default credentials. Register a new account:
1. Go to http://localhost:8081
2. Click "Register here"
3. Create account

---

## 📋 Common Tasks

### Register User (API)
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Login (API)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Create Task (API)
```bash
TOKEN="your-jwt-token-here"

curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "title": "My First Task",
    "description": "This is a test task",
    "dueDate": "2024-12-31",
    "category": "Work",
    "completed": false
  }'
```

### Get All Tasks (API)
```bash
curl http://localhost:8080/api/tasks \
  -H "Authorization: Bearer $TOKEN"
```

### Filter Tasks (API)
```bash
# Get completed tasks
curl "http://localhost:8080/api/tasks?completed=true" \
  -H "Authorization: Bearer $TOKEN"

# Get tasks by category
curl "http://localhost:8080/api/tasks?category=Work" \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🧪 Testing

### Backend Tests
```bash
cd smarttask-backend
./mvnw test
```

### Frontend Tests
```bash
cd smarttask-frontend
npm test
```

### API Testing with Swagger
1. Go to http://localhost:8080/q/swagger-ui
2. Click "Authorize"
3. Login to get token
4. Paste token in authorization
5. Try endpoints

---

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Kill process on port 8080 (backend)
lsof -ti:8080 | xargs kill -9

# Kill process on port 8081 (frontend)
lsof -ti:8081 | xargs kill -9
```

### MongoDB Connection Failed
```bash
# Check if MongoDB is running
docker ps | grep mongo

# Start MongoDB
docker start smarttask-mongo

# Or create new instance
docker run -d -p 27017:27017 --name smarttask-mongo mongo:7
```

### Backend Won't Start
```bash
# Clean and rebuild
cd smarttask-backend
./mvnw clean
./mvnw quarkus:dev
```

### Frontend Won't Start
```bash
# Clear cache and reinstall
cd smarttask-frontend
rm -rf node_modules package-lock.json
npm install
npm start
```

### Docker Issues
```bash
# Stop all containers
docker-compose down

# Remove all containers and volumes
docker-compose down -v

# Rebuild from scratch
docker-compose build --no-cache
docker-compose up
```

---

## 📦 Project Structure

```
smart-task-manager/
├── smarttask-backend/       # Quarkus API
├── smarttask-frontend/      # Angular UI
├── docker-compose.yml       # Container orchestration
├── start-dev.sh            # Quick start script
├── stop-dev.sh             # Stop script
└── README.md               # Main documentation
```

---

## 🔧 Configuration Files

| File | Purpose |
|------|---------|
| `smarttask-backend/src/main/resources/application.properties` | Backend config |
| `smarttask-frontend/src/environments/environment.ts` | Frontend config |
| `docker-compose.yml` | Docker services |
| `.gitignore` | Git exclusions |

---

## 📚 Additional Resources

- [Quarkus Documentation](https://quarkus.io/guides/)
- [Angular Documentation](https://angular.io/docs)
- [MongoDB Documentation](https://docs.mongodb.com/)
- [Docker Documentation](https://docs.docker.com/)

---

## 💡 Tips

1. **Use Quarkus Dev Mode** - Auto-reload on code changes
2. **Use Swagger UI** - Interactive API testing
3. **Check Health Endpoint** - Monitor app status
4. **Use Dev Scripts** - Quick start/stop with `./start-dev.sh`
5. **Check Logs** - Look in `*.log` files for errors

---

## 🆘 Getting Help

1. Check this quick reference
2. Read the main README.md
3. Check component READMEs:
   - smarttask-backend/README.md
   - smarttask-frontend/README.md
4. Review Swagger docs at /q/swagger-ui
5. Check troubleshooting section above

---

**Last Updated:** 2024
**Version:** 1.0.0
