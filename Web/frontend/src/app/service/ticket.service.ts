import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TicketService {
  uri = 'http://localhost:4000';

  constructor(private http: HttpClient) {}

  getAllPending() {
    return this.http.get(`${this.uri}/ticket/getAllPending`);
  }

  insert(data) {
    return this.http.post(`${this.uri}/ticket/insert`, data);
  }

  accept(data) {
    return this.http.post(`${this.uri}/ticket/accept`, data);
  }

  reject(data) {
    return this.http.post(`${this.uri}/ticket/reject`, data);
  }
}
