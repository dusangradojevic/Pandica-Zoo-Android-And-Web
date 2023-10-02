import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AnimalService {
  uri = 'http://localhost:4000';

  constructor(private http: HttpClient) {}

  insert(data) {
    return this.http.post(`${this.uri}/animal/insert`, data);
  }

  update(data) {
    return this.http.post(`${this.uri}/animal/update`, data);
  }

  remove(data) {
    return this.http.post(`${this.uri}/animal/remove`, data);
  }

  getAll() {
    return this.http.get(`${this.uri}/animal/getAll`);
  }

  getPhoto(data) {
    return this.http.post(`${this.uri}/animal/getPhoto`, data, {
      responseType: 'blob',
    });
  }
}
