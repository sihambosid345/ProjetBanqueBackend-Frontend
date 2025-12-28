export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  email: string;
  nom: string;
  roles: string[];
}

export interface User {
  email: string;
  nom: string;
  roles: string[];
}
