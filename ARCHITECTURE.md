# SmartTask Manager - Architecture & Data Flow

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                           USER BROWSER                               │
│                                                                       │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │              Angular SPA (http://localhost:8081)                │ │
│  │                                                                  │ │
│  │  ┌─────────────┐  ┌─────────────┐  ┌──────────────────────┐   │ │
│  │  │   Login     │  │  Register   │  │    Task List         │   │ │
│  │  │ Component   │  │  Component  │  │    Component         │   │ │
│  │  └──────┬──────┘  └──────┬──────┘  └──────────┬───────────┘   │ │
│  │         │                 │                     │               │ │
│  │         └─────────────────┴─────────────────────┘               │ │
│  │                           │                                      │ │
│  │         ┌─────────────────▼─────────────────┐                   │ │
│  │         │      Auth Service / Task Service  │                   │ │
│  │         │      (HTTP Client + RxJS)         │                   │ │
│  │         └─────────────────┬─────────────────┘                   │ │
│  │                           │                                      │ │
│  │         ┌─────────────────▼─────────────────┐                   │ │
│  │         │      Auth Interceptor             │                   │ │
│  │         │      (Add JWT, Handle Errors)     │                   │ │
│  │         └─────────────────┬─────────────────┘                   │ │
│  └───────────────────────────┼──────────────────────────────────────┘ │
└───────────────────────────────┼────────────────────────────────────────┘
                                │
                    HTTP REST API (JSON)
                    Authorization: Bearer <JWT>
                                │
┌───────────────────────────────▼────────────────────────────────────────┐
│                  QUARKUS BACKEND (http://localhost:8080)               │
│                                                                         │
│  ┌──────────────────────────────────────────────────────────────────┐ │
│  │                      REST LAYER (JAX-RS)                          │ │
│  │                                                                    │ │
│  │  ┌──────────────────┐              ┌──────────────────────────┐  │ │
│  │  │  AuthResource    │              │    TaskResource          │  │ │
│  │  │  /api/auth/*     │              │    /api/tasks/*          │  │ │
│  │  │  - register      │              │    - GET /tasks          │  │ │
│  │  │  - login         │              │    - GET /tasks/{id}     │  │ │
│  │  └────────┬─────────┘              │    - POST /tasks         │  │ │
│  │           │                         │    - PUT /tasks/{id}     │  │ │
│  │           │                         │    - DELETE /tasks/{id}  │  │ │
│  │           │                         └────────┬─────────────────┘  │ │
│  └───────────┼──────────────────────────────────┼────────────────────┘ │
│              │                                   │                      │
│  ┌───────────▼──────────────┐       ┌───────────▼─────────────┐       │
│  │     SERVICE LAYER        │       │    SERVICE LAYER        │       │
│  │                          │       │                         │       │
│  │  ┌────────────────────┐ │       │  ┌───────────────────┐  │       │
│  │  │   AuthService      │ │       │  │   TaskService     │  │       │
│  │  │                    │ │       │  │                   │  │       │
│  │  │  - register()      │ │       │  │  - getAllTasks()  │  │       │
│  │  │  - login()         │ │       │  │  - getById()      │  │       │
│  │  │  - generateToken() │ │       │  │  - createTask()   │  │       │
│  │  │                    │ │       │  │  - updateTask()   │  │       │
│  │  └─────────┬──────────┘ │       │  │  - deleteTask()   │  │       │
│  └────────────┼────────────┘       │  └─────────┬─────────┘  │       │
│               │                     └────────────┼────────────┘       │
│  ┌────────────▼────────────┐       ┌────────────▼────────────┐       │
│  │  REPOSITORY LAYER       │       │  REPOSITORY LAYER       │       │
│  │                         │       │                         │       │
│  │  ┌───────────────────┐ │       │  ┌──────────────────┐   │       │
│  │  │  UserRepository   │ │       │  │  TaskRepository  │   │       │
│  │  │  (Panache)        │ │       │  │  (Panache)       │   │       │
│  │  └─────────┬─────────┘ │       │  └────────┬─────────┘   │       │
│  └────────────┼───────────┘       └───────────┼─────────────┘       │
│               │                                │                      │
│  ┌────────────▼───────────────────────────────▼────────────┐         │
│  │              MongoDB Driver / Client                     │         │
│  └────────────┬─────────────────────────────────────────────┘         │
└───────────────┼───────────────────────────────────────────────────────┘
                │
     MongoDB Wire Protocol
                │
┌───────────────▼───────────────────────────────────────────────────────┐
│                    MONGODB (localhost:27017)                           │
│                                                                         │
│  ┌──────────────────────────────────────────────────────────────────┐ │
│  │                      Database: smarttask                          │ │
│  │                                                                    │ │
│  │  ┌─────────────────────┐         ┌─────────────────────────┐    │ │
│  │  │  Collection: users  │         │  Collection: tasks      │    │ │
│  │  │                     │         │                         │    │ │
│  │  │  Documents:         │         │  Documents:             │    │ │
│  │  │  - _id              │         │  - _id                  │    │ │
│  │  │  - username         │         │  - title                │    │ │
│  │  │  - email            │         │  - description          │    │ │
│  │  │  - password (hash)  │         │  - dueDate              │    │ │
│  │  │  - role             │         │  - completed            │    │ │
│  │  │  - createdAt        │         │  - userId               │    │ │
│  │  │  - lastLogin        │         │  - category             │    │ │
│  │  │                     │         │  - createdAt            │    │ │
│  │  │                     │         │  - updatedAt            │    │ │
│  │  └─────────────────────┘         └─────────────────────────┘    │ │
│  └──────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

## Authentication Flow

```
┌──────────┐                  ┌──────────┐                 ┌──────────┐
│  Client  │                  │  Backend │                 │ MongoDB  │
└────┬─────┘                  └────┬─────┘                 └────┬─────┘
     │                             │                            │
     │ 1. POST /auth/register      │                            │
     │ {username, email, password} │                            │
     ├────────────────────────────►│                            │
     │                             │                            │
     │                             │ 2. Hash password           │
     │                             │    (SHA-256 + salt)        │
     │                             │                            │
     │                             │ 3. Save user               │
     │                             ├───────────────────────────►│
     │                             │                            │
     │                             │ 4. User saved              │
     │                             │◄───────────────────────────┤
     │                             │                            │
     │                             │ 5. Generate JWT token      │
     │                             │    - userId                │
     │                             │    - username              │
     │                             │    - role                  │
     │                             │    - expiration (24h)      │
     │                             │                            │
     │ 6. Return {token, user}     │                            │
     │◄────────────────────────────┤                            │
     │                             │                            │
     │ 7. Store token in           │                            │
     │    localStorage             │                            │
     │                             │                            │
     │                             │                            │
     │ 8. GET /tasks               │                            │
     │ Header: Authorization:      │                            │
     │         Bearer <JWT>        │                            │
     ├────────────────────────────►│                            │
     │                             │                            │
     │                             │ 9. Verify JWT              │
     │                             │    Extract userId          │
     │                             │                            │
     │                             │ 10. Query tasks            │
     │                             │     WHERE userId=...       │
     │                             ├───────────────────────────►│
     │                             │                            │
     │                             │ 11. Return tasks           │
     │                             │◄───────────────────────────┤
     │                             │                            │
     │ 12. Return tasks as JSON    │                            │
     │◄────────────────────────────┤                            │
     │                             │                            │
```

## Task Creation Flow

```
┌──────────────┐      ┌──────────────┐      ┌──────────────┐      ┌──────────┐
│  Task Form   │      │  TaskService │      │ TaskResource │      │  MongoDB │
│  Component   │      │  (Frontend)  │      │  (Backend)   │      │          │
└──────┬───────┘      └──────┬───────┘      └──────┬───────┘      └────┬─────┘
       │                     │                     │                   │
       │ 1. Submit Form      │                     │                   │
       │    {title,desc,...} │                     │                   │
       ├────────────────────►│                     │                   │
       │                     │                     │                   │
       │                     │ 2. POST /api/tasks  │                   │
       │                     │    + JWT token      │                   │
       │                     ├────────────────────►│                   │
       │                     │                     │                   │
       │                     │                     │ 3. Validate JWT   │
       │                     │                     │    Extract userId │
       │                     │                     │                   │
       │                     │                     │ 4. Validate input │
       │                     │                     │    (Bean Valid.)  │
       │                     │                     │                   │
       │                     │                     │ 5. Create entity  │
       │                     │                     │    Set timestamps │
       │                     │                     │                   │
       │                     │                     │ 6. Save to DB     │
       │                     │                     ├──────────────────►│
       │                     │                     │                   │
       │                     │                     │ 7. Saved          │
       │                     │                     │◄──────────────────┤
       │                     │                     │                   │
       │                     │ 8. Return 201       │                   │
       │                     │    Created + task   │                   │
       │                     │◄────────────────────┤                   │
       │                     │                     │                   │
       │ 9. Success          │                     │                   │
       │    Reload task list │                     │                   │
       │◄────────────────────┤                     │                   │
       │                     │                     │                   │
```

## Data Models

### User Model
```typescript
{
  id: ObjectId,
  username: string,
  email: string,
  password: string (hashed),
  role: string,
  createdAt: DateTime,
  lastLogin: DateTime
}
```

### Task Model
```typescript
{
  id: ObjectId,
  title: string,
  description: string,
  dueDate: Date,
  completed: boolean,
  userId: string,
  category: string,
  createdAt: DateTime,
  updatedAt: DateTime
}
```

### JWT Token Claims
```json
{
  "iss": "https://smarttask.com",
  "upn": "username",
  "groups": ["USER"],
  "userId": "507f1f77bcf86cd799439011",
  "email": "user@example.com",
  "exp": 1234567890
}
```

## Technology Stack Layers

```
┌─────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                      │
│  Angular 17 + TypeScript + HTML/CSS                         │
│  - Standalone Components                                     │
│  - Reactive Forms                                            │
│  - RxJS Observables                                          │
│  - Route Guards                                              │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP/REST
┌──────────────────────────▼──────────────────────────────────┐
│                      API LAYER (REST)                        │
│  JAX-RS / RESTEasy Reactive                                 │
│  - JSON serialization                                        │
│  - OpenAPI/Swagger                                           │
│  - JWT validation                                            │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                      BUSINESS LAYER                          │
│  Services (CDI Beans)                                        │
│  - Business logic                                            │
│  - Validation                                                │
│  - Authentication                                            │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                      DATA ACCESS LAYER                       │
│  Panache Repositories                                        │
│  - CRUD operations                                           │
│  - Query methods                                             │
│  - MongoDB integration                                       │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                      PERSISTENCE LAYER                       │
│  MongoDB 7                                                   │
│  - Document storage                                          │
│  - Collections: users, tasks                                 │
└─────────────────────────────────────────────────────────────┘
```

## Deployment Architecture

```
┌────────────────────────────────────────────────────────────────┐
│                      Docker Host                               │
│                                                                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │              Docker Compose Network                       │ │
│  │              (smarttask-network)                          │ │
│  │                                                            │ │
│  │  ┌────────────────┐  ┌────────────────┐  ┌────────────┐ │ │
│  │  │   Frontend     │  │    Backend     │  │  MongoDB   │ │ │
│  │  │   Container    │  │   Container    │  │ Container  │ │ │
│  │  │                │  │                │  │            │ │ │
│  │  │  nginx:alpine  │  │  openjdk:21    │  │  mongo:7   │ │ │
│  │  │  Port: 8081    │  │  Port: 8080    │  │  Port:     │ │ │
│  │  │                │  │                │  │  27017     │ │ │
│  │  │  Angular SPA   │  │  Quarkus App   │  │            │ │ │
│  │  └────────┬───────┘  └────────┬───────┘  └─────┬──────┘ │ │
│  │           │                   │                 │        │ │
│  │           └───────────────────┴─────────────────┘        │ │
│  │                                                            │ │
│  └──────────────────────────────────────────────────────────┘ │
│                                                                 │
│  Port Mappings:                                                │
│  - 8081:8081 (Frontend)                                        │
│  - 8080:8080 (Backend)                                         │
│  - 27017:27017 (MongoDB)                                       │
│                                                                 │
│  Volumes:                                                      │
│  - mongodb_data:/data/db                                       │
└────────────────────────────────────────────────────────────────┘
```

## Request/Response Examples

### Register User
**Request:**
```http
POST /api/auth/register HTTP/1.1
Content-Type: application/json

{
  "username": "john",
  "email": "john@example.com",
  "password": "secret123"
}
```

**Response:**
```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "507f1f77bcf86cd799439011",
    "username": "john",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

### Create Task
**Request:**
```http
POST /api/tasks HTTP/1.1
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "title": "Complete project",
  "description": "Finish SmartTask implementation",
  "dueDate": "2024-12-31",
  "category": "Work",
  "completed": false
}
```

**Response:**
```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": "507f191e810c19729de860ea",
  "title": "Complete project",
  "description": "Finish SmartTask implementation",
  "dueDate": "2024-12-31",
  "category": "Work",
  "completed": false,
  "userId": "507f1f77bcf86cd799439011"
}
```
