import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { LoginRequest, LoginResponse, User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    this.loadUserFromStorage();
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${environment.backendHost}/api/auth/login`, credentials)
      .pipe(
        tap(response => {
          console.log('🔐 AuthService - Storing token and user');

          localStorage.setItem('token', response.token);

          const user: User = {
            email: response.email,
            nom: response.nom,
            roles: response.roles,
          };
          localStorage.setItem('user', JSON.stringify(user));

          this.currentUserSubject.next(user);

          console.log('✅ AuthService - User stored:', user);
        })
      );
  }

  logout(): void {
    console.log('🚪 AuthService - Logging out');
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    const token = localStorage.getItem('token');
    console.log('🔑 AuthService - getToken:', token ? 'Present' : 'Absent');
    return token;
  }

  getCurrentUser(): User | null {
    const user = this.currentUserSubject.value;
    console.log('👤 AuthService - getCurrentUser:', user);
    return user;
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    const user = this.getCurrentUser();
    const isAuth = !!(token && user);
    console.log('🔐 AuthService - isAuthenticated:', isAuth);
    return isAuth;
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user?.roles.includes('ROLE_ADMIN') || false;
  }

  isUser(): boolean {
    const user = this.getCurrentUser();
    return user?.roles.includes('ROLE_USER') || false;
  }

  // Méthode pour savoir si c'est un utilisateur normal (pas admin)
  isNormalUser(): boolean {
    return this.isUser() && !this.isAdmin();
  }

  private loadUserFromStorage(): void {
    const userStr = localStorage.getItem('user');
    const token = localStorage.getItem('token');

    console.log('📦 AuthService - Loading from storage');
    console.log('  Token:', token ? 'Present' : 'Absent');
    console.log('  User data:', userStr ? 'Present' : 'Absent');

    if (userStr && token) {
      try {
        const user = JSON.parse(userStr);
        this.currentUserSubject.next(user);
        console.log('✅ AuthService - User loaded from storage:', user);
      } catch (e) {
        console.error('❌ AuthService - Error parsing user from localStorage', e);
        localStorage.removeItem('user');
        localStorage.removeItem('token');
      }
    }
  }
}
