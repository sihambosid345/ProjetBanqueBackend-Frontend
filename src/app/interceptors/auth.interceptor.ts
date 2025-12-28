import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  console.log('Interceptor - URL:', req.url);
  console.log('Interceptor - Token:', token ? 'Present' : 'Absent');

  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    console.log('Interceptor - Authorization header added');
  }

  return next(req).pipe(
      catchError((error) => {
        console.error('Interceptor - Error:', error.status);

        if (error.status === 401 || error.status === 403) {

          const currentUrl = router.url;
          localStorage.removeItem('token');
          localStorage.removeItem('user');


          router.navigate(['/login'], {
            queryParams: { returnUrl: currentUrl }
          });
        }

        return throwError(() => error);
      })
  );
};
