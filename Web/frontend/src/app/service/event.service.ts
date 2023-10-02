import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class EventService {
  uri = 'http://localhost:4000';

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get(`${this.uri}/event/getAll`);
  }

  insert(data) {
    return this.http.post(`${this.uri}/event/insert`, data);
  }

  update(data) {
    return this.http.post(`${this.uri}/event/update`, data);
  }

  remove(data) {
    return this.http.post(`${this.uri}/event/remove`, data);
  }

  getPhoto(data) {
    return this.http.post(`${this.uri}/event/getPhoto`, data, {
      responseType: 'blob',
    });
  }

  getLikedEventsFlags(data) {
    return this.http.post(`${this.uri}/event/getLikedEventsFlags`, data);
  }

  like(data) {
    return this.http.post(`${this.uri}/event/like`, data);
  }

  dislike(data) {
    return this.http.post(`${this.uri}/event/dislike`, data);
  }
}
