#!/bin/bash

echo "🚀 Starting SmartTask Manager Development Environment..."

# Check prerequisites
echo "📋 Checking prerequisites..."

if ! command -v java &> /dev/null; then
    echo "❌ Java 21 is required but not installed."
    exit 1
fi

if ! command -v node &> /dev/null; then
    echo "❌ Node.js is required but not installed."
    exit 1
fi

if ! command -v docker &> /dev/null; then
    echo "❌ Docker is required but not installed."
    exit 1
fi

echo "✅ All prerequisites met!"

# Start MongoDB
echo "🗄️ Starting MongoDB..."
docker run -d -p 27017:27017 --name smarttask-mongo mongo:7 2>/dev/null || docker start smarttask-mongo

# Wait for MongoDB
echo "⏳ Waiting for MongoDB to be ready..."
sleep 3

# Start Backend
echo "🔧 Starting Quarkus Backend..."
cd smarttask-backend
./mvnw quarkus:dev > backend.log 2>&1 &
BACKEND_PID=$!
cd ..

# Install frontend dependencies if needed
if [ ! -d "smarttask-frontend/node_modules" ]; then
    echo "📦 Installing frontend dependencies..."
    cd smarttask-frontend
    npm install
    cd ..
fi

# Start Frontend
echo "🎨 Starting Angular Frontend..."
cd smarttask-frontend
npm start > frontend.log 2>&1 &
FRONTEND_PID=$!
cd ..

echo ""
echo "✨ SmartTask Manager is starting up!"
echo ""
echo "📍 Services:"
echo "   Frontend:  http://localhost:8081"
echo "   Backend:   http://localhost:8080"
echo "   Swagger:   http://localhost:8080/q/swagger-ui"
echo "   MongoDB:   mongodb://localhost:27017"
echo ""
echo "📝 Logs:"
echo "   Backend:   smarttask-backend/backend.log"
echo "   Frontend:  smarttask-frontend/frontend.log"
echo ""
echo "⏹️  To stop: ./stop-dev.sh"
echo ""

# Save PIDs for cleanup
echo $BACKEND_PID > .backend.pid
echo $FRONTEND_PID > .frontend.pid
