import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ClientService } from '../services/client.service';
import { Client } from '../model/client.model';
import {Router} from '@angular/router';

@Component({
  selector: 'app-new-client',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './new-client.component.html',
  styleUrls: ['./new-client.component.css']
})
export class NewClientComponent implements OnInit {

  newClientFormGroup!: FormGroup;

  constructor(private fb: FormBuilder, private clientService: ClientService,private router:Router) {}

  ngOnInit(): void {
    this.newClientFormGroup = this.fb.group({
      nom: ['', [Validators.required, Validators.minLength(4)]],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  handleSaveClient() {
    const client: Client = this.newClientFormGroup.value;
    this.clientService.saveClient(client).subscribe({
      next: () => {
        this.router.navigateByUrl("/clients");
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
