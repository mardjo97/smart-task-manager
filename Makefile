# Development Scripts

# Start development environment
dev-start:
	docker-compose up -d mongodb
	cd smarttask-backend && ./mvnw quarkus:dev &
	cd smarttask-frontend && npm start &

# Stop all services
dev-stop:
	docker-compose down
	pkill -f quarkus:dev
	pkill -f "ng serve"

# Run all tests
test:
	cd smarttask-backend && ./mvnw test
	cd smarttask-frontend && npm test

# Build for production
build:
	cd smarttask-backend && ./mvnw clean package
	cd smarttask-frontend && npm run build

# Clean all build artifacts
clean:
	cd smarttask-backend && ./mvnw clean
	cd smarttask-frontend && rm -rf dist node_modules

# Docker commands
docker-build:
	docker-compose build

docker-up:
	docker-compose up -d

docker-down:
	docker-compose down

docker-logs:
	docker-compose logs -f

# Database commands
db-start:
	docker run -d -p 27017:27017 --name smarttask-mongo mongo:7

db-stop:
	docker stop smarttask-mongo && docker rm smarttask-mongo

.PHONY: dev-start dev-stop test build clean docker-build docker-up docker-down docker-logs db-start db-stop
