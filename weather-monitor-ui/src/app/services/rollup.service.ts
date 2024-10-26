import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { WeatherSummary } from '../models/weather-summary.model';

@Injectable({
  providedIn: 'root'
})
export class RollupService {
  private baseUrl = 'http://localhost:8090/api/weather';

  constructor(private http: HttpClient) {}
  getAllDailySummaries(): Observable<WeatherSummary[]> {
    return this.http.get<{ [date: string]: WeatherSummary }>(`${this.baseUrl}/all-daily-summaries`)
      .pipe(
        map(data => Object.values(data)) // Convert to array of WeatherSummary
      );
  }
  getDailySummaryByDate(date: string): Observable<WeatherSummary> {
    return this.http.post<WeatherSummary>(`${this.baseUrl}/summary/daily`, { date });
  }
}
