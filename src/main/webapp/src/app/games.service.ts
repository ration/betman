import { Injectable } from '@angular/core';
import {HttpClient, HttpRequest} from "@angular/common/http";

@Injectable()
export class GamesService {
  private apiAll = 'api/all';

  constructor(private http: HttpClient) { }

  all() {
    return this.http.get(this.apiAll);
  }
}
