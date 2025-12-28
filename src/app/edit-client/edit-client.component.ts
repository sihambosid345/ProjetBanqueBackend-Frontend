import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ClientService } from '../services/client.service';
import { Client } from '../model/client.model';

@Component({
  selector: 'app-edit-client',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-client.component.html',
  styleUrls: ['./edit-client.component.css']
})
export class EditClientComponent implements OnInit {
  clientFormGroup!: FormGroup;
  clientId!: number;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private clientService: ClientService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.clientId = +this.route.snapshot.params['id'];

    this.clientFormGroup = this.fb.group({
      nom: [''],
      email: ['']
    });

    this.clientService.getClientById(this.clientId).subscribe((client: Client) => {
      this.clientFormGroup.patchValue(client);
    });
  }

  handleUpdateClient() {
    const updatedClient: Client = this.clientFormGroup.value;
    this.clientService.updateClient(this.clientId, updatedClient).subscribe(() => {
      alert("Client modifié avec succès");
      this.router.navigateByUrl('/clients');
    });
  }

  handleCancel() {
    this.router.navigateByUrl('/clients');
  }
}
