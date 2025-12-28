import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ClientsComponent } from './clients/clients.component';
import { AccountsComponent } from './accounts/accounts.component';
import { NewClientComponent } from './new-client/new-client.component';
import { ClientAccountsComponent } from './client-accounts/client-accounts.component';
import { EditClientComponent } from './edit-client/edit-client.component';
import { AuthGuard, AdminGuard } from './guards/auth.guard';

export const routes: Routes = [
  // ========================================
  // ROUTES PUBLIQUES (sans authentification)
  // ========================================
  {
    path: 'login',
    component: LoginComponent
  },

  // ========================================
  // ROUTES ACCESSIBLES À TOUS (ADMIN + USER)
  // ========================================
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard]  // ✅ Accessible à tous les utilisateurs authentifiés
  },

  // ========================================
  // ROUTES ADMIN UNIQUEMENT
  // ========================================
  {
    path: 'accounts',
    component: AccountsComponent,
    canActivate: [AdminGuard]  // ✅ Seulement ADMIN
  },
  {
    path: 'accounts/:accountId',
    component: AccountsComponent,
    canActivate: [AdminGuard]  // ✅ Seulement ADMIN
  },
  {
    path: 'clients',
    component: ClientsComponent,
    canActivate: [AdminGuard]  // ✅ Seulement ADMIN
  },
  {
    path: 'new-client',
    component: NewClientComponent,
    canActivate: [AdminGuard]  // ✅ Seulement ADMIN
  },
  {
    path: 'edit-client/:id',
    component: EditClientComponent,
    canActivate: [AdminGuard]  // ✅ Seulement ADMIN
  },
  {
    path: 'client-accounts/:id',
    component: ClientAccountsComponent,
    canActivate: [AdminGuard]  // ✅ Seulement ADMIN
  },

  // ========================================
  // REDIRECTIONS
  // ========================================
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];
