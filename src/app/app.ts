import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './navbar/navbar.component';
import {ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  template: `
    <app-navbar></app-navbar>
    <div class="container mt-3">
      <router-outlet></router-outlet>
    </div>
  `,
  imports: [RouterOutlet, NavbarComponent,ReactiveFormsModule,CommonModule]
})
export class App {}
