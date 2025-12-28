import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientService } from '../services/client.service';
import { AccountsService } from '../services/accounts.service';
import { Client } from '../model/client.model';
import { Compte } from '../model/account.model';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-client-accounts',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './client-accounts.component.html',
})
export class ClientAccountsComponent implements OnInit {

  clientId!: number;
  client!: Client;
  comptes: Compte[] = [];
  loading = true;
  errorMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private clientService: ClientService,
    private accountsService: AccountsService
  ) {}

  ngOnInit(): void {
    this.clientId = +this.route.snapshot.params['id'];

    forkJoin({
      client: this.clientService.getClientById(this.clientId),
      comptes: this.accountsService.getAccountsByClient(this.clientId)
    }).subscribe({
      next: (data) => {
        this.client = data.client;
        this.comptes = data.comptes;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Erreur lors du chargement des données';
        this.loading = false;
      }
    });
  }

  viewDetails(compte: Compte): void {
    this.router.navigate(['/accounts', compte.id]);
  }
}
