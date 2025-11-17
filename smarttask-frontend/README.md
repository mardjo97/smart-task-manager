# SmartTask Manager - Frontend

Modern Angular application for task management with responsive UI.

## Technology Stack

- **Angular 17** - Web application framework
- **TypeScript** - Typed JavaScript
- **RxJS** - Reactive programming
- **Standalone Components** - Modern Angular architecture
- **Karma/Jasmine** - Testing framework

## Project Structure

```
smarttask-frontend/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── login/           # Login component
│   │   │   ├── register/        # Registration component
│   │   │   └── task-list/       # Task management component
│   │   ├── guards/              # Route guards
│   │   ├── interceptors/        # HTTP interceptors
│   │   ├── models/              # TypeScript interfaces
│   │   ├── pipes/               # Custom pipes
│   │   ├── services/            # API services
│   │   ├── app.component.ts
│   │   ├── app.config.ts
│   │   └── app.routes.ts
│   ├── environments/            # Environment configs
│   ├── index.html
│   ├── main.ts
│   └── styles.css
├── angular.json
├── package.json
├── tsconfig.json
└── Dockerfile
```

## Prerequisites

- Node.js 20+ and npm
- Angular CLI (optional, project uses standalone mode)

## Local Development

### 1. Install dependencies

```bash
cd smarttask-frontend
npm install
```

### 2. Start development server

```bash
npm start
```

The application will start on `http://localhost:8081`

### 3. Make sure backend is running

The frontend expects the backend API at `http://localhost:8080/api`

## Features

### Authentication
- User registration
- User login
- JWT token management
- Auto-logout on token expiration
- Protected routes with AuthGuard

### Task Management
- Create, read, update, delete tasks
- Mark tasks as complete/pending
- Filter by status (all, pending, completed)
- Filter by category
- Responsive card-based layout
- Real-time UI updates

### UI/UX
- Clean, modern design
- Responsive layout (mobile-friendly)
- Form validation
- Error handling
- Loading states
- Success/error notifications

## Configuration

Edit `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

## Building

```bash
# Development build
npm run build

# Production build
npm run build -- --configuration production
```

Build artifacts will be in `dist/smarttask-frontend/`

## Testing

```bash
# Run unit tests
npm test

# Run tests with coverage
npm test -- --code-coverage
```

## Docker

```bash
# Build image
docker build -t smarttask-frontend .

# Run container
docker run -p 8081:8081 smarttask-frontend
```

## Code Structure

### Services
- **AuthService** - Authentication and user management
- **TaskService** - Task CRUD operations

### Components
- **LoginComponent** - User authentication
- **RegisterComponent** - User registration
- **TaskListComponent** - Task dashboard and management

### Guards
- **authGuard** - Protects routes requiring authentication

### Interceptors
- **AuthInterceptor** - Handles HTTP errors and token refresh

## Styling

Global styles in `src/styles.css`:
- CSS variables for theming
- Utility classes
- Responsive design
- Button styles
- Form styles
- Card layouts
