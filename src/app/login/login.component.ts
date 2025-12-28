import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { LoginRequest } from '../model/user.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials: LoginRequest = {
    email: '',
    password: ''
  };

  errorMessage = '';
  loading = false;
  private returnUrl: string = '/dashboard'; // ✅ Par défaut dashboard

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Récupérer l'URL de retour si elle existe
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
  }

  onSubmit(): void {
    if (!this.credentials.email || !this.credentials.password) {
      this.errorMessage = 'Veuillez remplir tous les champs';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    console.log('🔐 Tentative de connexion pour:', this.credentials.email);

    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        console.log('✅ Login réussi:', response);

        // ✅ Rediriger vers returnUrl ou dashboard par défaut
        this.router.navigate([this.returnUrl]);
      },
      error: (err) => {
        console.error('❌ Erreur login:', err);
        this.loading = false;

        if (err.status === 401) {
          this.errorMessage = 'Email ou mot de passe incorrect';
        } else if (err.status === 0) {
          this.errorMessage = 'Impossible de se connecter au serveur';
        } else {
          this.errorMessage = 'Une erreur est survenue. Veuillez réessayer.';
        }
      }
    });
  }
}
