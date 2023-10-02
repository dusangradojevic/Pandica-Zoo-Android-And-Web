import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  uri = 'http://localhost:4000';

  constructor(private http: HttpClient) {}

  getAllByEventId(data) {
    return this.http.post(`${this.uri}/comment/getAllByEventId`, data);
  }

  insert(data) {
    return this.http.post(`${this.uri}/comment/insert`, data);
  }

  update(data) {
    return this.http.post(`${this.uri}/comment/update`, data);
  }

  remove(data) {
    return this.http.post(`${this.uri}/comment/remove`, data);
  }
}
