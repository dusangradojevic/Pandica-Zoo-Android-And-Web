import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PromoPackageService {
  uri = 'http://localhost:4000';

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get(`${this.uri}/promoPackage/getAll`);
  }

  insert(data) {
    return this.http.post(`${this.uri}/promoPackage/insert`, data);
  }

  update(data) {
    return this.http.post(`${this.uri}/promoPackage/update`, data);
  }

  remove(data) {
    return this.http.post(`${this.uri}/promoPackage/remove`, data);
  }

  getById(data) {
    return this.http.post(`${this.uri}/promoPackage/getById`, data);
  }

  getByIds(data) {
    return this.http.post(`${this.uri}/promoPackage/getByIds`, data);
  }
}
