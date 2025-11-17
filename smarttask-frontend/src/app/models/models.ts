export interface Task {
  id?: string;
  title: string;
  description?: string;
  dueDate?: string;
  completed: boolean;
  userId?: string;
  category?: string;
}

export interface User {
  id?: string;
  username: string;
  email: string;
  role?: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}
