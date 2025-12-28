import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client } from '../model/client.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ClientService {
  constructor(private http: HttpClient) {}

  public getClients(): Observable<Array<Client>> {
    return this.http.get<Array<Client>>(`${environment.backendHost}/api/clients`);
  }

  public searchClients(keyword: string): Observable<Array<Client>> {
    return this.http.get<Array<Client>>(`${environment.backendHost}/api/clients/search?keyword=${keyword}`);
  }

  public saveClient(client: Client): Observable<Client> {
    return this.http.post<Client>(`${environment.backendHost}/api/clients`, client);
  }

  public deleteClient(id: number) {
    return this.http.delete(`${environment.backendHost}/api/clients/${id}`);
  }

  public getClientById(id: number): Observable<Client> {
    return this.http.get<Client>(`${environment.backendHost}/api/clients/${id}`);
  }

  public updateClient(id: number, client: Client): Observable<Client> {
    return this.http.put<Client>(`${environment.backendHost}/api/clients/${id}`, client);
  }
}
