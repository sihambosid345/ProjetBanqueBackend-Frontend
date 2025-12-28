import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AccountDetails, Compte } from '../model/account.model';

@Injectable({ providedIn: 'root' })
export class AccountsService {
  private baseUrl = `${environment.backendHost}/api`;

  constructor(private http: HttpClient) {}

  getAccountsByClient(clientId: number): Observable<Compte[]> {
    return this.http.get<Compte[]>(`${this.baseUrl}/clients/${clientId}/accounts`);
  }

  getAccount(accountId: string, page: number, size: number): Observable<AccountDetails> {
    return this.http.get<AccountDetails>(
      `${this.baseUrl}/accounts/${accountId}/pageOperations?page=${page}&size=${size}`
    );
  }

  debit(accountId: string, amount: number, description: string) {
    return this.http.post(`${this.baseUrl}/accounts/debit`, { id: accountId, amount, description });
  }

  credit(accountId: string, amount: number, description: string) {
    return this.http.post(`${this.baseUrl}/accounts/credit`, { accountId, amount, description });
  }


  transfer(source: string, destination: string, amount: number) {
    return this.http.post(`${this.baseUrl}/accounts/transfer`, {
      accountSource: source,
      accountDestination: destination,
      amount
    });
  }

}
