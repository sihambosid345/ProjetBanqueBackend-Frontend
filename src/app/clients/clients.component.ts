import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ClientService } from '../services/client.service';
import { AuthService } from '../services/auth.service';
import { Client } from '../model/client.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.css']
})
export class ClientsComponent implements OnInit {
  clients!: Observable<Client[]>;
  searchformGroup!: FormGroup;
  isAdmin = false;

  constructor(
    private clientService: ClientService,
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.searchformGroup = this.fb.group({ keyword: [''] });
    this.isAdmin = this.authService.isAdmin();
    this.loadClients();
  }

  loadClients() {
    this.clients = this.clientService.getClients();
  }

  handleSearchClients() {
    const keyword = this.searchformGroup.value.keyword;
    this.clients = this.clientService.searchClients(keyword);
  }

  handleDeleteClient(client: Client) {
    if (confirm("Voulez-vous vraiment supprimer ce client ?")) {
      this.clientService.deleteClient(client.id).subscribe(() => this.loadClients());
    }
  }

  handleEditClient(client: Client) {
    this.router.navigate(['/edit-client', client.id]);
  }

  handleClientAccounts(client: Client) {
    this.router.navigate(['/client-accounts', client.id]);
  }

  trackByClientId(index: number, client: Client): number {
    return client.id;
  }
}
