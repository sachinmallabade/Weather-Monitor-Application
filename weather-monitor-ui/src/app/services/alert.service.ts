// src/app/services/alert.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Alert } from '../models/alert.model';

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  private apiUrl = 'http://localhost:8090/api/weather/alerts';
  constructor(private http: HttpClient) {}

  getAlerts(): Observable<string[]> { // Adjust this if your backend returns Alert[] instead
    return this.http.get<string[]>(this.apiUrl); // Ensure this matches the API response
  }
}
