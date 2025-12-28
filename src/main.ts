import 'zone.js';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { App } from './app/app';
import { routes } from './app/app.routes';
import { authInterceptor } from './app/interceptors/auth.interceptor';

bootstrapApplication(App, {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([authInterceptor])
    )
  ]
}).catch(err => console.error(err));
