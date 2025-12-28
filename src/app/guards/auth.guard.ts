import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const AuthGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  console.log('🔒 AuthGuard - Vérification de la route :', state.url);

  const token = localStorage.getItem('token');
  if (!token) {
    console.log('❌ AuthGuard - Aucun token, redirection vers login');
    router.navigate(['/login'], {
      queryParams: { returnUrl: state.url }
    });
    return false;
  }

  // Vérifier si l'utilisateur est authentifié
  if (!authService.isAuthenticated()) {
    console.log('❌ AuthGuard - Non authentifié, redirection vers login');
    router.navigate(['/login'], {
      queryParams: { returnUrl: state.url }
    });
    return false;
  }

  // Récupérer les données utilisateur
  const user = authService.getCurrentUser();
  if (!user) {
    console.log('❌ AuthGuard - Aucune donnée utilisateur, nettoyage et redirection');
    authService.logout();
    router.navigate(['/login'], {
      queryParams: { returnUrl: state.url }
    });
    return false;
  }

  // Vérifier les rôles requis (si spécifiés)
  const requiredRoles = route.data['roles'] as string[];

  if (requiredRoles && requiredRoles.length > 0) {
    console.log('🔑 AuthGuard - Rôles requis :', requiredRoles);
    console.log('👤 AuthGuard - Rôles utilisateur :', user.roles);

    const hasRole = requiredRoles.some(role => user.roles.includes(role));

    if (!hasRole) {
      console.log('❌ AuthGuard - Accès refusé (droits insuffisants)');
      // Rediriger vers dashboard si accès refusé
      router.navigate(['/dashboard']);
      return false;
    }
  }

  console.log('✅ AuthGuard - Accès autorisé pour', user.nom, '(', user.roles.join(', '), ')');
  return true;
};

export const AdminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  console.log('🔒 AdminGuard - Vérification de la route :', state.url);

  // Vérifier si un token existe
  const token = localStorage.getItem('token');
  if (!token) {
    console.log('❌ AdminGuard - Aucun token, redirection vers login');
    router.navigate(['/login'], {
      queryParams: { returnUrl: state.url }
    });
    return false;
  }

  // Vérifier l'authentification
  if (!authService.isAuthenticated()) {
    console.log('❌ AdminGuard - Non authentifié, redirection vers login');
    router.navigate(['/login'], {
      queryParams: { returnUrl: state.url }
    });
    return false;
  }

  // Vérifier le rôle ADMIN
  if (!authService.isAdmin()) {
    console.log('❌ AdminGuard - Accès refusé (pas admin)');
    const user = authService.getCurrentUser();
    console.log('👤 AdminGuard - Utilisateur actuel :', user?.nom, '(', user?.roles.join(', '), ')');

    // Rediriger USER vers dashboard
    router.navigate(['/dashboard']);
    return false;
  }

  console.log('✅ AdminGuard - Accès autorisé (ADMIN)');
  return true;
};
