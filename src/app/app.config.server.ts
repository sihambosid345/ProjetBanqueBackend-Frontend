// src/app/app.config.server.ts
import { ValueProvider } from '@angular/core';

export const API_URL = 'http://localhost:8080/api';

export const serverConfigProviders: ValueProvider[] = [
  { provide: 'API_URL', useValue: API_URL }
];
