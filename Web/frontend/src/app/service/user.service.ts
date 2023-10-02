import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  uri = 'http://localhost:4000';

  constructor(private http: HttpClient) {}

  update(data) {
    return this.http.post(`${this.uri}/user/update`, data);
  }

  remove(data) {
    return this.http.post(`${this.uri}/user/remove`, data);
  }

  accept(data) {
    return this.http.post(`${this.uri}/user/accept`, data);
  }

  reject(data) {
    return this.http.post(`${this.uri}/user/reject`, data);
  }

  getAll() {
    return this.http.get(`${this.uri}/user/getAll`);
  }

  getAllPending() {
    return this.http.get(`${this.uri}/user/getAllPending`);
  }

  getByIds(data) {
    return this.http.post(`${this.uri}/user/getByIds`, data);
  }

  getUsernameById(data) {
    return this.http.post(`${this.uri}/user/getUsernameById`, data);
  }

  getById(data) {
    return this.http.post(`${this.uri}/user/getById`, data);
  }

  login(data) {
    return this.http.post(`${this.uri}/user/login`, data);
  }

  register(data) {
    return this.http.post(`${this.uri}/user/register`, data);
  }
}
