import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PromoCodeService {
  uri = 'http://localhost:4000';

  constructor(private http: HttpClient) {}

  insert(data) {
    return this.http.post(`${this.uri}/promoCode/insert`, data);
  }
}
